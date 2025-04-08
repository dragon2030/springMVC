package com.bigDragon.javase.collection;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Vector:
 * 古老实现类
 * 线程按照 通过synchronized 效率低
 * 现已经基本不用，遇到线程安全的问题时一般选用Collections.synchronizedList(List<T> list) 或JUC
 *
 * Vector的源码分析：
 *      1.jdk7和jdk8中通过Vector()构造器创建对象时，底层都创建了长度为10的数组
 *      2.在扩容方面，默认扩容为原理的数组长度的2倍
 *  public synchronized boolean add(E e) {
 *  ...
 *
 * @author bigDragon
 * @create 2020-11-05 16:45
 */
public class VectorTest {
    public void test(){
        List list = new Vector();
        list.add(new Object());
    }
}
