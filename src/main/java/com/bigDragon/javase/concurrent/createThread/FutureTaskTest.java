/**
 *
 */
package com.bigDragon.javase.concurrent.createThread;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
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
		//futureTask执行demo
//		futureTaskTest.futureTaskDemo();
		//Callable模拟执行单请求多线程执行，并返回结果集
		futureTaskTest.test1();
		//Runnable执行随机数时间
//		futureTaskTest.test2();
	}
	//1.创建一个实现C拉拉不了的实现类
	class NumThread implements Callable<Integer>{
		//2.实现call方法，将此线程需要执行的操作声明在call()中
		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			int sum = 0;
			for(int i = 0; i <= 100; i++){
				if(i % 2 ==0){
					System.out.println(i);
					sum += i;
				}
			}
			//当不需要返回值时 直接返回null值
			//return null;
//		Thread.sleep(100000);
			return sum;
		}

	}
	//futureTask执行demo
	public void futureTaskDemo() {
		//3.创建Callable接口实现类的对象
		NumThread numThread=new NumThread();
		//4.将此Callable接口实现类对象，传递到FutureTask构造器中，创建FutureTask对象
		FutureTask<Integer> futureTask=new FutureTask<Integer>(numThread);
		//5.将FutureTask的对象作为参数传递到Thread构造器中，创建Thread对象，并用start方法调用
		new Thread(futureTask).start();
		try {
			//6.获取Callable中call方法的返回值
			//get方法的返回值即为FutureTask构造器参数Callable实现类重写的call返回值
			//阻塞到线程执行结束
			boolean done = futureTask.isDone();
			System.out.println("get调用之前，isDone:"+done);
			Integer sum=futureTask.get();
//			Integer sum=futureTask.get(10, TimeUnit.SECONDS);//一般使用线程执行最大时间，防止线程一直阻塞
			System.out.println("总和为："+sum);
			boolean done2 = futureTask.isDone();
			System.out.println("get调用之后，isDone:"+done2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	//FutureTask实现多线程执行任务并
	@Test
	public void test1() {

		ConcurrentHashMap<FutureTask<Integer>, Integer> futureTaskIntegerConcurrentHashMap = new ConcurrentHashMap<>();
		for (int i = 0; i < 10; i++) {
			FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
				Random random = new Random();
				int randomInt = random.nextInt(10);
				Thread.sleep(randomInt * 1000);
				System.out.println("线程内部生成随机数：" + randomInt);
				return randomInt;
			});
			new Thread(futureTask).start();
			boolean done = futureTask.isDone();
			//			System.out.println("get调用之前，isDone:" + done);
			//				Integer random=futureTask.get();
			//				System.out.println("生成随机数："+random);
			//			boolean done2 = futureTask.isDone();
			//			System.out.println("get调用之后，isDone:" + done2);
			futureTaskIntegerConcurrentHashMap.put(futureTask, 1);
		}
		//通过阻塞的方式而不是回调的方式会很难获取值，只能通过线程睡眠的方式 调用get直接是阻塞进程的，不用定时sleep
//		try {
//			while (true) {
//				Set<Integer> integers = new HashSet<>();
//				for (FutureTask<Integer> futureTask : futureTaskIntegerConcurrentHashMap.keySet()) {
//					if (futureTask.isDone()) {
//						integers.add(1);
//					}else{
//						integers.add(0);
//					}
//				}
//				if (integers.contains(0)) {
//					Thread.sleep(1000);
//				}else{
//					break;
//				}
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//所有都执行完
		try {
			if (futureTaskIntegerConcurrentHashMap.keySet().size() >= 10) {
				System.out.println("主线程执行完毕");
				for (FutureTask<Integer> futureTask : futureTaskIntegerConcurrentHashMap.keySet()) {
					System.out.println(futureTask.get(10, TimeUnit.SECONDS) + " " + futureTaskIntegerConcurrentHashMap.get(futureTask));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}


}
