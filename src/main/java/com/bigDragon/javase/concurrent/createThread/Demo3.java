package com.bigDragon.javase.concurrent.createThread;

/**
 * 使用同步方法解决实现Runnable接口的线程安全问题
 * 
 * @author: bigDragon
 * @date: 2020年8月11日
 *
 */
public class Demo3 implements Runnable{
	private static int ticket=100;
	private static int flag=1;
	
	@Override
	public void run(){
		while(true){
			if(flag==0){break;}
			show();
		}
	}
	private synchronized void show(){//同步见识器this，与下面写的效果相当
		//共享数据 加锁
		//synchronized(this){
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
				flag=0;
			}
		//}
	}
	public static void main(String[] args){
		//唯一对象
		Demo3 demo3=new Demo3();
		new Thread(demo3).start(); 
		new Thread(demo3).start(); 
		new Thread(demo3).start(); 
	}
}

