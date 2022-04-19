package com.bigDragon.javase.baseTest.FactoryTest.FactoryFunction;

/**
 * 工厂方法
 *
 * 有一个工厂接口CarFactory，定义了一个抽象方法getCar返回Car对象
 * 两个工厂实现类实现了工厂接口CarFactory，实现了getCar()方法的具体细节
 * 实例化工厂实现类的对象调用以实现了的getCar()方法获取具体对象的实例
 *
 * 这样如果需要功能扩展可以增加具体的车和具体的工厂分别实现对应接口就可以，不用对现有方法进行修改
 * 缺点：需要把新加的具体实现逻辑写死在代码里，不能动态生成，所以就有了“抽象工厂模式”
 *
 * @author bigDragon
 * @create 2021-04-14 15:36
 */
public class FactoryFunction {
    public static void main(String[] args) {
        //调用奥迪的run()方法
        new AudiFactory().getCar().run();
        //调用比亚迪的run()方法
        new BYDFactory().getCar().run();
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
//工厂接口
interface CarFactory{
    Car getCar();
}
//两个工厂类
class AudiFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new Audi();
    }
}
class BYDFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new BYD();
    }
}