package com.bigDragon.javase.collection;

import com.bigDragon.javase.collection.map.CollectionsTest;
import com.bigDragon.javase.collection.map.MapMain;
import com.bigDragon.javase.collection.map.TreeMapTest;

/**
 * 一、集合框架的概述
 *
 * 1.集合、数组都是对多个数据进行存储操作的结构，简称Java容器
 *
 * 2.1数组在存储多个数据方面的特点：
 *      一旦初始化以后，其长度就确定了。
 *      数组一旦定义好，其元素类型也就确定了。我们只能操作指定类型的数据
 * 2.2数组在存储多个数据方面的缺点
 *      一旦初始化以后，其长度就不可修改
 *      数组中提供的方法非常有限，对于添加、删除、插入数据等操作，非常不方便，同时效率不高
 *      获取数组中实际元素的个数的需求，数组没有现成的属性或方法可用
 *      数组存储数据的特点：有序、可复制。对于无序、不可重复的需求，不能满足。
 * 二、集合框架
 *      Collection接口：单列集合，用来存储一个一个的对象
 *          List接口：存储有序的、可重复的数据
 *              ArrayList/LinkedListTest/Vector
 *          Set接口：存储无序的、不可重复的数据
 *              HashSet/LinkedHashSet/TreeSet
 *      Map接口：双列集合，用来存储一个（key-value）一对的数据
 *          HashMap/LinkedHashMap/TreeMap/Hashtable/Properties
 *
 * @author bigDragon
 * @create 2020-10-29 14:43
 */
public class Main {
    public static void main(String[] args){
        //Collection接口
        new CollectionTest();
        //collection的contains测试的实体类,重写equals(),toString(),hashCode()方法
        new Person();
        //Iterator接口
        new IteratorTest();
        //jdk5.0新增了foreach循环，用于遍历集合、数组
        new ForEachTest();
        //List接口概述
        new ListTest();
        //ArrayList
        new ArrayListTest();
        //LinkedList
        new LinkedListTest();
        //Vector
        new VectorTest();
        //SetList
        new SetList();
        //Collections:操作Collection和 Map的工具类
        new CollectionsTest();
        //HashMap 和 LinkedHashMap
        new MapMain();
        //TreeMap 自然排序 定制排序
        new TreeMapTest();
    }
}
