package com.bigDragon.javase.java8;

/**
 * java 8 新特性
 *  目录架构
 *  1.Lambda表达式
 *  2.函数式样（Functional）接口
 *  3.方法引用于构造器引用
 *  4.强大的StreamApi
 *  5.Optional类
 *
 *  java8中两大最重要的改变。第一个是Lambda表达式；另一个则是StreamAPI
 * @author bigDragon
 * @create 2020-12-15 10:47
 */
public class Java8Main {
    public static void main(String[] args) {
        //1.Lambda表达式 2.函数式样（Functional）接口
        new LambdaTest();

        //3.方法引用于构造器引用
        //方法引用
        new MethodReferencesTest();
        //构造器引用和数组引用
        new ConstructorRefTest();
        //4.强大的Stream API
        new StreamApiTest();
        //5.Optional类
        new OptionalTest();
    }
}
