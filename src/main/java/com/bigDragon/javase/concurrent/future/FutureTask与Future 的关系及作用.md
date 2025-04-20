# FutureTask 与 Future 的关系及作用
## Future 和 FutureTask 的关系
Future 是接口：Future 是 Java 并发包 (java.util.concurrent) 中的一个接口，表示异步计算的结果。

FutureTask 是实现类：FutureTask 是 Future 接口的一个具体实现类，同时也实现了 Runnable 接口。

继承关系：
public class FutureTask<V> implements RunnableFuture<V>
public interface RunnableFuture<V> extends Runnable, Future<V>

## FutureTask 的作用
### 可取消的异步计算：

提供了一种机制来执行异步任务

可以查询计算是否完成

可以获取计算结果

可以取消任务执行

### 双重身份：

作为 Runnable 可以被线程执行

作为 Future 可以获取计算结果

#### 适配器功能：

可以将 Callable 转换为 Runnable 和 Future

这使得需要 Runnable 的线程执行器 (如 Thread 或 Executor) 能够执行 Callable 任务

```
//用FutureTask包装Callable
FutureTask<Integer> futureTask = new FutureTask<>(()->{return 123;});

// 作为Runnable提交给线程执行
new Thread(futureTask).start();
```
## 主要方法
get()：获取计算结果，如果计算未完成则阻塞

get(long timeout, TimeUnit unit)：带超时的获取结果

cancel(boolean mayInterruptIfRunning)：尝试取消任务

isDone()：判断任务是否完成

isCancelled()：判断任务是否被取消
