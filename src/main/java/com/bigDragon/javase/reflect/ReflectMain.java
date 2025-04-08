package com.bigDragon.javase.reflect;

import com.bigDragon.javase.baseTest.procyDemoMevie.ProxyTest;
import com.bigDragon.javase.reflect.cgLibProxy.CgLibProxy;
import com.bigDragon.javase.reflect.jdkProxy.JdkProxy;

/**
 * 反射
 *
 * 一、JAVA反射机制概述
 *
 *      Reflection(反射)是被视为动态语言的关键，反射机制允许程序在执行期借助于Reflection API取得任何类的内部信息，并能直接
 *      操作任意对象的内部属性及方法。
 *
 *      加载完类之后，在堆内存的方法区中就产生了一个Class类型的对象（一个类只有一个Class对象），这个对象就包含了完整的类的结构
 *      信息。我们可以通过这个对象看到类的结构。这个对象就像一面镜子，透过这个镜子看到类的结构，所以，我们形象的称之为：反射。
 *
 *      正常方式：
 *      引入需要的“包类”名称 --> 通过new实例化 --> 取得实例化对象
 *      反射方式：
 *      实例化对象 --> getClass()方法 -->得到完整的"包类"名称
 *
 *      补充：动态语言 vs 静态语言
 *      1.动态语言：
 *      是一类在运行时可以改变其结构的语言：例如新的函数、对象、甚至diam可以被引进，已有的函数可以被删除或是其他结构删上的变化。
 *      通俗点说就是在运行时代码可以根据某些条件改变自身结构。
 *      主要动态语言：Object-C、C#，JavaScript、PHP、Python。
 *      2.静态语言
 *      与动态语言相对应，运行时结构不可变的语言就是静态语言。如Java、C、C++。
 *      注：Java不是动态语言，但Java可以称之为“准动态语言”。即Java有一定的动态性，我们可以利用反射机制、字节码操作获得类似
 *      动态语言的特性。Java的动态性让编程的时候更加灵活。
 *
 *      Java反射机制提供的功能
 *      >在运行时判断任意一个对象所属的类
 *      >在运行时构造任意一个类的对象
 *      >在运行时判断任意一个类所具有的成员变量和方法
 *      >在运行时获取泛型信息
 *      >在运行时调用任意一个对象的成员变量和方法
 *      >在运行时处理注解
 *      >生成动态代理
 *
 *      反射相关的主要API
 *      >java.lang.Class：代表一个类
 *      >java.lang.reflect.Method：代表类的方法
 *      >java.lang.reflect.Field：代表类的成员变量
 *      >java.lang.reflect.Constructor：代表类的构造器
 *
 * 二、理解Class类并获取Class实例
 * 三、类的加载与ClassLoader的理解
 * 四、创建运行时类的对象
 * 五、获取运行时类的完整结构
 * 六、调用运行时类的指定结构
 * 七、反射的应用：动态代理
 *
 * @author bigDragon
 * @create 2020-11-30 14:02
 */
public class ReflectMain {
    public static void main(String[] args){
        //测试反射的实体类
        new Person();

        //二、理解Class类并获取Class实例 反射之前对象的操作 vs 反射之后对象的操作
        new ReflectionTest();
        //三、类的加载与ClassLoader的理解
        new ClassLoaderTest();
        //四、创建运行时类的对象
        new NewInstanceTest();
        //五、获取运行时类的完整结构
        //获取运行时类的属性结构
        new FieldTest();
        //运行时类的方法结构
        new MethodTest();
        //运行时类的其他结构
        new OtherTest();
        //六、调用运行时类的指定结构(属性、方法、构造器)
        new ReflectionUsingTest();
        //七、反射的应用：动态代理
        //静态代理举例1
        new ProxyTest();
        //静态代理举例2
        new StaticProxyTest();
        //jdk 动态代理实例
        //在spring6项目中 com.atguigu.spring6.aop.example.main 有应用于日志打印更好的示例
        //生成的类名通常是类似$ProxyN这样的格式，通过ProxyGenerator生成字节码。
        new JdkProxy();
        //CGLIB 动态代理
        //类名通常会是TargetClass$$EnhancerByCGLIB$$加上随机字符串的形式。
        new CgLibProxy();
    }
}
