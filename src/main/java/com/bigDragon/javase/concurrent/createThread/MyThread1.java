package com.bigDragon.javase.concurrent.createThread;

/**
 * 多线程的创建，方式一：继承与Thread类
 * 1.创建一个继承与Thread类的子类
 * 2.重写Thread类的run(),将此线程执行的操作申明在run()中
 * 3.创建Thread类的子类对象
 * 4.通过此对象调用start()
 * 
 * 测试Thread中的常用方法：
 * 1.start():启动当前线程；调用当前线程的run()
 * 2.run():通常需要重写Thread类中的方法，将创建的线程要执行的操作申明在此方法中
 * 3.currentThread():静态方法，返回要执行的当前代码的线程
 * 4.getName():获取当前线程名称
 * 5.setName():设置当前线程名称
 * 6.yield():释放当前cpu的执行权
 * 7.join():在线程a中调用join(),此时线程a就进入了阻塞状态，直到线程b完全执行完后，线程a才结束阻塞状态
 * 8.stop():已过时。当执行此方法时，强行结束当前线程。
 * 9.sleep(long millitime):让当前线程“睡眠”指定的millitime。在指定的millitime毫秒时间内，当前线程是阻塞状态。
 * 10.isAlive():判断当前线程是否存活
 * 
 * 线程共享方式理解
 * 每个线程拥有自己独立的：栈、程序计数器
 * 多个线程，共享同一个进程的结构：方法区、堆
 * 
 * 线程的优先级：
 * 1.线程的优先级等级
 * MAX_PRIORITY:10
 * MIN_PRIORITY:1
 * NORM_PRIORITY:5
 * 2.涉及的方法
 * getPriority():返回线程的优先级
 * setPriority(int newPriority):改变线程的优先级
 * 说明：
 * 线程创建时继承父线程的优先级
 * 低优先级只是获得调度的概率低，并非一定是高优先级线程之后才被调用
 * 
 * 线程的分类：
 * 一种是守护线程，一种是用户线程
 * 
 * 线程的生命周期：
 * 新建、就绪、运行、阻塞、死亡
 * 
 * 线程的三大特性：
 * 原子性
 * 可见性
 * 有序性
 * 
 * @author G003759
 *
 */
public class MyThread1 extends Thread {
	private int i;
	
	public static void main(String[] args) {
		for(int j = 0;j < 5;j++) {
			
			//调用Thread类的currentThread()方法获取当前线程
			MyThread1 myThread=new MyThread1();
			myThread.setName("A");
			myThread.setPriority(10);
			//getName和setName为获取、设置线程名称
			myThread.setName("第"+j+"次执行的线程,当前线程优先度"+myThread.getPriority());
			myThread.start();

		}
		for(int j = 0;j < 5;j++) {
			
			//调用Thread类的currentThread()方法获取当前线程
			MyThread1 myThread=new MyThread1();
			myThread.setName("B");
			myThread.setPriority(1);
			//getName和setName为获取、设置线程名称
			myThread.setName("第"+j+"次执行的线程,当前线程优先度"+myThread.getPriority());
			myThread.start();

		}
	}
 
	public void run(){
		for(int i=0;i < 5;i++) {
			System.out.println(this.getName() + " "  + i+"start");
			//当通过继承Thread类的方式实现多线程时，可以直接使用this获取当前执行的线程
/*			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			System.out.println(this.getName() + " "  + i+"end");
		}
	}
	}