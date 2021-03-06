package com.bigDragon.javase.Generics;

import org.junit.Test;

/**
 * 自定义泛型方法使用
 * @author bigDragon
 * @create 2020-11-24 14:43
 */
public class GenericTest1 {
    @Test
    public void test1(){
        //如果定义了泛型类，实例化没有指定类的泛型，则认为此泛型类型为Object类型
        //要求：如果定义了类带泛型，建议实例化时指明类的泛型
        Order order = new Order();
        order.setOrderT(123);
        order.setOrderT("123");

        //建议:实例化时指明类的泛型
        Order<String> order2 = new Order<>("11",101,"123");
    }
}
