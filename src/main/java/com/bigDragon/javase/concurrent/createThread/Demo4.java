package com.bigDragon.javase.concurrent.createThread;

/**
 * 使用同步方法解决实现继承Thread类方式中的线程安全问题
 * @author G003759
 *
 */
public class Demo4 extends Thread{
	
	private static int ticket=100;
	private static int flag=1;
	@Override
	public void run(){
		while(true){
			if(flag==0){break;}
			show();
		}
	}
	private static synchronized void show(){//添加了static 同步监视器：当前的类
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
/*		new Demo1().start(); 
		new Demo1().start(); 
		new Demo1().start();*/ 
		Demo1 demo1=new Demo1();
		Demo1 demo2=new Demo1();
		Demo1 demo3=new Demo1();
		
		demo1.start();
		demo2.start();
		demo3.start();

	}

}
