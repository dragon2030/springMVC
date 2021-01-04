package com.bigDragon.javase.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lambda表达式
 * 一、Lambda表达式的概念
 * Lambda是一个匿名函数，我们可以把Lambda表达式理解为是一段可以传递的代码（将代码想暑假一样进行传递）。
 * 使用它可以写出更简洁、更灵活的代码。作为一种更紧凑的代码风格，使Java的语言的语言表达能力得到提升。
 * 二、Lambda表达式的使用
 * 1.举例：(o1,o2) -> Integer.compare(o1,o2);
 * 2.格式：
 *      -> : lambda操作符 或  箭头操作符
 *      ->左边 : lambda新参列表（其实就是接口中的抽象方法的形参列表）
 *      ->右边 : lambda体 （其实就是重写的抽象方法的方法体）
 * 3.lambda表达式的使用：（分6中情况介绍）
 *      语法格式一：无参无返回值
 *      语法格式二：lambda需要一个参数，但是没有返回值
 *      语法格式三：数据类型可以省略，因为可由编译器推断得出，称为“类型推断”
 *      语法格式四：lambda若只需要一个参数时，参数的小括号可以省略
 *      语法格式五：lambda需要两个或以上的参数，多条执行语句，并且可以有返回值
 *      语法格式六：当lambda体只有一条语句是，return与大括号若有都可以省略
 *
 *      总结：
 *          -> 左边：lambda形参列表的参数类型可以省略（类型推断）;如果lambda形参列表只有一个参数，其一对小括号也可省略。
 *          -> 右边：lambda体应该使用一对{}包裹；如果lambda体只有一条执行语句（可能是return语句），可以省略这一对{}和
 *          return关键字。
 * 4.lambda表达式的本质：作为函数式接口的实例
 * 5.什么是函数式接口
 *      >如果一个接口中，只声明了一个抽象方法，则此接口就称为函数式接口
 *      >可以通过lambda表达式来创建函数式接口的对象。
 *      >可以再一个接口上使用@FunctionalInterface注解，这样可以检查是否是一个函数式
 *      接口,同时javadoc也会包含一条声明，说明这个接口是一个函数式接口。
 *      >lambda表达式就是一个函数式接口的实例
 * 6.java内置的4大核心函数式接口
 *      >消费型接口      Consumer<T>     void accept(T t)
 *      >供给型接口      Supplier<T>     T get()
 *      >函数型接口      Function<T,R>   R apply(T t)
 *      >断定型接口      Predicate<T>    boolean test(T t)
 *
 * @author bigDragon
 * @create 2020-12-15 11:36
 */
