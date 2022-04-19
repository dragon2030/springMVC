package com.bigDragon.javase.baseTest.extendTest;

/**
 * @author bigDragon
 * @create 2021-04-01 19:22
 */
public class Father {
    public String name = "Mike";
    public int age = 45;
    public Father(){
        System.out.println("Father的空参构造器");
    }
    public void eat(){
        System.out.println("Father类的eat方法");
    }
}
