package com.bigDragon.javase.concurrent.createThread;

/**
 * 简化书写 
 * @author G003759
 *
 */
public class SimpleStructure {
	
	//局部内部类：类定义在方法中
	public void SimpleFunction1(){
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
		class RunnableTest implements  Runnable{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++){
					System.out.println(i+"方法执行，线程名："+Thread.currentThread().getName());
				}
			}
		}
		new Thread(new RunnableTest()).start();
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
	}
	
	//匿名内部类：没有子类的名称，必须借助接口或父类
	public void SimpleFunction2(){
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++){
					System.out.println(i+"方法执行，线程名："+Thread.currentThread().getName());
				}
			}
		}).start();
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
	}
	public void SimpleFunction3(){
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<10;i++){
					System.out.println(i+"方法执行，线程名："+Thread.currentThread().getName());
				}
			}
		}.start();
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
	}
	
	//Lambda简化:简化简单的线程类，Lambda表达式，去掉接口和方法名
	public void SimpleFunction4(){
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
		new Thread(()->{
			for(int i=0;i<10;i++){
				System.out.println(i+"方法执行，线程名："+Thread.currentThread().getName());
			}
		}).start();
		System.out.println("简化书写之前执行，线程名："+Thread.currentThread().getName());
	}
	
	public static void main(String[] args){
		SimpleStructure simpleStructure=new SimpleStructure();
		//simpleStructure.SimpleFunction1();
		//simpleStructure.SimpleFunction2();
		simpleStructure.SimpleFunction3();
	}
}
