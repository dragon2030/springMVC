package com.bigDragon.javase.collection.map;

import com.bigDragon.javase.collection.Person;
import com.bigDragon.javase.collection.Person3;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * TreeMap
 *
 * 向TreeMap中添加key-value.要求key必须是同一个类创建的对象
 * 按照key进行排序：自然排序、定制排序
 *
 * @author bigDragon
 * @create 2020-11-19 13:22
 */
public class TreeMapTest {
    @Test
    //自然排序
    public void test(){
        TreeMap treeMap = new TreeMap();
        Person3 p1 = new Person3("John",22);
        Person3 p2 = new Person3("Sam",25);
        Person3 p3 = new Person3("Mike",15);
        Person3 p4 = new Person3("Tom",75);
        Person3 p5 = new Person3("Bob",34);
        Person3 p6 = new Person3("Bob",43);
        treeMap.put(p1,1);
        treeMap.put(p2,2);
        treeMap.put(p3,3);
        treeMap.put(p4,4);
        treeMap.put(p5,5);
        treeMap.put(p6,6);
        //迭代遍历
        Set entrySet =treeMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
            Map.Entry<Person3, Integer> entry = (Map.Entry<Person3, Integer>)object;
            System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
    }
    //定制排序
    @Test
    public void test2(){
        Comparator comparator = new Comparator(){
            //按照年龄从小到大排序
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Person && o2 instanceof  Person){
                    Person person1 = (Person)o1;
                    Person person2 = (Person)o2;
                    return Integer.compare(person1.getAge(),person2.getAge());
                }else {
                    throw new RuntimeException("输入的数据类型不匹配");
                }
            }
        };
        TreeMap treeMap = new TreeMap(comparator);
        Person p1 = new Person("John",22);
        Person p2 = new Person("Sam",25);
        Person p3 = new Person("Mike",15);
        Person p4 = new Person("Tom",75);
        Person p5 = new Person("Bob",34);
        Person p6 = new Person("Bob",43);
        treeMap.put(p1,1);
        treeMap.put(p2,2);
        treeMap.put(p3,3);
        treeMap.put(p4,4);
        treeMap.put(p5,5);
        treeMap.put(p6,6);
        //迭代遍历
        Set entrySet =treeMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
            Map.Entry<Person3, Integer> entry = (Map.Entry<Person3, Integer>)object;
            System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
    }
}
