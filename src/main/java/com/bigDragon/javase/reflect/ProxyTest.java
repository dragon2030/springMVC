package com.bigDragon.javase.reflect;

import org.aopalliance.intercept.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理的举例
 *
 * @author bigDragon
 * @create 2020-12-14 10:53
 */

interface Human{
    String getBelief();

    void eat(String food);
}
//被代理类
class SuperMan implements Human{

    @Override
    public String getBelief() {
        return "I believe i can fly!";
    }

    @Override
    public void eat(String food) {
        System.out.println("我喜欢吃"+food);
    }
}

class HumanUtil{
    public void method1(){
        System.out.println("******************通用方法一********************");
    }
    public void method2(){
        System.out.println("******************通用方法二********************");
    }
}

/*
要想实现动态代理：需要解决的问题？
问题一：如何根据加载到内存汇总的被代理类，动态的创建一个代理类及其对象
问题二：当通过代理类的对象调用方法a时，如何动态的调用被代理类的同名方法a
 */
class  ProxyFactory{
    //调用此方法，返回一个代理类的对象，解决问题一。
    public static Object getProxyInstance(Object obj){
        MyInvocationHandler handler = new MyInvocationHandler();

        handler.bind(obj);

        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces()
        ,handler);
    }
}
class MyInvocationHandler implements InvocationHandler{

    private  Object obj;    //需要使用被代理类的对象进行赋值

    public void bind(Object obj){
        this.obj = obj;
    }

    //当我们通过代理类的对象，调用方法a时，就会自动调用如下方法：invoke（）；
    //将被代理类要执行的方法a功能声明在invoke()方法中
    //proxy:代理类对象，method代理类对象调用的方法，args代理类对象调用方法所需参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        HumanUtil humanUtil = new HumanUtil();

        humanUtil.method1();
        //method:即为代理类对象调用的方法，此方法也就作为被代理类对象调用的方法
        //obj：被代理类的对象
        Object returnValue = method.invoke(obj, args);

        humanUtil.method2();
        //上述方法的返回值就当做当前类invoke()的返回值
        return returnValue;
    }
}
public class ProxyTest {
    public static void main(String[] args) {
        SuperMan superMan = new SuperMan();
        //proxyInstance:代理类的对象
        Human proxyInstance = (Human)ProxyFactory.getProxyInstance(superMan);
        //当通过代理类对象调用方法时，会自动的调用被代理类中同名方法
        String belief = proxyInstance.getBelief();
        System.out.println(belief);
        proxyInstance.eat("四川麻辣烫");

        System.out.println("****************************");

        NikeClothFactory nikeClothFactory = new NikeClothFactory();

        ClothFactory proxyClothFactory = (ClothFactory)ProxyFactory.getProxyInstance(nikeClothFactory);

        proxyClothFactory.produceCloth();
    }
}