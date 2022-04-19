package com.bigDragon.javase.baseTest;

/**
 * 类的成员之四：代码块（或初始化块）
 * @author bigDragon
 * @create 2021-04-12 16:50
 */
public class BlockTest {
    public static void main(String[] args) {
        System.out.println(BlockTest.desc);
        new BlockTest();
    }
    //属性
    String name;
    int age;
    static String desc = "我是一个人";

    //构造器
    public BlockTest() {
    }

    public BlockTest(String name, int age) {
        this.name = name;
        this.age = age;
    }

    //静态代码块
    static {
        System.out.println("hello,static block");
        desc = "2";
        //调用静态结构
        info();
        //非静态结构不可调用
        //toString();
        //eat();
    }

    //非静态代码块
    {
        System.out.println("hello, block");
        //调用非静态结构
        toString();
        eat();
        //调用静态结构
        desc = "我是一个爱学习的人";
        info();
    }

    static {
        System.out.println("hello,static block-2");
    }

    //方法
    public void eat(){
        System.out.println("吃饭");
    }

    @Override
    public String toString() {
        return "BlockTest{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
    public static void info(){
        System.out.println("我是一个好人");
    }

}
