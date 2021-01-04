package com.bigDragon.javase.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 构造器引用和数组引用
 * 一、构造器引用
 *      和方法引用类似，函数式接口的抽象方法的形参列表和构造器的形参列表一致
 *      抽象方法的返回值类型即为构造器所属的类的类型
 * 二、数组引用
 *      可以把数组看做事一个特殊的类，则写法与构造器引用一致
 *
 * @author bigDragon
 * @create 2020-12-17 15:28
 */
public class ConstructorRefTest {
    public static void main(String[] args) {
        ConstructorRefTest constructorRefTest = new ConstructorRefTest();
        //构造器引用
        //Supplier中的T get()
        //Person中的空参构造器：Person()
        constructorRefTest.test();
        //Function中R apply(T t)
        //Person中的有参构造器：private Person(String name)
        constructorRefTest.test2();
        //BiFunction中的R apply(T t,U u)
        //Person中的有参构造器：public Person(String name, int age)
        constructorRefTest.test3();

        //数组引用
        //Function中R apply(T t)
        //new String[integer]
        constructorRefTest.test4();
    }
    /*
    构造器引用
    Supplier中的T get()
    Person中的空参构造器：Person()
     */
    @Test
    public void test(){
        Supplier<Person> sup = new Supplier<Person>() {
            @Override
            public Person get() {
                return new Person();
            }
        };
        System.out.println(sup.get());
        System.out.println("****************************");
        Supplier<Person> sup2 = () -> new Person();
        System.out.println(sup2.get());
        System.out.println("****************************");
        Supplier<Person> sup3 = Person::new;
        System.out.println(sup3.get());
        System.out.println("****************************");
    }
    /*
    构造器引用
    Function中R apply(T t)
    Person中的有参构造器：public Person(String name)
     */
    @Test
    public void test2(){
        Function<String,Person> func1 = new Function<String, Person>() {
            @Override
            public Person apply(String s) {
                return new Person(s);
            }
        };
        System.out.println(func1.apply("Mike"));
        System.out.println("****************************");
        Function<String,Person> func2 = s -> new Person(s);
        System.out.println(func2.apply("Mike"));
        System.out.println("****************************");
        Function<String,Person> func3 = Person::new;
        System.out.println(func3.apply("Mike"));
    }
    /*
    构造器引用
    BiFunction中的R apply(T t,U u)
    Person中的有参构造器：public Person(String name, int age)
     */
    @Test
    public void test3(){
        BiFunction<String,Integer,Person> func1 = new BiFunction<String, Integer, Person>() {
            @Override
            public Person apply(String s, Integer integer) {
                return new Person(s,integer);
            }
        };
        System.out.println(func1.apply("Mike",22));
        System.out.println("****************************");
        BiFunction<String,Integer,Person> func2 = (s, integer) -> new Person(s,integer);
        System.out.println(func2.apply("Mike",22));
        System.out.println("****************************");
        BiFunction<String,Integer,Person> func3 = Person::new;
        System.out.println(func3.apply("Mike",22));
    }
    /*
    数组引用
    Function中R apply(T t)
    new String[integer]
     */
    @Test
    public void test4(){
        Function<Integer,String[]> func1 = new Function<Integer, String[]>() {
            @Override
            public String[] apply(Integer integer) {
                return new String[integer];
            }
        };
        System.out.println(Arrays.toString(func1.apply(5)));
        System.out.println("****************************");
        Function<Integer,String[]> func2 = integer -> new String[integer];
        System.out.println(Arrays.toString(func2.apply(10)));
        System.out.println("****************************");
        Function<Integer,String[]> func3 = String[]::new;
        System.out.println(Arrays.toString(func3.apply(15)));
    }
}
