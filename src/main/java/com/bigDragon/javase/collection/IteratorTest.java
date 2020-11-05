package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 集合元素的遍历操作，使用迭代器Iterator接口
 *
 * 1.Iterator对象称为迭代器（设计模式的一种），主要用于遍历Collection集合中的元素。
 * 2.GOF给迭代器模式的定义为：提供一种方法访问一个容器（container）对象中各个元素，而又不需暴露改对象的内部细节。迭代器模式就是为容器二生的。
 * 3.Collection接口继承了java.lang.iterator接口，该接口有一个iterator()方法，那么左右实现了Collection接口的集合有有一个iterator方法，用以返回一个实现iterator接口的对象。
 * 4.Iterator仅用于遍历集合，Iterator本身并不提供承装对象的能力，。如果需要创建Iterator对象，则必须有一个被迭代的集合。
 * 5.集合对象每次调用Iterator()方法都会得到一个全新的迭代器对象，默认游标都在集合的第一个元素之前。
 * @author bigDragon
 * @create 2020-11-02 14:44
 */
public class IteratorTest {
    @Test
    public void test(){
        Collection collection =new ArrayList();
        collection.add("AA");
        collection.add("BB");
        collection.add(new Person("Mike",22));
        collection.add(new Person("Sam",21));
        collection.add("CC");

        Iterator iterator = collection.iterator();

        //遍历迭代器
/*        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }*/

        //迭代器删除
        //迭代器删除错误操作:报错ConcurrentModificationException，当迭代器iterator操作时，对象容器发生变化就会报错。
/*        while(iterator.hasNext()){
            if("BB".equals(iterator.next()))
                collection.remove("BB");
        }
        CollectionTest.print(collection);*/
        //迭代器删除正确操作：迭代器自带的remove方法
        while(iterator.hasNext()){
            if("BB".equals(iterator.next()))
                iterator.remove();
        }
        CollectionTest.print(collection);
    }
}
