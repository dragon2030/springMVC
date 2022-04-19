package com.bigDragon.javase.baseTest;

import org.junit.Test;

import java.util.Objects;

/**
 * @author bigDragon
 * @create 2021-04-07 14:27
 */
public class ObjectTest {
    //Object类为所有类的父类
    public void test1(){
        Order2 order = new Order2();
        System.out.println(order.getClass().getSuperclass());//class java.lang.Object
    }
    //finalize方法
    @Test
    public void test2(){
        Person person = new Person();
        person = null;//此时对象实体就是垃圾对象，等待被回收，但时间不确定
        System.gc();//强制释放空间
    }

    //==
    @Test
    public void test3(){
        //比较基本数据类型：自动类型提升
        int i = 10;
        double d = 10.0;
        System.out.println(i == d);//true
        char c = 10;
        System.out.println(i == c);//true
        boolean b = true;
        //特别的：char类型数值和字符相比较
        char c1 = 65;
        char c2 = 'A';
        System.out.println(c1 == c2);
        //System.out.println(i == b);//编译报错 Operator '==' cannot be applied to 'int', 'boolean'
        //比较引用数据类型
        String s1 = new String();
        String s2 = new String();
        System.out.println(s1 == s2);//false
    }
    //equals
    @Test
    public void test4(){
        Object o = new Object();
        o.equals(o);
        String s = new String();
        s.equals(s);
    }
    //toString()
    @Test
    public void test5(){
        Person person = new Person();
        System.out.println(person);//com.bigDragon.javase.baseTest.Person@33833882
        System.out.println(person.toString());//com.bigDragon.javase.baseTest.Person@33833882
        System.out.println(new Customer());
    }

}
class Order2{}
class Person{
    //子类重写此方法，可在释放对象前进行某操作
    @Override
    protected void finalize() throws Throwable {
        System.out.println("对象被释放"+this);
    }
}
//重写equals方法
class Customer{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    //重写的原则：比较两个对象的实体内容（属性）是否相等
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof Customer){
            Customer customer = (Customer)obj;
            //比较两个对象的属性值是否相等,基本数据类型==，引用数据类型equals
            return this.age == customer.age && this.name.equals(customer.name);
        }
        return false;
    }

}