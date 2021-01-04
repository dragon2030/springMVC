package com.bigDragon.javase.reflect;

/**
 * 静态代理举例
 *
 * 特点：代理类和被代理类在编译期间，就确定下来
 *
 * @author bigDragon
 * @create 2020-12-14 10:09
 */
public class StaticProxyTest {
    public static void main(String[] args){
        //创建被代理类的对象
        ClothFactory nikeClothFactory = new NikeClothFactory();
        //创建代理类的对象
        ClothFactory proxyClothFactory = new ProxyClothFactory(nikeClothFactory);
        //调用代理类对象的方法
        proxyClothFactory.produceCloth();
    }
}

interface ClothFactory{
    void produceCloth();
}
//代理类
class ProxyClothFactory implements  ClothFactory{

    private ClothFactory clothFactory;//用被代理类对象进行实例化

    public ProxyClothFactory(ClothFactory clothFactory) {
        this.clothFactory = clothFactory;
    }

    @Override
    public void produceCloth() {
        System.out.println("代理工作做一些准备工作");
        clothFactory.produceCloth();
        System.out.println("代理工作做一些后续收尾工作");
    }
}
//被代理类
class NikeClothFactory implements ClothFactory{

    @Override
    public void produceCloth() {
        System.out.println("Nike工厂生产一批运动服");
    }
}