public class LambdaTest {
    public static void main(String[] args) {
        LambdaTest lambdaTest = new LambdaTest();
        //使用举例：提供一个实现Runnable接口的匿名实现类的对象
        lambdaTest.test1();
        //使用举例：lambda表达式与方法引用
        lambdaTest.test2();
        //lambda表达式的语法格式
        //语法格式一：无参无返回值
        lambdaTest.format1();
        //语法格式二：lambda需要一个参数，但是没有返回值
        lambdaTest.format2();
        //语法格式三：数据类型可以省略，因为可由编译器推断得出，称为“类型推断”
        lambdaTest.format3();
        //语法格式四：lambda若只需要一个参数时，参数的小括号可以省略
        lambdaTest.format4();
        //语法格式五：lambda需要两个或以上的参数，多条执行语句，并且可以有返回值
        lambdaTest.format5();
        //语法格式六：当lambda体只有一条语句是，return与大括号若有都可以省略
        lambdaTest.format6();
        //java内置的4大核心函数式接口:消费型接口 Consumer<T> void accept(T t)
        lambdaTest.functionTest1();
        //java内置的4大核心函数式接口:断定型接口 Predicate<T> boolean test(T t)
        lambdaTest.functionTest4();
    }
    @Test
    public void test1(){
        //提供一个实现Runnable接口的匿名实现类的对象
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程："+Thread.currentThread().getName()+"我爱吃苹果");
            }
        };
        runnable.run();
        //new Thread(runnable).start();

        System.out.println("***************************");
        //用Lambda表达式提供一个实现Runnable接口的匿名实现类的对象
        Runnable runnable2 = () -> System.out.println("我爱吃香蕉");
        runnable2.run();
    }
    /*
    lambda表达式与方法引用
     */
    @Test
    public void test2(){
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1,o2);
            }
        };
        System.out.println(comparator.compare(12,21));

        System.out.println("******************************");

        //lambda表达式
        Comparator<Integer> comparator2 = (o1, o2) -> Integer.compare(o1,o2);
        System.out.println(comparator2.compare(12,21));

        System.out.println("******************************");
        //方法引用
        Comparator<Integer> comparator3 = Integer::compareTo;
        System.out.println(comparator3.compare(12,21));
    }
    /*
    lambda表达式的语法格式
    语法格式一：无参无返回值
     */
    @Test
    public void format1(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程："+Thread.currentThread().getName()+"我爱吃苹果");
            }
        };
        runnable.run();

        System.out.println("***************************");
        //用Lambda表达式提供一个实现Runnable接口的匿名实现类的对象
        Runnable runnable2 = () -> System.out.println("我爱吃香蕉");
        runnable2.run();
    }
    /*
    lambda表达式的语法格式
    语法格式二：lambda需要一个参数，但是没有返回值
     */
    @Test
    public void format2(){
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("谎言和誓言的区别");

        System.out.println("********************************");

        Consumer<String> consumer2 = (String s) -> {
            System.out.println(s);
        };
        consumer2.accept("一个听的人当真了，一个说的人当真了");
    }
    /*
    lambda表达式的语法格式
    语法格式三：数据类型可以省略，因为可由编译器推断得出，称为“类型推断”
    */
    public void format3(){
        Consumer<String> consumer2 = (String s) -> {
            System.out.println(s);
        };
        consumer2.accept("一个听的人当真了，一个说的人当真了");

        System.out.println("********************************");

        Consumer<String> consumer3 = (s) -> {
            System.out.println(s);
        };
        consumer3.accept("一个听的人当真了，一个说的人当真了");

        //类型推断举例
        ArrayList<String> list = new ArrayList<>();//类型推断
        int[] arr = {1,2,3};//类型推断
    }

    /*
    lambda表达式的语法格式
    语法格式四：lambda若只需要一个参数时，参数的小括号可以省略
     */
    @Test
    public void format4(){
        Consumer<String> consumer = (s) -> {
            System.out.println(s);
        };
        consumer.accept("一个听的人当真了，一个说的人当真了");

        System.out.println("********************************");

        Consumer<String> consumer2 = s -> {
            System.out.println(s);
        };
        consumer2.accept("一个听的人当真了，一个说的人当真了");
    }
    /*
    lambda表达式的语法格式
    语法格式五：lambda需要两个或以上的参数，多条执行语句，并且可以有返回值
     */
    @Test
    public void format5(){
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                System.out.println(o1);
                System.out.println(o2);
                return o1.compareTo(o2);
            }
        };
        System.out.println(comparator.compare(12,21));
        System.out.println("********************************");
        Comparator<Integer> comparator2 = (o1, o2) -> {
            System.out.println(o1);
            System.out.println(o2);
            return o1.compareTo(o2);
        };
        System.out.println(comparator2.compare(12,6));
    }

    /*
    lambda表达式的语法格式
    语法格式六：当lambda体只有一条语句是，return与大括号若有都可以省略
     */
    @Test
    public void format6(){
        Comparator<Integer> comparator = (o1, o2) -> {
            return o1.compareTo(o2);
        };
        System.out.println(comparator.compare(12,21));
        System.out.println("********************************");
        Comparator<Integer> comparator2 = (o1, o2) -> o1.compareTo(o2);
        System.out.println(comparator2.compare(12,6));
    }
    /*
    java内置的4大核心函数式接口:消费型接口 Consumer<T> void accept(T t)
     */
    @Test
    public void functionTest1(){
        happyTime(500, new Consumer<Double>() {
            @Override
            public void accept(Double aDouble) {
                System.out.println("学习太累了，去天上人间买瓶矿井水。价格："+aDouble);
            }
        });
        System.out.println("*********************************");
        happyTime(500, aDouble -> System.out.println("学习太累了，去天上人间买瓶矿井水。价格："+aDouble));
    }
    public void happyTime(double money,Consumer<Double> con){
        con.accept(money);
    }
    /*
    java内置的4大核心函数式接口:断定型接口 Predicate<T> boolean test(T t)
     */
    @Test
    public void functionTest4(){
        List<String> list = Arrays.asList("北京", "南京", "天津", "东京", "西京", "普京");
        List<String> filterStrs = filterString(list, new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("京");
            }
        });
        System.out.println(filterStrs);
        System.out.println("**********************************");
        List<String> filterStrs2 = filterString(list, s -> s.contains("京"));
        System.out.println(filterStrs2);
    }
    //根据给定的规则，过滤集合中的字符串。此规则由Predicate的方法决定
    public List<String> filterString(List<String>list, Predicate<String> pre){
        ArrayList<String> filterList = new ArrayList<>();
        for(String s : list){
            if (pre.test(s)){
                filterList.add(s);
            }
        }
        return filterList;
    }
}
