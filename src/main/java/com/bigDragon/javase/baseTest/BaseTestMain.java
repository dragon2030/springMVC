package com.bigDragon.javase.baseTest;

import com.bigDragon.javase.baseTest.FactoryTest.FactoryTestMain;
import com.bigDragon.javase.baseTest.InnerClassTest.InnerClassTest;
import com.bigDragon.javase.baseTest.InnerClassTest.InnerClassTest2;
import com.bigDragon.javase.baseTest.InterfaceTest2.SubClassTest;
import com.bigDragon.javase.baseTest.interfaceTest.InterfaceTest;
import com.bigDragon.javase.baseTest.procyDemoMevie.ProxyTest;
import com.bigDragon.javase.reflect.StaticProxyTest;
import com.bigDragon.javase.reflect.jdkProxy.JdkProxy;

/**
 * @author bigDragon
 * @create 2021-04-12 16:50
 */
public class BaseTestMain {
    public static void main(String[] args) {
        //代码块
        new BlockTest();
        //代码块与构造器执行的先后顺序
        new Block_Son();
        //对属性的赋值位置的赋值先后顺序
        new OrderTest();
        //抽象类
        new AbstractTest();
        //抽象类的应用：模板方法的设计模式
        new TemplateTest();
        //接口的使用——JDK7及以前，只能定义全局常量和抽象方法
        new InterfaceTest();
        //接口的使用实例
        new USB_test();
        //设计模式：静态代理举例——电影院
        new ProxyTest();
        //设计模式：静态代理举例——耐克工厂
        new StaticProxyTest();
        //设计模式：jdk动态代理
        new JdkProxy();
        //设计模式：工厂模式
        new FactoryTestMain();
        //接口的使用——JDK8：除了定义全局常量和抽象方法之外，还可以定义静态方法、默认方法
        new SubClassTest();
        //类的内部成员之五：内部类——成员内部类
        new InnerClassTest();
        //局部内部类
        new InnerClassTest2();
    }
}
