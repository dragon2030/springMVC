/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

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
 * 			
 * @author: bigDragon
 * @date: 2020年8月14日
 * 
 */
class NumberThread implements Runnable{


	@Override
	public void run() {
		//int sum = 0;
		for(int i = 0; i <= 100; i++){
			if(i % 2 ==0){
				System.out.println(Thread.currentThread().getName()+": "+i);
				//sum += i;
			}
		}
	}
	
}

class NumberThread1 implements Runnable{


	@Override
	public void run() {
		//int sum = 0;
		for(int i = 0; i <= 100; i++){
			if(i % 2 ==1){
				System.out.println(Thread.currentThread().getName()+": "+i);
				//sum += i;
			}
		}
	}
	
}

class NumberThread2 implements Callable<Integer>{


	@Override
	public Integer call() throws Exception {
		int sum = 0;
		for(int i = 0; i <= 100; i++){
			if(i % 2 ==1){
				System.out.println(Thread.currentThread().getName()+": "+i);
				sum += i;
			}
		}
		return sum;
	}
	
}

public class MyThread4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//1.提供指定线程数量的线程池
		ExecutorService executorService=Executors.newFixedThreadPool(10);
		
		//java反射 获取具体构造类
		System.out.println(executorService.getClass());
		
		//设置线程池的属性
		ThreadPoolExecutor threadPoolExecutor=(ThreadPoolExecutor)executorService;
		threadPoolExecutor.setCorePoolSize(15);
		threadPoolExecutor.setMaximumPoolSize(15);
		
		//2.执行指定的线程操作。需要提供实现Runnable接口或Callable接口实现的对象
		//executorService.execute(new NumberThread());//适合使用于Runnable
		//executorService.execute(new NumberThread1());
		Future<Integer> submit = executorService.submit(new NumberThread2());
		FutureTask<Integer> futureTask= (FutureTask<Integer>) submit;//适合使用与Callable
		try {
			System.out.println(futureTask.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//3.关闭线程池
		executorService.shutdown();
	}

}
