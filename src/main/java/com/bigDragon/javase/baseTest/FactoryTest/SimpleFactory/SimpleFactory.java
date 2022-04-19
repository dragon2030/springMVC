package com.bigDragon.javase.baseTest.FactoryTest.SimpleFactory;

/**
 * 简单工厂模式
 * 造对象和调用对象实现分离，造对象的步骤交给工厂方法执行，外部只需要调用具体执行方法。
 * 缺点：当增加产品的时候，就需要改变工厂类的判断逻辑，这违反了开闭原则（对扩展开发，对修改封闭），所以就有了“工厂方法模式”
 *
 * @author bigDragon
 * @create 2021-04-14 15:11
 */
public class SimpleFactory {
    public static void main(String[] args) {
        //调用奥迪的方法
        CarFactory.getCar("奥迪").run();
        CarFactory.getAudi().run();
        //调用比亚迪的方法
        CarFactory.getCar("比亚迪").run();
        CarFactory.getBYD().run();
    }
}
interface Car{
    void run();
}
class Audi implements Car{

    @Override
    public void run() {
        System.out.println("奥迪在跑");
    }
}
class BYD implements Car{

    @Override
    public void run() {
        System.out.println("比亚迪在跑");
    }
}
//创建了简单的工厂模式
class CarFactory{
    public static Car getCar(String type){
        if("奥迪".equals(type)){
            return new Audi();
        } else if ("比亚迪".equals(type)) {
            return new BYD();
        }else {
            return null;
        }
    }
    public static Car getAudi(){
        return new Audi();
    }
    public static Car getBYD(){
        return new BYD();
    }
}
