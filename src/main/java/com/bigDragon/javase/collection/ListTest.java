package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * List
 * 一、List接口概述
 *
 * 1.鉴于Java中数组用来存储数据的局限性，我们通常使用List替代数组
 * 2.List集合类中元素有序、且可重复，集合中的每个元素都有其对应的顺序索引。
 * 3.List容器中的元素都有对应一个整数数型的序号记载其在容器中的位置，可以根据需要存取容器的元素。
 * 4.JDK API中List接口的实现常用的有：ArrayList、LinkedList和Vector
 *
 * 二、结构体系：
 *      Collection接口：单列集合，用来存储一个一个的对象
 *          List接口：存储有序的、可重复的数据
 *              ArrayList：作为List接口的主要实现类；线程不安全的，效率高；底层使用Object[] elementData存储
 *              LinkedListTest:对于频繁的插入。删除操作，使用此类效率比ArrayList高；底层使用双向链表存储。
 *              Vector:作为List接口的古老实现类；线程安全，效率低；底层使用Object[] elementData存储
 * 三、List接口
 *  List除了从Collection集合继承的方法外，List集合里添加了一些根据索引来操作集合元素的方法。
 *      void add(int index,Object element):在index位置插入element元素
 *      boolean addAll(int index,Collection elements):从index位置开始将elements中的所有元素添加进来
 *      Object get(int index):获取指定index位置的元素
 *      int indexOf(Object obj):返回obj在计划中首次出现的位置
 *      int lastIndexOf(Object obj):返回obj在当前集合中末此出现的位置
 *      Object remove(int index):移除指定index位置的元素，并返回此元素
 *      Object set(int index,Object element):设置指定index位置的元素为element
 *      List subList(int fromIndex,int toIndex):返回从fromIndex到toIndex位置的子集合(前闭后开)
 *
 * @author bigDragon
 * @create 2020-11-03 16:05
 */
public class ListTest {
    public static void main(String[] args){
        ListTest listTest = new ListTest();

        //ArrayList
        new ArrayListTest();
        //LinkedList
        new LinkedListTest();
        //Vector
        new VectorTest();
        //List的常用方法
        listTest.commonMethod();
    }

    /**
     * List的常用方法
     * void add(int index,Object element)
     * boolean addAll(int index,Collection elements)
     * Object get(int index)
     * int indexOf(Object obj)
     * int lastIndexOf(Object obj)
     * remove(int index)
     * Object set(int index,Object element)
     * List subList(int fromIndex,int toIndex)
     */
    public void commonMethod(){
        List list = new ArrayList();
        list.add(123);
        list.add(456);
        list.add(new Person("Mike",22));
        list.add("AA");
        list.add(789);
        System.out.println(list);
        //1.void add(int index,Object element):在index位置插入element元素
        list.add(1,"BB");
        System.out.println("add:"+list);
        //2.boolean addAll(int index,Collection elements):从index位置开始将elements中的所有元素添加进来
        List list2=Arrays.asList(1,2,3);
        list.addAll(1,list2);
        System.out.println("addAll:"+list);
        //3.Object get(int index):获取指定index位置的元素
        Object object=list.get(2);
        System.out.println("get(int index):"+object);
        //4.int indexOf(Object obj):返回obj在计划中首次出现的位置。当不存在时，返回-1
        int index=list.indexOf(789);
        System.out.println("indexOf:"+index);
        //5.int lastIndexOf(Object obj):返回obj在当前集合中末此出现的位置。当不存在时，返回-1
        int index2=list.lastIndexOf(789);
        System.out.println("lastIndexOf:"+index2);
        //6.Object remove(int index):移除指定index位置的元素，并返回此元素。重载Collection接口中remove(Object obj)方法，方法名相同，参数列表不同
        list.remove(2);
        System.out.println("remove(int index):"+list);
        //7.Object set(int index,Object element):设置指定index位置的元素为element
        list.set(2,"33");
        System.out.println(" set(int index,Object element):"+list);
        //List subList(int fromIndex,int toIndex):返回从fromIndex到toIndex位置(左闭右开)的子集合
        List list3=list.subList(2,5);
        System.out.println("subList(int fromIndex,int toIndex):"+list3);
    }

    //list插入元素到指定位置
    //TODO 其实应该用linkedlist，后面再优化
    @Test
    public void test_20220618(){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        System.out.println(list);
//        list.add(10,2222);
//        System.out.println(list);
        for(int i = 0;i < list.size();i++){
            if(list.get(i)%2 == 1){
                //奇数则添加一行
                list.add(i+1,2222);
                i=i+1;
            }
        }
        System.out.println(list);
    }
}
