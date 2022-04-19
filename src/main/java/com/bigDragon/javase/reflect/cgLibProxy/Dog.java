package com.bigDragon.javase.reflect.cgLibProxy;

/**
 * 目标类
 * @author bigDragon
 * @create 2021-01-06 18:48
 */
public class Dog {
    final public void run(String name) {
        System.out.println("狗"+name+"----run");
    }

    public void eat() {
        System.out.println("狗----eat");
    }
}
