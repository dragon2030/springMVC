**原子类(atomic)**
# 简介
原子类属于JUC。

原子类由CAS操作保证原子性，由volatile关键字保证可见性。

原子类自jdk 1.5开始出现，位于java.util.concurrent.atomic包下面。
# 原子类类型
## 基本类型
AtomicBoolean
AtomicInteger
AtomicLong
## 引用类型
AtomicReference
AtomicStampedReference
AtomicReferenceFieldUpdater
AtomicMarkableReference
## 数组类型
AtomicIntegerArray
AtomicLongArray
AtomicReferenceArray
## 字段类型
AtomicIntegerFieldUpdater
AtomicLongFieldUpdater
AtomicStampedFieldUpdater
## JDK8新增原子类简介
DoubleAccumulator
LongAccumulator
DoubleAdder
LongAdder
# 博客
* https://way2j.com/a/551
> 面试博客
