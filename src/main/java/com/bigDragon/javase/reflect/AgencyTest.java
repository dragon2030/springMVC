package com.bigDragon.javase.reflect;

import com.bigDragon.javase.baseTest.procyDemoMevie.ProxyTest;

/**
 * 七、反射的应用：动态代理
 *
 * 1.代理设计模式的原理：
 * 使用一个代理将对象包装起来，然后用该对象取代原始对象。任何对原始对象的调用都要通过代理。
 * 代理对象决定是是否以及何时将方法调用转到原始对象上。
 *
 * 2.动态代理相比于静态代理的优点
 * 抽象角色中（接口）声明的所有方法都被转移到调用处理器一个集中的方法中处理，这样，我们可以更加灵活和统一的
 * 处理众多的方法。
 *
 * @author bigDragon
 * @create 2020-12-14 9:37
 */
public class AgencyTest {
    public static void main(String[] args){
        //静态代理举例
        new StaticProxyTest();
        //动态代理的举例
        new ProxyTest();
    }
}
