/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

/**
 * 线程通讯的例子，synchronized方式
 * 
 * 涉及到的三个方法：
 * wait()：一旦执行此方法，当前线程就进入阻塞状态，并释放同步监视器
 * notify()：一旦执行此方法，就会唤醒为wait的一个线程，如果有多个方法就唤醒优先级高的
 * notifyAll()：一旦执行此方法，就会唤醒所以wait的线程
 * 
 * 说明：
 * 	1.wait()，notify()，notifyAll()三个方法必须使用在同步代码块或同步同步方法中
 * 	2.wait()，notify()，notifyAll()三个方法的调用者必须是同步代码块或同步方法中的同步监视器
 * 		否则会出现 IllegalMonitorStateException异常
 * 	3.wait()，notify()，notifyAll()都是定义在Object类中的
 * 
 * 面试题：sleep()和wait()的异同？
 * 	1.相同点：一旦执行方法，都可以使得当前的线程进入阻塞状态
 * 	2.不同点：	1）两个的申明的位置不同：Thread类中申明sleep，Object申明wait
 * 				2）调用要求不同：sleep可以在任何需要的场景下调用，wait（）必须使用同步代码块或同步方法中
 * 				3)关于是否释放同步监视器：如果两个方法都使用在同步代码块或同步方法中，sleep方法不会释放锁，
 * 					wait会释放锁。
 * 
 * @author: bigDragon
 * @date: 2020年8月13日
 * 
 */
public class CommunicationTest implements Runnable{
	private static int ticket=100;
	Object obj=new Object();
	
	@Override
	public void run(){
		while(true){
			//共享数据 加锁
			synchronized(this){
				this.notify();
				//notifyAll();
				
				if(ticket>0){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(Thread.currentThread().getName()+":卖票，票号为"+ticket);
					ticket--;
					
					try {
						//调用wait() 方法的线程进入阻塞状态
						this.wait();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					break;
				}
			}
		}
	}
	public static void main(String[] args){
		//唯一对象
		CommunicationTest communicationTest=new CommunicationTest();
		new Thread(communicationTest).start(); 
		new Thread(communicationTest).start(); 
	}
}
