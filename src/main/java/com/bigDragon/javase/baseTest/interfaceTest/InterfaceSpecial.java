package com.bigDragon.javase.baseTest.interfaceTest;

/**
 * @author bigDragon
 * @create 2021-04-15 10:13
 */
public class InterfaceSpecial implements InterfaceA{
    public void test1(){
        System.out.println(id);//编译出错,找不到id的属性，必须得用“接口名.”的方式获取全局常量
        System.out.println(InterfaceA.id);
        /**1001**/
    }

    public static void main(String[] args) {
        InterfaceSpecial interfaceSpecial = new InterfaceSpecial();
        interfaceSpecial.test1();
    }

    @Override
    public void show() {

    }
}
class InterfaceSpecial2 extends ClassA implements InterfaceA{

    public void test1(){
        //System.out.println(this.id);//Reference to 'id' is ambiguous, both 'ClassA.id' and 'InterfaceA.id' match
        //编译错误：因为接口和类是同级的,编译器会出错
        System.out.println(super.id);//调用父类ClassA的属性
        System.out.println(InterfaceA.id);//调用实现类InterfaceA的属性
        show();
    }
    //@Override
    //public void show(){
    //    System.out.println("重写的show方法");//此时show既是重写父类的同名方法，也实现了接口同名方法
    //}

    public static void main(String[] args) {
        InterfaceSpecial2 interfaceSpecial2 = new InterfaceSpecial2();
        interfaceSpecial2.test1();
    }
}
interface InterfaceA{
    int id = 1001;
    void show();
}
class ClassA{
    int id = 1003;
    public void show(){
        System.out.println("ClassA");
    }
}