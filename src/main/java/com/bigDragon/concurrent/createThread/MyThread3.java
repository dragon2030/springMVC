/**
 * 
 */
package com.bigDragon.concurrent.createThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
		return sum;
	}
	 
}
public class MyThread3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//3.创建Callable接口实现类的对象
		NumThread numThread=new NumThread();
		//4.将此Callable接口实现类对象，传递到FutureTask构造器中，创建FutureTask对象
		FutureTask<Integer> futureTask=new FutureTask<Integer>(numThread);
		//5.将FutureTask的对象作为参数床底到Thread构造器中，创建Thread对象，并用start方法调用
		new Thread(futureTask).start();
		try {
			//6.获取Callable中call方法的返回值
			//get方法的返回值即为FutureTask构造器参数Callable实现类重写的call返回值
			Integer sum=futureTask.get();
			System.out.println("总和为："+sum);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
