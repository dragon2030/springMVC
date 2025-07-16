package com.bigDragon.javase.reflect.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author: bigDragon
 * @create: 2025/2/5
 * @Description:
 */
public class ProxyFactory2 {
    //调用此方法，返回一个代理类的对象，解决问题一。
    private Object target;
    
    public ProxyFactory2(Object target) {
        this.target = target;
    }
    public Object getProxyInstance(){
        //返回代理类实例
        /**
         * newProxyInstance()：创建一个代理实例
         * 其中有三个参数：
         * 1、classLoader：加载动态生成的代理类的类加载器
         * 2、interfaces：目标对象实现的所有接口的class对象所组成的数组
         * 3、invocationHandler：设置代理对象实现目标对象方法的过程，即代理类中如何重写接口中的抽象方法(例如在加减乘除之前或之后做的输出操作)
         */
        //参数一
        ClassLoader classLoader = target.getClass().getClassLoader();
        //参数二
        Class<?>[] interfaces = target.getClass().getInterfaces();
        //参数三 使用匿名内部类替代新建接口
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /**
                 * proxy：代理对象
                 * method：代理对象需要实现的方法，即其中需要重写的方法
                 * args：method所对应方法的参数
                 */
                Object result = null;
                try {
                    System.out.println("[动态代理][日志] "+method.getName()+"，参数："+ Arrays.toString(args));
                    result = method.invoke(target, args);
                    System.out.println("[动态代理][日志] "+method.getName()+"，结果："+ result);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("[动态代理][日志] "+method.getName()+"，异常："+e.getMessage());
                } finally {
                    System.out.println("[动态代理][日志] "+method.getName()+"，方法执行完毕");
                }
                return result;
            }
        };
        return Proxy.newProxyInstance(
                classLoader,
                interfaces
                ,invocationHandler);
    }
}
