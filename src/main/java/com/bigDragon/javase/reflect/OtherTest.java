package com.bigDragon.javase.reflect;

import com.bigDragon.javase.reflect.model.Person;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 运行时类的其他结构
 *
 * @author bigDragon
 * @create 2020-12-09 15:59
 */
public class OtherTest {
    public static void main(String[] args){
        OtherTest otherTest = new OtherTest();
        //运行时类的构造器结构
        otherTest.test1();
        //获取运行时类的父类
        otherTest.test2();
        //获取运行时类的带泛型的父类
        otherTest.test3();
        //获取运行时类的带泛型的父类的泛型
        otherTest.test4();
        //获取运行时类实现的接口
        otherTest.test5();
        //获取运行时类所在的包
        otherTest.test6();
        //获取运行时类声明的注解
        otherTest.test7();
    }
    /**
     * 运行时类的构造器结构
     */
    @Test
    public void test1(){
        Class<Person> personClass = Person.class;
        //getConstructors():获取当前运行时类中声明为public的构造器
        Constructor<?>[] constructors = personClass.getConstructors();
        for(Constructor constructor : constructors){
            System.out.println(constructor);
        }
        System.out.println();
        //getDeclaredConstructors():获取当前运行时类中声明的所有的构造器
        Constructor<?>[] declaredConstructors = personClass.getDeclaredConstructors();
        for(Constructor constructor : declaredConstructors){
            System.out.println(constructor);
        }
    }

    /**
     * 获取运行时类的父类
     */
    @Test
    public void test2(){
        Class<Person> personClass = Person.class;

        Class<? super Person> superclass = personClass.getSuperclass();
        System.out.println(superclass);//class com.bigDragon.javase.reflect.model.Creature
    }

    /**
     * 获取运行时类的带泛型的父类
     */
    @Test
    public void test3(){
        Class<Person> personClass = Person.class;

        Type genericSuperclass = personClass.getGenericSuperclass();
        System.out.println(genericSuperclass);//com.bigDragon.javase.reflect.model.Creature<java.lang.String>
    }

    /**
     * 获取运行时类的带泛型的父类的泛型
     */
    @Test
    public void test4(){
        Class<Person> personClass = Person.class;

        Type genericSuperclass = personClass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
        //获取泛型类型
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for(Type type:actualTypeArguments){
            //方法一
            System.out.println(type.getTypeName());
            //方法二
            System.out.println(((Class)type).getName());
        }
    }

    /**
     * 获取运行时类实现的接口
     */
    @Test
    public void test5(){
        Class<Person> personClass = Person.class;
        //获取运行时类实现的接口
        Class<?>[] interfaces = personClass.getInterfaces();
        for(Class clazz:interfaces){
            System.out.println(clazz);
        }

        System.out.println();
        //获取运行时类的父类实现的接口
        Class<?>[] interfaces2 = personClass.getSuperclass().getInterfaces();
        for(Class clazz:interfaces2){
            System.out.println(clazz);
        }
    }

    /**
     * 获取运行时类所在的包
     */
    @Test
    public void test6(){
        Class<Person> personClass = Person.class;

        Package aPackage = personClass.getPackage();
        System.out.println(aPackage);
    }

    /**
     * 获取运行时类声明的注解
     */
    @Test
    public void test7(){
        Class<Person> personClass = Person.class;
        Annotation[] annotations = personClass.getAnnotations();
        for (Annotation annotation : annotations){
            System.out.println(annotation);
        }
    }
}
