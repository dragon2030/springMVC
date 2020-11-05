/**
 * 
 */
package com.bigDragon.javase.concurrent.createThread;

/**
 * 单例模式的线程安全问题
 * @author: bigDragon
 * @date: 2020年8月11日
 * 
 */
public class Bank {
	
	private Bank(){}
	
	private static Bank instance = null;
	
	public static Bank getInstance(){
		//方式一:效率稍差
/*		synchronized (Bank.class) {
			if(instance == null){
				instance = new Bank();
			}
			return instance;
		}*/
		//方式二：效率更高
		if(instance == null){
			synchronized (Bank.class) {
				if(instance == null){
					instance = new Bank();
				}
			}
		}
		return instance;
	}
}
