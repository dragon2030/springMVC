# java中线程的状态
在 Java 8 中，线程状态由 Thread.State 枚举定义，共有 6 种状态。
## NEW (新建)
* 状态描述：线程被创建但尚未启动
* 进入方法：new Thread()
* 示例：
```
Thread t = new Thread(); // 线程处于NEW状态
```
## RUNNABLE (可运行)
* 状态描述：线程正在JVM中执行或等待操作系统资源(如CPU)
* 进入方法： 
  * thread.start() (从NEW状态转换)
  * 从阻塞/等待状态恢复
示例：
```
t.start(); // 进入RUNNABLE状态
```
## BLOCKED (阻塞)
* 状态描述：线程被阻塞等待监视器锁(进入synchronized块/方法)
* 进入方法：
  * 尝试进入被其他线程持有的synchronized块
* 示例：
```
synchronized(lock) {
// 其他线程尝试进入时会进入BLOCKED状态
}
```
## WAITING (无限期等待)
* 状态描述：线程无限期等待其他线程执行特定操作
* 进入方法：
  * object.wait() (需先获得对象锁)
  * thread.join() (不带参数)
  * LockSupport.park()
* 示例：
```
synchronized(obj) {
  obj.wait(); // 进入WAITING状态
}
```
## TIMED_WAITING (限期等待)
* 状态描述：线程在指定时间内等待
* 进入方法：
  * thread.sleep(long millis)
  * object.wait(long timeout)
  * thread.join(long millis)
  * LockSupport.parkNanos()
  * LockSupport.parkUntil()
* 示例：
```
Thread.sleep(1000); // 进入TIMED_WAITING状态
```

## TERMINATED (终止)
* 状态描述：线程执行完毕
* 进入方法：
  * run()方法执行结束
  * 线程抛出未捕获异常
* 示例：
```
// run()方法执行完毕后自动进入TERMINATED状态
```

# 状态转换图
```
NEW → start() → RUNNABLE
RUNNABLE → 获取锁失败 → BLOCKED
RUNNABLE → wait()/join() → WAITING
RUNNABLE → sleep()/wait(timeout) → TIMED_WAITING
WAITING/TIMED_WAITING → notify()/notifyAll()/unpark() → RUNNABLE
BLOCKED → 获取到锁 → RUNNABLE
RUNNABLE → run()结束 → TERMINATED
```
# 重要说明
* RUNNABLE 状态：包含传统意义上的"就绪"(Ready)和"运行中"(Running)两种子状态
* BLOCKED 与 WAITING：
  * BLOCKED：等待获取监视器锁(synchronized)
  * WAITING：等待其他条件(如notify/join完成)
* 中断响应：
  * WAITING/TIMED_WAITING状态的线程可被interrupt()中断
  * 中断后会抛出InterruptedException并进入RUNNABLE状态
* Lock接口：
  * 使用ReentrantLock时，线程等待进入的是WAITING状态而非BLOCKED状态
* 通过Thread.getState()方法可以获取线程当前状态，常用于调试和监控线程行为。
