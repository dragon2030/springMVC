/**
 *
 */
package com.bigDragon.javase.concurrent.threadPoolExecutor;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 创建线程的方式四：使用线程池
 *
 * 背景：经常创建和销毁，使用量特别大的资源，比如并发情况的线程，对性能影响很大。
 * 思路：提前创建好多个线程，放入线程池中，使用时直接获取，使用完放回池中。可以避免频繁
 * 			创建销毁、实现重复利用。类似生活中的公共交通工具
 * 好处：
 * 		1.提高响应速度（减少了创建新线程的时间）
 * 		2.降低资源消耗（重复利用线程池中线程，不需要每次都创建）
 * 		3.便于线程管理
 * 			corePoolSize:核心线程数
 * 			maximumPoolSize:最大线程数
 * 			keepAliceTime：线程没有任务时最多保存多少时间后会终止
 * 			...
 * Java中常用的四种线程池
 * newFixedThreadPool
 * newCachedThreadPool
 * newSingleThreadExecutor
 * newScheduledThreadPool
 * https://blog.csdn.net/java_0000/article/details/125432514?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522f649138996c80c3f2d4556f0ae3f8cb3%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=f649138996c80c3f2d4556f0ae3f8cb3&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-1-125432514-null-null.142
 * 
 *
 * @author: bigDragon
 * @date: 2020年8月14日
 *
 */
public class MyThread4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//主要线程池类型
		ExecutorService executorService=Executors.newFixedThreadPool(10);
		ExecutorService executorService2=Executors.newSingleThreadExecutor();
		ExecutorService executorService3=Executors.newScheduledThreadPool(10);
		ExecutorService executorService4=Executors.newCachedThreadPool();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.execute(()-> System.out.println(1));
		
		new MyThread4().fixedThreadPool();
		new MyThread4().scheduledThreadPool();
		//执行 Runnable Callable 运行线程
		new MyThread4().executeAndSubmit();
		//ThreadPoolExecutor类用提供对线程异常进行处理的方法。afterExecute和Thread.UncaughtExceptionHandler（未举例）
		new MyThread4().exception();
		//源码解析 并没完全看懂 大致看线程池执行流程和核心参数线程池执行的影响
		//源码解析1-execute(Runnable).md
		//源码解析2-runWorker.md

	}
	
	//FixedThreadPool - 固定大小线程池
	@Test
	public void fixedThreadPool() throws Exception{
		//1.提供指定线程数量的线程池
		ExecutorService executorService=Executors.newFixedThreadPool(10);
		
		//2.执行指定的线程操作。需要提供实现Runnable接口或Callable接口实现的对象
		Future<Integer> submit = executorService.submit(()-> 1);//适合使用于Callable
		
		FutureTask<Integer> futureTask= (FutureTask<Integer>) submit;//适合使用与Callable
		System.out.println(submit.get());
		//3.关闭线程池
		executorService.shutdown();
		
		//FixedThreadPool(置线程池的属性)
		ThreadPoolExecutor threadPoolExecutor=(ThreadPoolExecutor)executorService;
		threadPoolExecutor.setCorePoolSize(15);
		threadPoolExecutor.setMaximumPoolSize(15);
		threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
	}

	public void scheduledThreadPool() throws Exception{
		ExecutorService executorService3=Executors.newScheduledThreadPool(10);
		//ScheduledThreadPool
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor)executorService3;
		scheduledThreadPoolExecutor.scheduleAtFixedRate(()->{},0,1,TimeUnit.SECONDS);
	}
	
	@Test
	public void executeAndSubmit(){
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Runnable runnable=()->
			System.out.println("Runnable实现类执行。"+Thread.currentThread().getName());
		executorService.execute(runnable);
		Callable callable=()-> {
			System.out.println("Callable实现类执行。"+Thread.currentThread().getName());
			return null;
		};
		executorService.submit(callable);
		executorService.shutdown();
		/**
		 Runnable实现类执行。pool-1-thread-1
		 Callable实现类执行。pool-1-thread-2
		 */
	}
	
	//ThreadPoolExecutor类用提供对线程异常进行处理的方法。afterExecute和Thread.UncaughtExceptionHandler（未举例）
	//https://way2j.com/a/537
	@Test
	public void exception(){
		System.out.println("主线程开始");
		ThreadPoolExecutor threadPoolExecutor=
				new ThreadPoolExecutor(5,50, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20)){
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				if (r instanceof Thread) {
					if (t != null) {
						System.out.println("捕获到Thread异常。异常信息为：" + t.getMessage());
						System.out.println("异常栈信息为：");
						t.printStackTrace();
					}
				} else if (r instanceof FutureTask) {
					FutureTask futureTask = (FutureTask) r;
					try {
						futureTask.get();
					} catch (InterruptedException e) {
						System.out.println("捕获到InterruptedException异常。异常信息为：" + e.getMessage());
						System.out.println("异常栈信息为：");
						e.printStackTrace();
					} catch (ExecutionException e) {
						System.out.println("捕获到ExecutionException异常。异常信息为：" + e.getMessage());
						System.out.println("异常栈信息为：");
						e.printStackTrace();
					}
				}
			}
		};
		Runnable myRunnable = ()->{
			System.out.println("运行开始");
			throw new RuntimeException("自定义运行时异常");
		};
		threadPoolExecutor.execute(myRunnable);
		Callable myCallable = ()->{
			System.out.println("运行开始");
			int i = 1 / 0;
			return null;
		};
		threadPoolExecutor.submit(myCallable);
		threadPoolExecutor.shutdown();
		
		System.out.println("主线程结束");
		
	}
}
