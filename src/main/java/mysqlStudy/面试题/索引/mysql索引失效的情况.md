**索引失效是指查询语句无法有效利用索引，导致全表扫描，严重影响查询性能。**

# 1. 不符合最左前缀原则（针对复合索引）
* 场景：
```
-- 假设有复合索引 (a, b, c)
SELECT * FROM table WHERE b = 2 AND c = 3;  -- 不使用a条件
```
* 原理：
  * 联合索引内部的排序是基于第一列。只有当第一列的数据相同，才会依据第二列排序。
* 解决方案：
  * 确保查询条件包含复合索引的最左列
  * 调整索引顺序或创建新的合适索引
# 2. 在索引列上使用函数或运算
* 场景：
```
SELECT * FROM users WHERE YEAR(create_time) = 2023;  -- 对索引列使用函数
SELECT * FROM products WHERE price + 10 > 100;       -- 对索引列进行运算
```
* 原因分析
  * 索引存储的是字段的原始值，而不是经过函数处理后的计算结果，这使得数据库无法有效利用索引。
* 解决方案：
  * 避免在索引列上使用函数或运算
  * 改为范围查询：
```
SELECT * FROM users WHERE create_time BETWEEN '2023-01-01' AND '2023-12-31';
```
# 3. 使用不等于(!= 或 <>)或NOT IN
   场景：
```
SELECT * FROM users WHERE status != 1;
SELECT * FROM orders WHERE id NOT IN (1, 2, 3);
```
解决方案：
* 尽量避免使用不等于操作
* 对于NOT IN可考虑改为LEFT JOIN + IS NULL
## 基本原理
* 索引本质上是一种有序的数据结构（如B+树），它能够快速定位特定值。当使用等值查询（=、IN）时，数据库可以高效地利用索引的有序性进行精确查找。但不等于操作和NOT IN破坏了这种有序查找的优势。
### 不等于操作(!=/<>)的失效原因
* 数据访问特性：
  * 等值查询(=)：只需要定位到索引中的特定值位置
  * 不等于查询(!=)：需要访问索引中"不等于该值"的所有记录
* 执行过程：
  * 数据库先通过索引找到等于条件的记录位置
  * 然后需要扫描所有不等于该值的记录
  * 实际上相当于扫描了几乎整个索引
* 优化器决策：
  * 当数据分布中不等于的值占比较大时（通常>20%）
  * 优化器判断全表扫描比通过索引回表更高效
  * 因此放弃使用索引
* 示例：
```
-- 假设status有索引，且status=1的记录占5%
SELECT * FROM orders WHERE status != 1;
```
即使status=1的记录很少，优化器也可能选择全表扫描，因为!=操作需要获取所有非1的记录。
### 2. NOT IN的失效原因
* 集合操作特性：
  * IN操作：可以转换为多个OR连接的等值查询，能有效利用索引
  * NOT IN：需要排除集合中的所有值
* 执行过程：
  * 对NOT IN中的每个值，都要执行一次索引查找
  * 然后合并所有这些查找结果
  * 最后返回不在这些结果中的记录
* 性能问题：
  * 当NOT IN列表较大时，需要执行多次索引查找
  * 合并操作成本高
  * 优化器通常认为全表扫描更高效
* 示例：
```
-- 假设order_id有索引
SELECT * FROM orders WHERE order_id NOT IN (1001, 1002, 1003, ..., 2000);
```
即使order_id有索引，处理大量NOT IN值时优化器会选择全表扫描。
### 优化器的成本估算
* MySQL优化器基于统计信息计算不同执行计划的成本：
  * 索引选择性（不同值的比例）
  * 数据分布情况
  * 需要访问的数据量占比
* 当估算出使用索引的成本高于全表扫描时，就会导致"索引失效"。
### 实际解决方案
* 重写查询：
```   
-- 原查询
SELECT * FROM table WHERE col != 'value';

-- 可尝试改为
SELECT * FROM table WHERE col < 'value' OR col > 'value';
```
* 使用索引提示（谨慎使用）：【没看懂】
```
SELECT * FROM table FORCE INDEX(index_name) WHERE col != 'value';
```
* 考虑业务设计：
  * 是否可以避免使用不等于/NOT IN逻辑
  * 能否用其他查询方式替代（如JOIN、EXISTS等）
