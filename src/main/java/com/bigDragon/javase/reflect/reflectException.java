package com.bigDragon.javase.reflect;

import java.lang.reflect.Method;

/**
 * java.lang.reflect.InvocationTargetException异常及处理
 * 在我们使用Java的反射API时，我们经常会遇到java.lang.reflect.InvocationTargetException异常，在本教程中，我们将通过一个简单的实例来了解它
 * 以及处理它。
 *
 * 原文：http://www.subquery.cn/wordpress/exceptions/java-lang-reflect-invocation-target/
 *
 * @author bigDragon
 * @create 2020-12-08 13:56
 */
public class reflectException {
    public static void main(String[] args){
        try {
            //method1——原调用方法
            reflectException.testClass testClass = new reflectException().new testClass();
            //testClass.test();
            //method2——反射调用方法
            Class<? extends reflectException.testClass> aClass = new reflectException().new testClass().getClass();
            //调用方法
            Method show = aClass.getDeclaredMethod("test");
            show.invoke(testClass);
        } catch (Exception e) {
            System.out.println(e.getCause().getClass());
            e.printStackTrace();
        }
    }
    class testClass{//异常类
        public void test(){
            new String("12").substring(5);
        }
        public testClass(){}
    }
}
