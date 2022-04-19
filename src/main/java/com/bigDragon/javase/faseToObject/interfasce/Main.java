package com.bigDragon.javase.faseToObject.interfasce;

/**
 * interface的default
 * @author bigDragon
 * @create 2021-01-11 15:32
 */
public class Main {
    public static void main(String[] args) {
        //B为A的实现类，B中没有实现方法时
        //直接调用
        A b = new B();
        b.a();//这是A
        //调用了A的default方法

        //C为A的实现类，C中有实现方法
        A c= new C();
        c.a();//这是C
        //调用了C的重写方法
    }
}
