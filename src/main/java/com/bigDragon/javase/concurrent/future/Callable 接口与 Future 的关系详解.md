Callable 接口与 Future 的关系详解

Callable 和 Future 是 Java 并发编程中密切相关的两个接口，它们共同构成了 Java 异步任务执行的基础模型。

核心关系
生产者-消费者模式：

Callable 是任务生产者：定义要执行的任务逻辑

Future 是结果消费者：获取异步执行的结果

协作流程：

# 一、基本概念对比
|特性|Callable|Future|
|----|---|----|
|接口类型|函数式接口(java.util.concurrent)|普通接口(java.util.concurrent)
|核心方法|V call() throws Exception|get(), cancel(), isDone()等
|返回值|有返回值(泛型V)|包装Callable的返回值
|异常处理|允许抛出受检异常|捕获并包装Callable的异常
|使用场景|定义需要返回结果的任务|管理异步任务的生命周期和结果
