package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * jdk5.0新增了foreach循环，用于遍历集合、数组
 * @author bigDragon
 * @create 2020-11-03 15:12
 */
public class ForEachTest {
    @Test
    public void method1(){
        Collection collection =new ArrayList();
        collection.add("AA");
        collection.add("BB");
        collection.add(new Person("Mike",22));
        collection.add(new Person("Sam",21));
        collection.add("CC");

        //for(集合元素的类型 局部变量：集合对象)
        //内部仍然调用了迭代器
        for(Object object:collection)
            System.out.println(object);
        //for(数组元素的类型 局部变量：数组对象)
        int[] arr=new int[]{1,2,3,4,5};
        for(int i:arr)
            System.out.println(i);
    }
}
