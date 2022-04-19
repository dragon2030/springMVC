package com.bigDragon.javase.reflect.jdkProxy;
/**
 * 测试类
 * @author bigDragon
 * @create 2021-01-19 17:01
 */
public class JdkProxy {
    public static void main(String[] args) {
        SuperMan superMan = new SuperMan();
        //proxyInstance:代理类的对象
        Human proxyInstance = (Human) ProxyFactory.getProxyInstance(superMan);
        //当通过代理类对象调用方法时，会自动的调用被代理类中同名方法
        String belief = proxyInstance.getBelief();
        System.out.println(belief);
        proxyInstance.eat("四川麻辣烫");

    }
}
