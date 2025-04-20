/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 解决线程安全的问题的方式三：Lock锁 --JDK5.0新增
 * 
 * 1.面试题：synchronized与Lock的异同？
 * 		相同：两者都可以解决线程安全问题
 * 		不同：	synchronized机制在执行完相应的同步代码以后，自动释放监控器
 * 				Lock需要手动启动同步(lock())，同时结束同步也需要手动实现（unlock()）
 * 2.优先使用顺序：
 * 	lock->同步代码块（已经进入方法体，分配了相应资源）->同步方法（在方法体之外）
 * 
 * 3.面试题：如何解决线程安全问题？有几种方式
 * 以上
 * 
 * @author: bigDragon
 * @date: 2020年8月12日
 * 
 */
public class Demo5 implements Runnable{
	private static int ticket=100;
	//当fair为true时 进入的线程按照先后顺序,为false时 进入线程无需抢占cpu执行，默认为false
	//true if this lock should use a fair ordering policy
	private ReentrantLock lock=new ReentrantLock(true);
	
	@Override
	public void run(){
		while(true){
			try{
				//调用锁定的方法lock()
				lock.lock();
				
				if(ticket>0){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(Thread.currentThread().getName()+":卖票，票号为"+ticket);
					ticket--;
				}else{
					break;
				}
				
			}finally {
				//调用解锁方法lock.unlock()
				lock.unlock();
			}
		}
	}
	public static void main(String[] args){
		//唯一对象
		Demo5 demo5=new Demo5();
		new Thread(demo5).start(); 
		new Thread(demo5).start(); 
		new Thread(demo5).start(); 
	}
}
