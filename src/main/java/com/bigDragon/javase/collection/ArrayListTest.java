package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ArrayList
 *
 * 一、ArrayList的源码分析
 *  1.1 jdk 7情况下
 *      ArrayList list = new ArrayList();//底层创建了长度是10的Object[]数组elementData
 *      list.add(123);//elementData[0] = new Integer(123);
 *      ...
 *      list.add(11);//如果此次的添加导致底层elementData数组容量不够，则扩容。
 *      默认情况下，扩容为原来容量的1.5倍，同时需要将原有数组中的数据复制到新的数组中。
 *      建议在开发中使用带参的构造器：ArrayList list= new ArrayList(int capacity)
 *
 *  1.2 jdk 8情况下
 *      ArrayList list = new ArrayList();//底层Object[] elementData初始化为{}，并没有创建长度为10的数组
 *      list.add(123);//第一次调用add()时，底层才创建了长度为10的数组，并将数据123添加到elementData中
 *      ...
 *      后续的添加和扩容操作和jdk 7无异
 *
 *  1.3 小结：jdk7中的ArrayList的对象创建类似于单例的饿汉模式，而jdk8中的ArrayList的对象的创建类似于单例的懒汉模式，延时了数组的创建，
 *  节省内存。
 *
 * @author bigDragon
 * @create 2020-11-05 15:55
 */
public class ArrayListTest {
    public static void main(String[] args) {
        ArrayListTest test = new ArrayListTest();
        //java集合(Collection)中的一种错误机制:fail-fast 机制核心变量modCount
        //多线程修改时的报错场景
//        test.modCountTest1();
        //这边用的是arrayList内部类Itr中的remove方法，会修改modCount也修改expectedModCount
//        test.modCountTest2();
        //这边用的是arrayList中的remove方法，只会修改modCount不修改expectedModCount
        test.modCountTest3();
    
    }
    public void method(){
        List list = new ArrayList();
    }
    
    public void modCountTest1(){
        List<String> strList = new ArrayList<>();
        strList.add("a");
        strList.add("b");
        strList.add("c");
        Iterator<String> iterator = strList.iterator();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> strList.add("d")).start();
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.println(str);
        }
    }
    
    public void modCountTest2(){
        List<String> strList = new ArrayList<>();
        strList.add("AA");
        strList.add("aa");
        strList.add("aa");
        strList.add("CC");
        Iterator<String> iterator = strList.iterator();
        while (iterator.hasNext()) {
            if ("aa".equals(iterator.next())) {
                iterator.remove();
            }
        }
        System.out.println(strList);
    }
    
    public void modCountTest3(){
        List<String> strList = new ArrayList<>();

        strList.add("AA");
        strList.add("aa");
        strList.add("BB");
        strList.add("CC");
        for (String str : strList) {
            if ("aa".equals(str)) {
                strList.remove(str);
            }
        }
        System.out.println(strList);
    }
}
