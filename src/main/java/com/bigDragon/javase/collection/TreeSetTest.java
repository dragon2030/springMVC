package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * TreeSet
 *
 * 1、TreeSet添加的数据，要求是相同类的对象。
 * 2、两种排序方式：自然排序（实现Comparable接口） 和 定制排序（Comparator）
 * 3.自然排序中，比较两个对象是否相投的标准为：compareTo()返回0，不再是equals()
 * 4.定制排序中，比较两个对象是否相投的标准为：compare()返回0，不再是equals()
 *      TreeMap特点：采用红黑树的存储结构,有序，查询速度比List快
 *
 * 整理一下java两种给集合排序的方式
 *
 * @author bigDragon
 * @create 2020-11-16 15:04
 */
public class TreeSetTest {
    public static void main(String[] args){
        TreeSetTest treeSetTest = new TreeSetTest();
        //自然排序（实现Comparable接口）
        treeSetTest.test();
        //定制排序（Comparator）
        treeSetTest.test2();

    }

    /**
     * 自然排序（实现Comparable接口）
     */
    @Test
    public void test(){
        //LinkedHashSet测试
        Collection collection = new TreeSet();

        //失败：不能添加不同类的对象
/*        collection.add(1);
        collection.add("A");
        collection.add(44);
        collection.add("B");
        collection.add(new Person("Mike",22));
        collection.add(new Person2("Sam",25));*/

        //int类型排序
/*        collection.add(11);
        collection.add(41);
        collection.add(167);
        collection.add(12);
        collection.add(177);*/
        //[1, 2, 9, A, 44, B, Person{name='Mike', age=22}, Person2{name='Sam', age=25}] 按照插入顺序进行遍历

        //String类型排序
/*        collection.add("11");
        collection.add("41");
        collection.add("167");
        collection.add("12");
        collection.add("177");*/
        //[11, 12, 167, 177, 41]

        collection.add(new Person3("John",22));
        collection.add(new Person3("Sam",25));
        collection.add(new Person3("Mike",15));
        collection.add(new Person3("Tom",75));
        collection.add(new Person3("Bob",34));
        collection.add(new Person3("Bob",43));

       //[Person3{name='Tom', age=75},
        // Person3{name='Sam', age=25},
        // Person3{name='Mike', age=15},
        // Person3{name='John', age=22},
        // Person3{name='Bob', age=34},
        // Person3{name='Bob', age=43}]

        System.out.println(collection);

    }

    /**
     * 定制排序（Comparator）
     */
    public void test2() {
        Comparator comparator = new Comparator<Person>(){
            /**
             * 泛型之前的写法
             * 按照年龄从小到大排序
             * @param o1
             * @param o2
             * @return
             */
/*            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Person && o2 instanceof  Person){
                    Person person1 = (Person)o1;
                    Person person2 = (Person)o2;
                    return Integer.compare(person1.getAge(),person2.getAge());
                }else {
                    throw new RuntimeException("输入的数据类型不匹配");
                }
            }*/

            /**
             * 泛型之后的写法
             * @param person1
             * @param person2
             * @return
             */
            @Override
            public int compare(Person person1, Person person2) {
                return Integer.compare(person1.getAge(),person2.getAge());
            }
        };
        Collection collection = new TreeSet(comparator);
        collection.add(new Person("John",22));
        collection.add(new Person("Sam",25));
        collection.add(new Person("Mike",15));
        collection.add(new Person("Tom",75));
        collection.add(new Person("Bob",34));
        collection.add(new Person("Bob",43));
        System.out.println(collection);
        //[Person{name='Mike', age=15},
        // Person{name='John', age=22},
        // Person{name='Sam', age=25},
        // Person{name='Bob', age=34},
        // Person{name='Bob', age=43},
        // Person{name='Tom', age=75}]

    }
}
