package com.bigDragon.javase.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Set
 *
 * 一、Set接口的框架：
 *  |----Collection接口：单列集合，用来存储一个一个的对象
 *      |----Set接口：存储无序的、不可重复的数据
 *          |----HashSet：作为Set接口的主要实现类，线程不安全的，可以存储null值
 *              |----LinkedHashSet：作为HashSet的子类；遍历其内部数据时，可以按照添加的顺序遍历
 *                  优点：对于频繁的遍历操作，LinkedHashSet效率高于HashSet
 *          |----TreeSet：可以按照添加对象的指定属性，进行排序
 *
 * 二、Set的特性：存储无序的、不可重复的
 *      以HashSet为例说明：
 *      1.无序性：不等于随机性，存储的数据再底层数组中并非按照数组索引的顺序添加。而是根据数据的哈希值决定。
 *      2.不可重复性：保证添加的元素按照equals()判断时，不能返回true。即：相同的元素只能添加一个。
 *
 *  三、添加元素的过程：以HashSet为例（jdk7）
 *      我们向HashSet中添加元素a，首先调用元素a所在类的hashCode()方法，计算元素的哈希值。
 *      此哈希值接着通过算法计算出在HashSet底层数组中的存放位置（即为：索引位置/桶），判断数组此位置是否已经有元素：
 *          如果此位置上没有其他元素，则元素a添加成功   --->情况1
 *          如果此位置上有其他元素b（或以链表形式存在的多个元素），则比较元素a与元素b的hash值：
 *              如果hash值不相同，则元素a添加成功。    --->情况2
 *              如果hash值相同，进而需要调用元素a所在类的equals()方法：
 *                  equals()返回true，元素a添加失败
 *                  equals()返回false，元素a添加成功  --->情况3
 *      说明总结：
 *          对于添加成功的情况2和情况3而言：元素a与已经存在指定索引位置上数据以链表的方式存储
 *          jdk7 ：元素a放到数组中，指向原来的元素
 *          jdk8 ：原来的元素在数组中，指向元素a
 *          （七上八下）
 *          HashSet底层：数组+链表/红黑树的结构
 *   四、Set的说明
 *      1.Set接口中没有额外定义新的方法，使用的都是Collection中声明过的方面。
 *      2.向Set中添加的数据，其所在的类一定要重写hashCode()和equals()
 *      3.Set重写hashCode()和equals()小技巧：对象中用作equals()方法比较的Field，都应该用来计算hashCode。
 *
 *
 * 大神博客
 * Java集合之一—HashMap
 * https://blog.csdn.net/woshimaxiao1/article/details/83661464?ops_request_misc=%257B%2522request%255Fid%2522%253A%25220fb27377e6baa0e6d46fb84ba8fdb753%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=0fb27377e6baa0e6d46fb84ba8fdb753&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~hot_rank-2-83661464-null-null.142^v102^pc_search_result_base4&utm_term=hashmap&spm=1018.2226.3001.4187
 * 
 * @author bigDragon
 * @create 2020-11-09 15:14
 */
public class SetList {
    public static void main(String[] args){
        SetList setList =new SetList();

        //HashSet测试
        Collection hashSet = new HashSet();
        //数据插入
        setList.method1(hashSet);
        //遍历证明Set的无序性
        System.out.println(hashSet);//[1, A, 2, B, 9, 44, Person{name='Mike', age=22}]
        //遍历证明Set的不可重复性
        hashSet.add(2);
        System.out.println(hashSet);//[1, A, 2, B, 9, 44, Person{name='Mike', age=22}]  无变化，添加数据不增加元素
        hashSet.add(new Person("Mike",22));//[1, A, 2, B, Person2{name='Sam', age=25}, 9, 44, Person{name='Mike', age=22}]  未重写重写equals()、hashCode()
        System.out.println(hashSet);
        hashSet.add(new Person2("Sam",25));//[1, A, 2, B, Person2{name='Sam', age=25}, Person2{name='Sam', age=25}, 9, 44, Person{name='Mike', age=22}] 重写equals()、hashCode()
        System.out.println(hashSet);

        //LinkedHashSet测试
        new LinkedHashSetTest();
        //TreeSet测试
        new TreeSetTest();

    }

    /**
     * 插入数据
     */
    public void method1(Collection collection){
        collection.add(1);
        collection.add(2);
        collection.add(9);
        collection.add("A");
        collection.add(44);
        collection.add("B");
        collection.add(new Person("Mike",22));
        collection.add(new Person2("Sam",25));
    }

    /**
     * 迭代遍历元素
     * @param collection
     */
    public void method2(Collection collection){
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
