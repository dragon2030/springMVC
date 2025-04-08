package com.bigDragon.javase.reflect;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Random;

/**
 * 通过反射创建对应的运行时类的对象
 *
 *      通过Class实例获取目标类的构造器与对象
 *      newInstance():调用此方法，创建对应的运行时类的对象。内部调用了运行时类的空参构造器。
 *         想要此方法正常的创建运行时类的对象，要求：
 *         1.运行时类必须提供空参的构造器。
 *         2.空参的构造器的访问权限得够。通常，设置为public。
 *
 *         在spring框架 java bean中要求提供一个public的空参构造器，原因：
 *         1.便于通过反射，创建运行时类的对象
 *         2.便于子类继承运行时类时，默认调用super()时，保证父类有此构造器
 *
 * @author bigDragon
 * @create 2020-12-08 14:47
 */
public class NewInstanceTest {
    public static void main(String[] args) throws Exception {
        NewInstanceTest newInstanceTest = new NewInstanceTest();
        //通过Class实例获取目标类的构造器与对象
        newInstanceTest.test();
        //创建一个指定类的对象
        newInstanceTest.getInstance("");
        //体会反射的动态性:运行时才会确认实例化的类
        newInstanceTest.test2();
    }
    @Test
    public void test() throws Exception {
        //通过Class实例获取目标类的构造器与对象
        //newInstance():调用此方法，创建对应的运行时类的对象。内部调用了运行时类的空参构造器。
        /*
        想要此方法正常的创建运行时类的对象，要求：
        1.运行时类必须提供空参的构造器。
        2.空参的构造器的访问权限得够。通常，设置为public。

        在javabean中要求提供一个public的空参构造器，原因：
        1.便于通过反射，创建运行时类的对象
        2.便于子类继承运行时类时，默认调用super()时，保证父类有此构造器
         */
        Class<Person> personClass = Person.class;
        Person person = personClass.newInstance();
        System.out.println(person);//Person{name='null', age=0}
        //public Constructor<T> getConstructor(Class<?>... parameterTypes):调用此方法,调用了运行时类的有参构造器
        Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
        Person person1 = constructor.newInstance("Tom", 12);
        System.out.println(person1);//Person{name='Tom', age=12}

        //当类没有无参构造器时，报错java.lang.InstantiationException
        Class<Person2> person2Class = Person2.class;
        Person2 person2 = person2Class.newInstance();
        System.out.println(person2);
    }

    /**
     * 创建一个指定类的对象
     * classPath:指定类的全类名
     */
    public Object getInstance(String classPath) throws Exception {
        Class<?> aClass = Class.forName(classPath);
        return aClass.newInstance();
    }

    /**
     * 体会反射的动态性:运行时才会确认实例化的类
     */
    @Test
    public void test2(){
        for(int i = 0;i < 100;i++){
            int num = new Random().nextInt(3);
            String classPath = "";
            switch (num){
                case 0:
                    classPath = "java.util.Date";
                    break;
                case 1:
                    classPath = "java.lang.Object";
                    break;
                case 2:
                    classPath = "com.bigDragon.javase.reflect.Person";
                    break;
            }
            try {
                Object object = getInstance(classPath);
                System.out.println(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
