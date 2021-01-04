package com.bigDragon.javase.concurrent.createThread;

/**
 * 例子：创建三个窗口卖票，总票数为100张
 * 1.问题：卖票过程中，出现了重票、错票--》出现了线程安全问题
 * 2.问题出现的原因：当某个线程操作车票的过程中，尚未操作完成时，其他线程参与进来，也操作车票。
 * 3.如何解决：当一个线程a在操作ticket的时候，其他线程不能参与进来，直到线程a操作完成ticket时
 * 			线程才可以开始操作ticket。
 * 4.在JAVA中，我们通过同步机制，来解决线程安全问题。
 * 
 * 方式一：同步方法块
 * 	synchronized(同步监视器){
 * 		//需要被同步的代码
 * 	}
 * 	说明：	1.操作共享数据的代码，即为需要被同步的代码.	-->不能包含代码多了，也不能包含代码少了
 * 			2.共享数据；多个线程共同操作的变量，比如样例中的ticket
 * 			3.同步监视器，俗称：锁。任何一个类的对象，都可以充当锁
 * 				要求：多个线程必须要公用同一把锁。
 * 				补充：在实现Runable接口创建多线程的方式中，我们可以考虑使用this充当同步见识器
 * 方式二：同步方法
 * 		如果操作共享数据的代码完整的申明在一个方法中，我们不妨将此方法申明为同步的
 *  	关于同步方法的总结：
 * 		1.同步方法仍然涉及到同步监视器，只是不需要我们显式的声明。
 * 		2.非静态的同步方法，同步监视器是：this
 * 	  	    静态的同步方法，同步监视器是：当期类本身	
 * 
 * 5.同步的方式，解决了线程的安全问题
 * 		操作同步代码是，只能有一个线程参与，其他线程等待。相当于一个线程，效率低	---局限性
 * 
 * @author G003759
 *
 */
public class Demo2 implements Runnable{
	private static int ticket=100;
	Object obj=new Object();
	
	@Override
	public void run(){
		while(true){
			//共享数据 加锁
			synchronized(this){//此时的this：唯一的window1的对象--方法一
			//synchronized(obj){--方法二
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
			}
		}
	}
	public static void main(String[] args){
		//唯一对象
		Demo2 demo2=new Demo2();
		new Thread(demo2).start(); 
		new Thread(demo2).start(); 
		new Thread(demo2).start();
	}
}
