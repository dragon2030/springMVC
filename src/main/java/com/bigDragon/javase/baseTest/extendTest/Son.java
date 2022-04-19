package com.bigDragon.javase.baseTest.extendTest;

/**
 * @author bigDragon
 * @create 2021-04-01 19:22
 */
public class Son extends Father{
    public String name = "Bob";
    public int age = 22;
    public void method2(){
        System.out.println("我是子类");
    }
    public Son(){
        super();
        System.out.println("Son的空参构造器");
    }
    public Son(String name){
        this();
        this.name=name;
        System.out.println("Son的带参构造器");
    }
    public Son(String name,int age){
        this.name=name;
        this.age = age;
    }
    public void eat(){
        System.out.println("Father类的eat方法");
    }
}
