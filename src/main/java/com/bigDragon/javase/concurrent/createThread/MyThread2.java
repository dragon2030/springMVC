package com.bigDragon.javase.concurrent.createThread;

/**
 * 创建多线程方式二：实现Runnable接口
 * 1.创建一个实现了Runnable接口的类
 * 2.实现类去实现Runnable中的抽象方法：run()
 * 3.创建实现类的对象
 * 4.将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象
 * 5.通过Thread类的对象调用start()
 * 
 * 比较创建线程的两种方式：
 * 开发中优先选择：实现Runnable接口的方式
 * 原因：	1.实现的方式没有类的单继承性的局限性。
 * 		 	2.实现的方式更适合来处理多个线程有共享数据的情况。
 * 
 * 联系：public class Thread implements Runnable 
 * 相同点：	两种方式都需要重写run()，将现场要执行的逻辑申明在run()中。
			两种方式启动都需要调用start()命令
 * 
 * @author G003759
 *
 */
public class MyThread2 implements Runnable{
	public void run(){
		for(int i=0;i<3;i++){
			//线程命名方式1
			//Thread.currentThread().setName("lalala");
			System.out.println(i+"run()..."+Thread.currentThread().getName());
		}
	}
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		MyThread2 myThread2=new MyThread2();
		Thread trThread=new Thread(myThread2);
		//线程命名方式2
		trThread.setName("继承Runnable接口方式");
		trThread.start();
	}

}
