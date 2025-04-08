package com.bigDragon.javase.concurrent.createThread;

import com.bigDragon.javase.concurrent.JUCTest;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * java线程一章节学习
 *
 * 遗留问题：
 * java多线程与cpu启动数量、时间切片
 * java多线程实现原理
 * java同步锁的原理及应用
 *
 * @author: bigDragon
 * @date: 2020年8月17日
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		//机器的CPU核数
		System.out.println(Runtime.getRuntime().availableProcessors());

		//创建多线程方式一：继承与Thread类
		MyThread1.main(args);
		//创建多线程方式二：实现Runnable接口
		MyThread2.main(args);
		//创建多线程方式三：实现Callable接口
		FutureTaskTest.main(args);
		//创建多线程方式四：使用线程池
		MyThread4.main(args);
		//创建多线程方式:简化书写
		SimpleStructure.main(args);

		//synchronized代码块解决继承Thread类的方式的线程安全问题
		Demo1.main(args);
		//synchronized代码块解决实现Runable接口创建多线程的安全问题
		Demo2.main(args);
		//synchronized方法解决实现Runable接口创建多线程的安全问题
		Demo3.main(args);
		//synchronized方法解决继承Thread类的方式的线程安全问题
		Demo4.main(args);
		//lock锁解决线程安全问题
		Demo5.main(args);
		//演示线程的死锁问题
		Deadlock.main(args);
		//单例模式的线程安全问题
		Bank.getInstance();
		//线程通讯的例子，synchronized方式
		CommunicationTest.main(args);

		//spring集成线程池测试
		Class c=ThreadPoolDemo.class;
		//工具类的线程安全解决
		ThreadSafetyCompare.main(args);
		//测试方法
		//join
		JoinMethod.main(new String[]{});
		
		//JUC
		new JUCTest();
	}
}
