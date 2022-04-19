package com.bigDragon.javase.baseTest;

/**
 * 单例设计模式：饿汉式
 * @author bigDragon
 * @create 2021-04-12 9:48
 */
public class SingletonTest1 {
    public static void main(String[] args) {
        Bank bank1 = Bank.getInstance();
        Bank bank2 = Bank.getInstance();
        System.out.println(bank1 == bank2);//true 两个变量指向同一个对象
    }
}
//单例的Bank类
//饿汉式
class Bank{
    //1.私有化类的构造器
    private Bank(){
    }

    //2.内部创建类的对象
    //4.要求此对象也必须声明为静态的
    //static的方法中只能调用static的属性，故此属性也申明为static
    private static Bank instance = new Bank();

    //3.提供公共的静态的方法，返回类的对象
    //因此类是私有化的构造器，无法中外部获取此类对象，故方法必须声明为static
    public static Bank getInstance(){
        return instance;
    }
}