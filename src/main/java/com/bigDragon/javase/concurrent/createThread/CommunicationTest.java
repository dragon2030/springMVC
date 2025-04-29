/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

/**
 * 线程通讯的例子
 * 
 * 涉及到的三个方法：
 * wait()：方法会释放当前线程持有的 lock 对象的锁【同步监视器】，进入等待状态
 * notify()：方法会唤醒在同一个 lock 对象上等待的一个线程（如果有多个线程在等待，哪一个被唤醒是不确定的）。
 * notifyAll()：方法会唤醒在同一个 lock 对象上等待的全部线程
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
public class CommunicationTest{
	public static void main(String[] args){
		//wait 和 notity 的使用
		new CommunicationTest().wait_notify();
	}
	//wait 和 notity 的使用
	@Test
	public void wait_notify(){
		final Object lock = new Object(); // 创建一个共享的锁对象
		
		// 等待线程
		Runnable waitingRunnable =
				() -> {
					synchronized (lock) {
						System.out.println(Thread.currentThread().getName()+"等待线程：进入同步块，准备等待...");
						try {
							lock.wait(); // 在锁对象上等待
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName()+"线程被唤醒后的状态:"+Thread.currentThread().getState());
					}
				};
		
		// 启动等待线程和唤醒线程
		Thread waitingThread1 = new Thread(waitingRunnable,"waitingThread1");
		Thread waitingThread2 = new Thread(waitingRunnable,"waitingThread2");
		waitingThread1.start();
		waitingThread2.start();
		try {
			Thread.sleep(100); // 等待，确保线程执行 执行到lock.wait()
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("lock.wait()后等待线程1状态："+waitingThread1.getState());
		System.out.println("lock.wait()后等待线程2状态："+waitingThread2.getState());
		
		// 唤醒线程
		Thread notifyingThread = new Thread(() -> {
			synchronized (lock) {
				System.out.println("唤醒线程：进入同步块，准备唤醒等待线程...");
				lock.notify(); // 唤醒在lock对象上等待的一个线程
				// lock.notifyAll(); // 唤醒在lock对象上等待的所有线程
				System.out.println("唤醒线程：唤醒操作完成...");
			}
		});
		notifyingThread.start();
		//waitingThread1等待线程：进入同步块，准备等待...
		//waitingThread2等待线程：进入同步块，准备等待...
		//lock.wait()后等待线程1状态：WAITING
		//lock.wait()后等待线程2状态：WAITING
		//唤醒线程：进入同步块，准备唤醒等待线程...
		//唤醒线程：唤醒操作完成...
		//waitingThread1线程被唤醒后的状态:RUNNABLE
		//【由此可见waitingThread1被唤醒了waitingThread2没有被唤醒】
	}
	@Test
	public void wait_notifyAll(){
		final Object lock = new Object(); // 创建一个共享的锁对象
		
		// 等待线程
		Runnable waitingRunnable =
				() -> {
					synchronized (lock) {
						System.out.println(Thread.currentThread().getName()+"等待线程：进入同步块，准备等待...");
						try {
							lock.wait(); // 在锁对象上等待
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName()+"线程被唤醒后的状态:"+Thread.currentThread().getState());
					}
				};
		
		// 启动等待线程和唤醒线程
		Thread waitingThread1 = new Thread(waitingRunnable,"waitingThread1");
		Thread waitingThread2 = new Thread(waitingRunnable,"waitingThread2");
		waitingThread1.start();
		waitingThread2.start();
		try {
			Thread.sleep(100); // 等待，确保线程执行 执行到lock.wait()
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("lock.wait()后等待线程1状态："+waitingThread1.getState());
		System.out.println("lock.wait()后等待线程2状态："+waitingThread2.getState());
		
		// 唤醒线程
		Thread notifyingThread = new Thread(() -> {
			synchronized (lock) {
				System.out.println("唤醒线程：进入同步块，准备唤醒等待线程...");
				lock.notifyAll(); // 唤醒在lock对象上等待的一个线程
				// lock.notifyAll(); // 唤醒在lock对象上等待的所有线程
				System.out.println("唤醒线程：唤醒操作完成...");
			}
		});
		notifyingThread.start();
		//waitingThread1等待线程：进入同步块，准备等待...
		//waitingThread2等待线程：进入同步块，准备等待...
		//lock.wait()后等待线程1状态：WAITING
		//lock.wait()后等待线程2状态：WAITING
		//唤醒线程：进入同步块，准备唤醒等待线程...
		//唤醒线程：唤醒操作完成...
		//waitingThread2线程被唤醒后的状态:RUNNABLE
		//waitingThread1线程被唤醒后的状态:RUNNABLE
	}
}
