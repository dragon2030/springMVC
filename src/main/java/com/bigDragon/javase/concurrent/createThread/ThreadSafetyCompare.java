package com.bigDragon.javase.concurrent.createThread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadSafetyCompare {
	/**
	 * 线程安全解决办法 
	 * 方法1： CollectionsTest.synchronizedList(new LinkedListTest<String>())
	 * 方法2: LinkedList和ArrayList换成线程安全的集合，如CopyOnWriteArrayList，ConcurrentLinkedQueue......
	 * 方法3：Vector(内部主要使用synchronized关键字实现同步)
	 */
	public void ArrayListAndLinkedList(){
		List<String> list1=new LinkedList<String>();
		List<String> list2=new ArrayList<String>();
		list1.add("1");
		list2.add("1");
		List<String> list3=new CopyOnWriteArrayList<String>();
		List<String> list4=new Vector<String>();
		Queue<String> queue=new ConcurrentLinkedQueue<String>();
	}
	
	/**
	 * 比较StringBuffer和StringBuilder的线程安全问题
	 * stringBuffer中append加入synchronized同步锁
	 */
	public void StringBufferAndStringBuilder(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("1");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("1");
	}
	
	public static void main(String[] args){
		ThreadSafetyCompare threadSafetyCompare=new ThreadSafetyCompare();
		threadSafetyCompare.ArrayListAndLinkedList();
	}
}
