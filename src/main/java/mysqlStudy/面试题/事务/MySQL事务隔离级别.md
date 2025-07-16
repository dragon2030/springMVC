* MySQL 支持四种标准的事务隔离级别，它们控制着事务之间的可见性和影响程度，是数据库并发控制的核心机制。下面我将全面介绍这四种隔离级别及其特性。
# 一、四种隔离级别概述
|隔离级别|脏读|不可重复读|幻读|实现机制|
|----|---|---|---|---|
|READ UNCOMMITTED (读未提交)|可能|可能|可能|无锁|
|READ COMMITTED (读已提交)|不可能|可能|可能|行锁|
|REPEATABLE READ (可重复读)|不可能|不可能|可能(InnoDB不可能)|MVCC+间隙锁|
|SERIALIZABLE (串行化)|不可能|不可能|不可能|表锁|
# 二、各隔离级别详解
## 1. READ UNCOMMITTED (读未提交)
* 特点：事务可以读取其他事务未提交的修改（脏读）
* 问题：数据一致性最差，可能读到"脏数据"
* 使用场景：几乎不用，仅用于性能测试等特殊场景
```
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
```
## 2. READ COMMITTED (读已提交)
* 特点：只能读取已提交的数据，但同一事务内两次读取可能结果不同（不可重复读）
* 使用场景：Oracle等数据库的默认级别
```
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
```
## 3. REPEATABLE READ (可重复读)
* 特点：MySQL默认级别，同一事务内多次读取结果一致
* 特殊实现：InnoDB通过MVCC+间隙锁(Gap Lock)防止幻读
  【这个还没懂？？】
* 优势：平衡了并发性能和数据一致性
```
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
```
## 4. SERIALIZABLE (串行化)
* 特点：最高隔离级别，完全串行执行
* 使用场景：需要绝对数据一致性，不计性能代价
```
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
```
# 三、并发问题说明
1. 脏读(Dirty Read)：读取到其他事务未提交的数据
2. 不可重复读(Non-repeatable Read)：同一事务内两次读取同一数据结果不同
3. 幻读(Phantom Read)：同一事务内两次查询返回不同的行集合
【mvcc学习后再来深刻体会幻读？？】
# 五、如何选择和设置隔离级别

```
查看当前隔离级别
SELECT @@transaction_isolation;
全局设置（需重启生效）：
SET GLOBAL transaction_isolation = 'REPEATABLE-READ';
会话级别设置：
SET SESSION transaction_isolation = 'READ-COMMITTED';
```
# 七、性能影响对比
|隔离级别|并发性能|锁开销|适用场景|
|----|---|---|----|
|READ UNCOMMITTED|⭐⭐⭐⭐⭐|最小|几乎不用|
|READ COMMITTED|⭐⭐⭐⭐|低|高并发读|
|REPEATABLE READ|⭐⭐⭐|中|平衡场景(默认)|
|SERIALIZABLE|⭐|高|强一致性需求|
【在印象笔记中清楚记录了mysql没有规避幻读 为什么mvcc这个学完好好看一下】
