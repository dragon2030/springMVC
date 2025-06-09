
* 只要其中一个是变量，结果就在堆中（堆中对象在将实际地址指向对应常量池内存）
* 如果拼接的结果调用intern()方法，返回值就在常量池中


# 常量折叠（字面量+字面量）
**常量折叠（Constant Folding）优化**
* 表达式 String s1 = "abc" + "def"; 不会 生成 StringBuilder 临时对象，而是会在 编译期间 直接合并为一个字符串常量 "abcdef"，并放入字符串常量池（String Pool）。
# 对象+对象
> 分步计算
1. 字符串字面量部分
   * "xx" 是字符串字面量，Java 会将其放入字符串常量池（String Pool）。如果常量池中不存在 "xx"，则会创建一个新的 String 对象并放入常量池；如果已存在，则直接引用池中的对象。
   * 假设常量池中没有 "xx"，则 "xx" 会创建 1 个 String 对象（并存入常量池）。
2. new String("xx") 部分 
   * new String("xx") 会在堆内存中创建一个新的 String 对象（即使常量池中有 "xx"，new 也会强制在堆中创建新对象）。
   * 每次调用 new String("xx") 会创建 1 个新的堆对象。
   * 表达式中有 2 个 new String("xx")，所以会创建 2 个堆对象。

3. 字符串拼接 (+) 部分
   * 在 Java 8 中，字符串拼接 (+) 会被编译器优化为 StringBuilder 操作：
```
java
new StringBuilder()
.append(new String("xx"))
.append(new String("xx"))
.toString();
```
StringBuilder 的 toString() 方法会创建一个新的 String 对象（即拼接结果）。

因此，拼接过程会创建：

1 个 StringBuilder 对象（临时用于拼接）。

1 个新的 String 对象（最终结果）。

4. 总结创建的对象
   字符串常量池："xx"（1 个，如果之前不存在）。

堆对象：

new String("xx")（2 个）。

StringBuilder（1 个）。

拼接结果的 String（1 个）。

总对象数：

如果常量池中没有 "xx"：5 个对象（1 常量池 + 2 new String + 1 StringBuilder + 1 结果）。

如果常量池中已有 "xx"：4 个对象（2 new String + 1 StringBuilder + 1 结果）。
# 字面量+对象
以此类推
