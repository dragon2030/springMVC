package com.bigDragon.javase.Generics;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 泛型
 *
 * 一、泛型的概念：所谓泛型，就是允许在定义类、接口时通过一个标识表示类中的某个属性的类型或者是某个方法的返回值及参数类型。这个类型
 * 参数将在使用时确定。
 * 二、泛型的使用
 * 1.jdk5.0新增的特性
 * 2.在集合中使用的泛型：
 *      1.集合接口或集合类在jkd5.0时都修改为带泛型的结构。
 *      2.在实例化集合时，可以指明具体的泛型类型。
 *      3.指明完以后，在集合类活接口中凡是定义类活接口时，内部结构使用到类的泛型的位置，都会指定为实例化的泛型类型
 *        比如：add（E e） --->实例化以后：add(Integer e)
 *      4.泛型的类型必须是类，不能是基本数据类型。需要用到基本数据类型的位置，拿包装类来替换
 *      5.如果实例化时，没有指明泛型的类型，默认类型为java.lang.Object类型。
 * 三、如果自定义泛型结构:泛型类、泛型结构、泛型方法
 *
 * @author bigDragon
 * @create 2020-10-29 11:31
 */
public class GenericsMain {
    public static void main(String[] args){
        GenericsMain genericsMain=new GenericsMain();

        //List对泛型的应用
        genericsMain.test();
        //Map对泛型的应用
        genericsMain.test2();
        //自定义泛型类
        new Order();
        //自定义泛型类使用
        new GenericTest1();
        //子类继承泛型类
        //由于子类在继承带泛型的父类时，指明了泛型的类型，则实例化子类对象时，不在需要指明泛型
        Order<Integer> order= new SubOrder();//不再是泛型类，只是普通类
        Order<Boolean> order2 = new SubOrder2<Boolean>();//是泛型类
        //自定义泛型方法
        new Order2().copyFromArrayToList(new String[5]);
        String str = new Order2().genCode();
        Integer i = new Order2().genCode();

        //泛型在继承方面的体现
        //通配符的使用
        //有限制条件的通配符使用
        new GenericTest2();

    }

    /**
     * List对泛型的应用
     */
    @Test
    public void test(){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(2);
        //list.add("4");//编译时，就会进行类型检查，保证数据的安全
        for(Integer integer : list){
            int i = integer;//避免了强转操作
            System.out.println(i);
        }
    }

    /**
     * Map对泛型的应用
     */
    @Test
    public void test2(){
        //Map<Integer,String> map =new HashMap<Integer,String>();
        //jdk7新特性：类型推断
        Map<Integer,String> map =new HashMap<>();
        map.put(1,"A");
        map.put(2,"B");
        map.put(3,"C");
        //entrySet
        Set<Map.Entry<Integer,String>> s=map.entrySet();
        Iterator<Map.Entry<Integer,String>> iterator = s.iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,String> entry= iterator.next();
            System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
        Set<Integer> set = map.keySet();
        Iterator<Integer> iterator1=set.iterator();
        while (iterator1.hasNext()){
            Integer integer=iterator1.next();
            System.out.println(integer+"--->"+map.get(integer));
        }
    }

    /**
     * 子类继承泛型类
     */
    @Test
    public void test3(){
        //由于子类在继承带泛型的父类时，指明了泛型的类型。则实例化子类对象时，不在需要指明泛型。
        SubOrder subOrder = new SubOrder();
        subOrder.setOrderT(123);

        SubOrder2<String> subOrder2 = new SubOrder2();
        subOrder2.setOrderT("123");
    }
}
