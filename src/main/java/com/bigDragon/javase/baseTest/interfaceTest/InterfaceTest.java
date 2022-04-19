package com.bigDragon.javase.baseTest.interfaceTest;

/**
 * 接口的使用
 * 接口的使用——JDK7及以前，只能定义全局常量和抽象方法
 *
 *
 * @author bigDragon
 * @create 2021-04-13 19:35
 */
public class InterfaceTest {
    public static void main(String[] args) {
        System.out.println(Flyable.MAX_SPEED);
        System.out.println(Flyable.MIN_SPEED);
        //Flyable.MIN_SPEED=2;//编译不通过
    }
}
interface Flyable{
    //全局常量
    public static final int MAX_SPEED = 7900;
    int MIN_SPEED = 1;

    //抽象方法
    public abstract void fly();
    void stop();
}
abstract class PlaneA implements Flyable{}
class PlaneB implements Flyable{
    @Override
    public void fly() {

    }

    @Override
    public void stop() {

    }
}