package com.bigDragon.javase.Generics;

import org.junit.Test;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 1.泛型在继承方面的体现
 *
 *      虽然类A是类B的父类，但是G<A>和G<B>二者不具备子父类关系，二者是并列关系。
 *
 *      类A是类B的父类，A<G>是B<G>的父类
 *
 * 2.通配符的使用
 *      通配符：？
 *
 *      类A是类B的父类，G<A>和G<B>是没有关系的，二者共同的父类是G<?>
 *
 * 3.有限制条件的通配符使用
 * ? extends A：（<=）
 *          G<? extends A> 可以作为G<A>和G<B>的子类时，其中B是A的子类
 * ? super A：（>=）
 *          G<? extends A> 可以作为G<A>和G<B>的父类时，其中B是A的父类
 *
 * @author bigDragon
 * @create 2020-11-26 9:49
 */
public class GenericTest2 {
    /**
     * 泛型在继承方面的提现
     *      虽然类A是类B的父类，但是G<A>和G<B>二者不具备子父类关系，二者是并列关系。
     *      类A是类B的父类，A<G>是B<G>的父类
     */
    @Test
    public void test(){
        Object obj = null;
        String st = null;
        obj = st;

        Object[] oArray = null;
        String[] sArray = null;
        oArray = sArray;

        List<Object> list1 = null;
        List<String> list2 = null;
        //此时的list1和list2的类型不具有子父类关系
        //编译不通过
        //list1 = list2;

        AbstractList<String> list3 = null;
        ArrayList<String> list4 = null;
        list3 = list4;
    }

    /**
     * 2.通配符的使用
     *  通配符：？
     *
     *  类A是类B的父类，G<A>和G<B>是没有关系的，二者共同的父类是G<?>
     */
    @Test
    public void test2(){
        List<Object> list1 = null;
        List<String> list2 = new ArrayList<>(Arrays.asList(new String[]{"a","b","c"}));
        //List<String> list2 = null;

        List<?> list = null;
        //此时list作为list1和list2的通用父类
        list = list1;
        list = list2;
        print(list);
        //添加(写入):对于List<?>就不能向其内部添加数据
        //除了添加null之外
        //list.add("a");
        list.add(null);
        //获取（读取）：允许读取数据，读取的数据类型为Object
        Object o=list.get(0);
        System.out.println(o);
    }

    /**
     * 3.有限制条件的通配符使用
     * ? extends A：（<=）
     *          G<? extends A> 可以作为G<A>和G<B>的子类时，其中B是A的子类
     * ? super A：（>=）
     *          G<? extends A> 可以作为G<A>和G<B>的父类时，其中B是A的父类
     */
    @Test
    public void test3(){
        List<? extends Person> list1 = null;
        List<? super Person> list2 = null;

        List<Student> list3 = null;
        List<Person> list4 = null;
        List<Object> list5 = null;

        //extends
        list1 = list3;
        list1 = list4;
        //list1 = list5;//编译报错

        //list2 = list3;//编译报错
        list2 = list4;
        list2 = list5;

        //读取数据：
        Person p = list1.get(0);
        //编译不通过
        //Student s = list1.get(0);

        Object object = list2.get(0);
        //编译不通过
        //Person p = list2.get(0);

        //写入数据
        //list1.add(new Student());//可能泛型类型比Student更小的类

        list2.add(new Person());
        list2.add(new Student());
    }

    public void print(List<?> list){
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
            System.out.println(object);
        }
    }
}
