package com.bigDragon.javase.baseTest.Polymorphism;

/**
 * @author bigDragon
 * @create 2021-04-02 20:12
 */
public class Father extends Grandpa{
    public int id = 1;
    public int fatherId= 1001;
    public void eat(){
        System.out.println("Father类的eat方法");
    }
    public void isSmoke(){
        System.out.println("Father类的isSmoke方法");
    }
    public Father(){
        System.out.println("Father构造器执行");
    }
}
