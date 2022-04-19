package com.bigDragon.javase.baseTest.InterfaceTest2;

import java.util.Collection;

/**
 * 接口的使用——JDK8：除了定义全局常量和抽象方法之外，还可以定义静态方法、默认方法
 * @author bigDragon
 * @create 2021-04-14 20:03
 */
public class SubClassTest {
    public static void main(String[] args) {
        SubClass subClass = new SubClass();

        //知识点1.接口中定义的静态方法，只能通过接口来调用,不可以通过实现类对象调用
        //subClass.method1();//编译出错
        CompareA.method1();
        /** Output:method1**/
        //知识点2：通过实现类的对象，可以调用接口中的默认方法
        subClass.method2();
        //如果实现类重写了接口中的默认方法，调用时，调用的是重写后的方法
        subClass.method3();
        /** Output:method2
          重写的method3**/
        //知识点3：如果子类（或实现类）继承的父类和实现的接口中声明了同名同参数的方法
        //那么子类在没有重写此方法的情况下，默认调用的是父类中的同名同参数的方法-->类优先原则（仅方法中）
        subClass.method4();
        /** ClassA_method4 **/
    }
}
class SubClass extends ClassA implements CompareA{
    @Override
    public void method2(){
        System.out.println("method2");
    }
    public void method3(){
        System.out.println("重写的method3");
    }
}

class SubClass2 implements CompareA,CompareB{
    public void method4(){
        System.out.println("CompareA_method4");
    }
}
//如何在子类（或实现类）的方法中调用父类、接口中被重写的方法
class SubClass3 extends ClassA implements CompareA,CompareB {
    public void method4(){
        System.out.println("SubClass3.method4");
    }
    public void myMethod(){
        method4();//调用自己定义的重写方法
        super.method4();//调用的是父类中声明的方法
        //调用接口中的默认方法
        CompareA.super.method4();
        CompareB.super.method4();
    }

    public static void main(String[] args) {
        new SubClass3().myMethod();
    }
}
interface CompareA {
    //静态方法
    public static void method1(){
        System.out.println("method1");
    }
    //默认方法
    public default void method2(){
        System.out.println("method2");
    }
    //没有public时，默认省略了public
    default void method3(){
        System.out.println("method3");
    }

    default void method4(){
        System.out.println("CompareA_method4");
    }
}
interface CompareB{
    default void method4(){
        System.out.println("CompareB_method4");
    }
}
class ClassA{
    public void method4(){
        System.out.println("ClassA_method4");
    }
}
