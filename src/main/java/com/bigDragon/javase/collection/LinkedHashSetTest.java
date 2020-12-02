package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * LinkedHashSetTest
 * LinkedHashSetTest作为HashSet的子类，在添加数据的同事，每个数据还维护了两个引用，记录此数据前一个数据和后一个数据。
 * 优点：对于频繁的遍历操作，LinkedHashSet效率高于HashSet
 *
 * @author bigDragon
 * @create 2020-11-16 14:37
 */
public class LinkedHashSetTest {
    @Test
    public void test(){
        //LinkedHashSet测试
        Collection collection = new LinkedHashSet();

        collection.add(1);
        collection.add(2);
        collection.add(9);
        collection.add("A");
        collection.add(44);
        collection.add("B");
        collection.add(new Person("Mike",22));
        collection.add(new Person2("Sam",25));

        System.out.println(collection);
        //[1, 2, 9, A, 44, B, Person{name='Mike', age=22}, Person2{name='Sam', age=25}] 按照插入顺序进行遍历

    }
}
