package com.bigDragon.javase.reflect.jdkProxy;

import java.lang.reflect.Proxy;

/**
 * 代理类工厂
 * @author bigDragon
 * @create 2021-01-19 16:59
 */
class  ProxyFactory{
    //调用此方法，返回一个代理类的对象，解决问题一。
    public static Object getProxyInstance(Object obj){
        MyInvocationHandler handler = new MyInvocationHandler();
        //绑定被代理类对象到代理类工厂的obj属性中
        handler.bind(obj);
        //返回代理类实例
        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces()
                ,handler);
    }
}
