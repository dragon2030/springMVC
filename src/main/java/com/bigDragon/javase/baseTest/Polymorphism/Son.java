package com.bigDragon.javase.baseTest.Polymorphism;

/**
 * @author bigDragon
 * @create 2021-04-02 20:12
 */
public class Son extends Father {
    public int id = 2;
    public int sonId= 1003;
    public void eat(){
        System.out.println("Son类的eat方法");
    }
    public void sonMethod(){}
    public Son(){
        super();
        System.out.println("Son构造器执行");
    }
}
