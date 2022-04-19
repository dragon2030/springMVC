package com.bigDragon.javase.reflect.jdkProxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler实现类
 * @author bigDragon
 * @create 2021-01-19 17:00
 */
class MyInvocationHandler implements InvocationHandler {

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