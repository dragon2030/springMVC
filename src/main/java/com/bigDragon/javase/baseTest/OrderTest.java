package com.bigDragon.javase.baseTest;

/**
 * 对属性的赋值位置的赋值先后顺序
 * @author bigDragon
 * @create 2021-04-12 20:11
 */
public class OrderTest {
    public static void main(String[] args) {
        System.out.println(new Order_test().orderId);//4
        System.out.println(new Order_test2().orderId);//3
    }
}
class Order_test{
    int orderId = 3;
    {
        orderId = 4;
    }
}
class Order_test2{
    {
        orderId = 4;
    }
    int orderId = 3;
}