package com.bigDragon.javase.java8;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用的使用
 *
 * 1.使用情境：当要传递给lambda体的操作，已经有实现的方法了，可以使用方法引用。
 * 2.方法引用，本质上就是lambda表达式，而lambda表达式作为函数式接口的实例，所以
 * 方法引用，也是函数式接口的实例。
 * 3.使用格式：  类（或对象）:: 方法名
 * 4.具体分为如下三种情况
 *      >情况1    对象 :: 非静态方法
 *      >情况2    类 :: 静态方法
 *
 *      >情况3    类 :: 非静态方法
 * 5.方法引用使用的要求：要求 接口中的抽象方法的形参列表和返回值类 与 方法引用的形参列表和返回值类 相同！
 * （针对于情况1和情况2）
 *
 * @author bigDragon
 * @create 2020-12-16 14:49
 */
public class MethodReferencesTest {
    public static void main(String[] args) {
        MethodReferencesTest methodReferencesTest = new MethodReferencesTest();

        //情况1    对象 :: 非静态方法
        //Consumer中void accept(T t)
        //PrintStream中的void println(T t)
        methodReferencesTest.test1();
        //Supplier中的T get()
        //Person中的String getName())
        methodReferencesTest.test2();

        //情况2    类 :: 静态方法
        //Comparator中的int compare(T t1,T t2)
        //Integer中的int compare(T t1,T t2)
        methodReferencesTest.test3();
        //Function中R apply(T t)
        //Math中Long round(Double d)
        methodReferencesTest.test4();

        //情况3    类 :: 非静态方法
        //Comparator中int compare(T t1,T t2)
        //String中的int t1.compareTo(t2)
        methodReferencesTest.test5();
        //BigPredicate中的boolean test(T t1, T t2)
        //String中的boolean t1.equals(t2)
        methodReferencesTest.test6();
        //Function中的R apply(T t)
        //Person中的String getName()
        methodReferencesTest.test7();
    }

    /*
    情况一：对象 :: 非静态方法
    Consumer中void accept(T t)
    PrintStream中的void println(T t)
     */
    @Test
    public void test1(){
        PrintStream ps = System.out;
        Consumer<String> consumer4 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                ps.println(s);
            }
        };
        consumer4.accept("北京4");
        System.out.println("*************************************");
        Consumer<String> consumer = str -> System.out.println(str);
        Consumer<String> consumer3 = str -> ps.println(str);
        consumer.accept("北京1");
        consumer3.accept("北京3");
        System.out.println("*************************************");
        Consumer<String> con2 = ps::println;
        con2.accept("北京2");
    }

    /*
    情况一：对象 :: 非静态方法
    Supplier中的T get()
    Person中的String getName()
     */
    @Test
    public void test2(){
        Person person = new Person("Mike", 22);
        Supplier<String> sup1 = new Supplier<String>() {
            @Override
            public String get() {
                return person.getName();
            }
        };
        System.out.println(sup1.get());
        System.out.println("*************************************");
        Supplier<String> sup2 = () -> person.getName();
        System.out.println(sup2.get());
        System.out.println("*************************************");
        Supplier<String> sup3 = person::getName;
        System.out.println(sup3.get());
    }
    /*
    情况2：类 :: 静态方法
    Comparator中的int compare(T t1,T t2)
    Integer中的int compare(T t1,T t2)
     */
    @Test
    public void test3(){
        Comparator<Integer> com0 = new Comparator<Integer>(){
            @Override
            public int compare(Integer t1, Integer t2) {
                return Integer.compare(t1,t2);
            }
        };
        Comparator<Integer> com1 = (t1,t2) -> Integer.compare(t1,t2);
        System.out.println(com1.compare(12,21));
        System.out.println("*************************************");
        Comparator<Integer> com2 = Integer::compareTo;
        System.out.println(com2.compare(12,21));
    }
    /*
    情况2：类 :: 静态方法
    Function中R apply(T t)
    Math中Long round(Double d)
     */
    @Test
    public void test4(){
        Function<Double,Long> func = new Function<Double, Long>() {
            @Override
            public Long apply(Double aDouble) {
                return Math.round(aDouble);
            }
        };
        System.out.println(func.apply(100d));
        System.out.println("*************************************");
        Function<Double,Long> func2 = aDouble -> Math.round(aDouble);
        System.out.println(func2.apply(200d));
        System.out.println("*************************************");
        Function<Double,Long> func3 = Math::round;
        System.out.println(func3.apply(300d));
    }
    /*
    情况三：类 :: 非静态方法
    Comparator中int compare(T t1,T t2)
    String中的 t1.compareTo(t2)
     */
    @Test
    public void test5(){
        Comparator<String> com1 = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        };
        System.out.println(com1.compare("32","12"));
        System.out.println("*************************************");
        Comparator<String> com2 = (s1, s2) -> s1.compareTo(s2);
        System.out.println(com2.compare("11","12"));
        System.out.println("*************************************");
        Comparator<String> com3 = String::compareTo;
        System.out.println(com3.compare("12","12"));
    }
    /*
    情况三：类 :: 非静态方法
    BigPredicate中的boolean test(T t1, T t2)
    String中的boolean t1.equals(t2)
     */
    @Test
    public void test6(){
        BiPredicate<String,String> pre1 = new BiPredicate<String, String>() {
            @Override
            public boolean test(String s, String s2) {
                return s.equals(s2);
            }
        };
        System.out.println(pre1.test("abc","bca"));
        System.out.println("*************************************");
        BiPredicate<String,String> pre2 = (s, s2) -> s.equals(s2);
        System.out.println(pre2.test("abc","bca"));
        System.out.println("*************************************");
        BiPredicate<String,String> pre3 = String::equals;
        System.out.println(pre3.test("abc","bca"));
    }
    /*
    情况三：类 :: 非静态方法
    Function中的R apply(T t)
    Person中的String getName()
     */
    @Test
    public void test7(){
        Person person = new Person("Mike", 22);
        Function<Person,String> fun1 = new Function<Person, String>() {
            @Override
            public String apply(Person person) {
                return person.getName();
            }
        };
        System.out.println(fun1.apply(person));
        System.out.println("*************************************");
        Function<Person,String> fun2 = person1 -> person1.getName();
        System.out.println(fun2.apply(person));
        System.out.println("*************************************");
        Function<Person,String> fun3 = Person::getName;
        System.out.println(fun3.apply(person));
    }
}
