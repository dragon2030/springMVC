package com.bigDragon.javase.concurrent.createThread;

import com.bigDragon.javase.collection.map.CollectionsTest;

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
	 * 方法1：Vector(内部主要使用synchronized关键字实现同步)
	 * 方法2： Collections.synchronizedList(new LinkedListTest<String>())
	 * 方法3: LinkedList和ArrayList换成线程安全的集合，如CopyOnWriteArrayList，ConcurrentLinkedQueue......
	 */
	public void ArrayListAndLinkedList(){
		List<String> list1=new LinkedList<String>();
		List<String> list2=new ArrayList<String>();
		list1.add("1");
		list2.add("1");
		List<String> list4=new Vector<String>();
		Queue<String> queue=new ConcurrentLinkedQueue<String>();
		List<String> list3=new CopyOnWriteArrayList<String>();
		list3.add(new String());
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