# 4. 使用LIKE以通配符开头
* 场景：
```
SELECT * FROM users WHERE name LIKE '%林%';  -- 前导通配符
```
* 原因：
  * 这是因为B+树索引是根据索引值有序存储的，仅能支持前缀比较。
  * 举个例子，假设我们查询以「林」为前缀的名称。查询过程如下：
    * a.比较首节点的索引值：若「林」的拼音在某些节点中 介于「陈」和「周」之间，则继续查找下一个节点。
    * b.依此类推，直到找到符合前缀的叶子节点，并读取相应数据。
    * 如果查询条件是LIKE '%林'，因无法确定从哪个索引值开始比较，导致必须进行全表扫描。
* 解决方案：
  * 尽量避免前导通配符
  * 考虑使用全文索引 
  * 如果必须使用，尝试改为：
```
SELECT * FROM users WHERE name LIKE '张%';  -- 可以使用索引
```
# 5. 隐式类型转换
* 场景：
```
sql
-- 假设user_id是字符串类型，但用数字查询
SELECT * FROM users WHERE user_id = 123;  -- 隐式转换
```
* 原因分析
  * MySQL会将其自动转换为数字，相当于执行：
```
SELECT * FROM users WHERE user_id = 123;
等同于:SELECT * FROM users WHERE CAST(user_id AS SIGNED) = 123;
```
* 解决方案：
  * 确保查询条件的类型与列定义类型一致
```
SELECT * FROM users WHERE user_id = '123';
```
# 6. OR条件列没都索引
   场景：
```
-- 假设name有索引，age没有
SELECT * FROM users WHERE name = '张三' OR age = 25;
```
解决方案：
* 对OR条件的所有列都建立索引
* 改用UNION：【？】
```
SELECT * FROM users WHERE name = '张三'
UNION
SELECT * FROM users WHERE age = 25;
```
## 索引合并(Index Merge)
当对OR/AND条件中的所有列都建立索引时，MySQL会采用"索引合并(Index Merge)"优化策略来执行查询。
### 基本执行流程
```
-- 假设name和age都有独立索引
SELECT * FROM users WHERE name = '张三' OR age = 25;
```
1. 分别扫描各个索引：
    * 先通过name索引查找'张三'的记录
    * 同时通过age索引查找age=25的记录
2. 合并结果集：
    * 将两个索引扫描得到的主键ID集合进行并集(union)操作
    * 去除重复的主键ID（同一记录可能同时满足两个条件）
3. 回表获取完整数据：
    * 根据合并后的主键ID集合
    * 回表(聚簇索引)获取完整的行数据
### 索引合并的三种类型
* Index Merge Union：
  * 用于OR条件的合并
  * 示例：WHERE key1 = 1 OR key2 = 2
* Index Merge Intersection：
  * 用于AND条件的合并
  * 示例：WHERE key1 = 1 AND key2 = 2
* Index Merge Sort-Union：
  * 当范围条件无法直接合并时使用
  * 需要对索引扫描结果先排序再合并
### 执行计划示例
使用EXPLAIN分析时，你会看到类似这样的输出：
```
EXPLAIN SELECT * FROM users WHERE name = '张三' OR age = 25;
```
可能的输出：
```
+----+-------------+-------+-------------+----------------------+----------------------+---------+------+------+---------------------------------------+
| id | select_type | table | type        | possible_keys         | key                  | key_len | ref  | rows | Extra                                 |
+----+-------------+-------+-------------+----------------------+----------------------+---------+------+------+---------------------------------------+
|  1 | SIMPLE      | users | index_merge | idx_name,idx_age      | idx_name,idx_age      | 1022,4  | NULL |   50 | Using union(idx_name,idx_age); Using where |
+----+-------------+-------+-------------+----------------------+----------------------+---------+------+------+---------------------------------------+
```
关键字段说明：
* type: index_merge 表示使用了索引合并
* Extra: Using union(...) 显示具体合并了哪些索引
# 7. IS NOT NULL：
* 在大多数情况下 IS NULL 不会导致索引失效
* IS NOT NULL 和 <>（不等于）操作导致索引失效的原因非常相似
# 8. 数据分布不均匀
* 场景：
  * 当某个值出现频率非常高时，优化器可能认为全表扫描更快
* 解决方案：
  * 使用FORCE INDEX强制使用索引
  * 考虑是否需要该索引
# 诊断索引是否失效的方法
* 使用EXPLAIN分析查询执行计划
```
EXPLAIN SELECT * FROM users WHERE name = '张三';
```
* 查看key列是否为NULL，type列是否为ALL（全表扫描）

