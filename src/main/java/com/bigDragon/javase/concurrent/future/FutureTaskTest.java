/**
 *
 */
package com.bigDragon.javase.concurrent.future;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 创建线程的方式三：实现Callable接口。 ---JDK5.0新增
 *
 * 如何理解实现Callable接口创建多线程比Runnable接口创建多线程强大
 * 1.call()方法可以有返回结果
 * 2.call()可以抛出异常，被外面的操作捕获，获取异常信息
 * 3.Callable是支持泛型的
 *
 *
 * @author: bigDragon
 * @date: 2020年8月13日
 *
 */

public class FutureTaskTest {
	public static void main(String[] args) {
		FutureTaskTest futureTaskTest = new FutureTaskTest();
		new FutureTaskTest().FutureDemo();
		//futureTask执行demo
		new FutureTaskTest().FutureTask();
		//线程池使用(标准Future使用方式，需要配合线程池,CompletableFuture内置了默认线程池ForkJoinPool不强制)
		new FutureTaskTest().Future();

	}
	@Test
	public void FutureDemo() {
		Future<Integer> future=new FutureTask<>(() -> 1);
		new Thread((FutureTask)future).start();
	}
	//futureTask执行demo
	@Test
	public void FutureTask() {
		//将此Callable接口实现类对象，传递到FutureTask构造器中，创建FutureTask对象
		// 1. 定义Callable任务
		Callable<Integer> callable = () -> {
			Thread.sleep(1000);
			return 1;
		};
		
		//public FutureTask(Callable<V> callable)
		Future<Integer> future=new FutureTask<>(callable);
		FutureTask<Integer> futureTask = (FutureTask<Integer>)future;
		//5.将FutureTask的对象作为参数传递到Thread构造器中，创建Thread对象，并用start方法调用
//		futureTask.run(); // 同步执行，当前线程会阻塞直到任务完成，所以第一次isDone()true
		new Thread(futureTask).start();// 异步执行，所以第一次isDone()false
		try {
			//6.获取Callable中call方法的返回值
			//get方法的返回值即为FutureTask构造器参数Callable实现类重写的call返回值
			//阻塞到线程执行结束
			boolean done = futureTask.isDone();
			System.out.println("get调用之前，isDone:"+done);
			Integer sum=futureTask.get();//线程会进行阻塞
//			Integer sum=futureTask.get(10, TimeUnit.SECONDS);//设置10秒超时 一般使用线程执行最大时间，防止线程一直阻塞
			System.out.println("总和为："+sum);
			boolean done2 = futureTask.isDone();
			System.out.println("get调用之后，isDone:"+done2);
			/**
			 public boolean cancel(boolean mayInterruptIfRunning) {
			 mayInterruptIfRunning 参数
			 true：表示如果任务已经在运行，尝试中断执行任务的线程
			 false：表示不允许中断正在执行的任务，只会取消尚未开始的任
			 返回值
			 true：如果任务被成功取消
			 false：如果任务已经完成、已经被取消或由于其他原因无法取消
			 */
			futureTask.cancel(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	//FutureTask实现多线程执行任务并
	@Test
	public void Future() {
		ExecutorService executor = Executors.newFixedThreadPool(3);

		// 1. 定义Callable任务
		Callable<String> task = () -> {
			Thread.sleep(1000);
			return "任务结果";
		};

		// 2. 提交任务获取Future
		Future<String> future = executor.submit(task);
		//java.util.concurrent.FutureTask@4ae82894
		// 3. 通过Future获取结果
		try {
			String result = future.get();  // 获取Callable.call()的返回值
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}


}
