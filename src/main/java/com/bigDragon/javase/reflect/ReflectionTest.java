package com.bigDragon.javase.reflect;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 直接实例化和反射的对比
 *
 * 疑问1.通过直接new的方式或反射的方式都可以调用公共的结构，开发中到底用哪个？
 *      建议：直接new的方式
 *          反射的特性：动态性
 * 疑问2：反射机制和面向对象中的封装性是不是矛盾？如何看待两个技术
 *      不矛盾。封装性解决建议调用使用的问题，反射解决能不能调用的问题。
 *
 * @author bigDragon
 * @create 2020-12-01 16:04
 */
public class ReflectionTest {
    /**
     * 反射之前，对于Person的操作
     */
    @Test
    public void test1(){
        //1.创建Person类的对象
        Person person=new Person("Tom",12);

        //2.通过对象，调用其内部的属性、方法
        person.age = 10;
        System.out.println(person);
        person.show();
        //在Person类外部，不可以通过Person类的对象调用其内部私有结构。
        //比如：name、showNation()以及私有的构造器
    }

    /**
     * 反射之后，对于Person的操作
     */
    @Test
    public void test2(){
        try {
            Class clazz = Person.class;

            //1.通过反射，创建Person类的对象
            Constructor constructor = clazz.getConstructor(String.class, int.class);
            Object o = constructor.newInstance("Tom", 12);
            Person p = (Person) o;
            System.out.println(p);

            //2.通过反射，调用对象指定的属性、方法
            //调用属性
            Field age = clazz.getDeclaredField("age");
            age.set(p,10);
            System.out.println(p);

            //调用方法
            Method show = clazz.getDeclaredMethod("show");
            show.invoke(p);

            System.out.println("***************************");

            //通过反射，可以调用Person类的私有结构的。比如：私有的构造器、方法、属性。
            //调用私有的构造器
            Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
            Object o1 = declaredConstructor.newInstance("Jerry");
            Person p1 = (Person) o1;
            System.out.println(p1);

            //调用私有的属性
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            name.set(p1,"HanMeimei");
            System.out.println(p1);
            
            //调用私有的方法
            Method showNation = clazz.getDeclaredMethod("showNation", String.class);
            showNation.setAccessible(true);
            String nation = (String) showNation.invoke(p1,"中国");//相当于p1.showNation(中国)
            System.out.println(nation);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自测
     */
    @Test
    public void test3(){
        try {
            Class clazz = Person.class;
            Person person=new Person("Tom",12);
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            name.set(person,"HanMeimei");
            System.out.println(person);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
