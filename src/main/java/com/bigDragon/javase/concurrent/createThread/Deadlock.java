/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

/**
 * 演示线程的死锁问题
 * 
 * 1.死锁的理解：不同的线程分别占用对方需要的同步资源不放弃
 * 		都在等待对方放弃自己  需要的同步资源，就形成了线程的死锁
 * 
 * 2.说明：
 * 	1）出现死锁后，不会出现异常，不会出现提示。只是所有线程都处于阻塞状态。
 * 	2）我们使用同步时，要避免出现死锁。
 * 
 * @author: bigDragon
 * @date: 2020年8月12日
 * 
 */
public class Deadlock {
	public static void main(String[] args){
		
		StringBuffer s1=new StringBuffer();
		StringBuffer s2=new StringBuffer();
		
		new Thread(){
			@Override
			public void run(){
				
				synchronized (s1) {
					s1.append("a");
					s2.append("1");
					
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
					
					synchronized (s2) {
						s1.append("b");
						s2.append("2");
					}
				}
			}
		}.start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (s2) {
					s1.append("c");
					s2.append("3");
					
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
					
					synchronized (s1) {
						s1.append("d");
						s2.append("4");
					}
				}
			}
		}).start();
	}
}
