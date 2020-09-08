package com.bigDragon.concurrent.createThread;

/**
 * 例子：创建三个窗口卖票，总票数为100张，使用基础Thread类的方式
 * 使用同步代码块的解决继承Thread类的方式的线程安全问题
 * 
 * 说明：在继承Thread类创建线程的方式中，慎用this充当同步监视器，可以用当前类充当同步监视器
 * @author G003759
 *
 */
public class Demo1 extends Thread{
	
	private static int ticket=100;
	private static Object obj=new Object();
	@Override
	public void run(){
		while(true){
			//共享数据
			//synchronized(this){//--方法一多个对象不可行 this代表三个不同的对象
			synchronized(obj){//--方法二 正确的方式
			//synchronized(Demo1.class){//--方法三 是唯一的 类只会加载一次
				if(ticket>0){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println(getName()+":卖票，票号为"+ticket);
					ticket--;
				}else{
					break;
				}
			}
		}
	}
	
	public static void main(String[] args){
		new Demo1().start(); 
		new Demo1().start(); 
		new Demo1().start(); 
	}
}
