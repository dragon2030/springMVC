package com.bigDragon.javase.baseTest.FactoryTest.NoFactoryTest;

/**
 * 无工厂类时的方法实现
 * @author bigDragon
 * @create 2021-04-14 15:04
 */
public class NoFactoryTest {
    public static void main(String[] args) {
        Car a = new Audi();
        Car b = new BYD();
        a.run();
        b.run();
        //此时，创建者“new Audi();”和调用者“ a.run();”就混在一起
    }
}
interface Car{
    void run();
}
class Audi implements Car {

    @Override
    public void run() {
        System.out.println("奥迪在跑");
    }
}
class BYD implements Car {

    @Override
    public void run() {
        System.out.println("比亚迪在跑");
    }
}