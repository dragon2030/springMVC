package com.bigDragon.javase.Generics;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义泛型类
 *
 * @author bigDragon
 * @create 2020-11-24 13:49
 */
public class Order <T>{
    String orderName;
    int orderId;

    //类的内部结构就可以使用类的泛型
    T orderT;

    public Order() {
        //泛型参数的创建数组
        //编译出错
        //T[] arr = new T[10];
        //编译通过
        T[] arr = (T[]) new Object[10];
    }

    public Order(String orderName, int orderId, T orderT) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.orderT = orderT;
    }
    public T getOrderT() {
        return orderT;
    }

    public void setOrderT(T orderT) {
        this.orderT = orderT;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderName='" + orderName + '\'' +
                ", orderId=" + orderId +
                ", orderT=" + orderT +
                '}';
    }

    //泛型方法：在方法中出现了泛型的结构，泛型参数与类的泛型参数没有任何关系。
    //泛型方法做属的类是不是泛型类都没有关系
    //泛型方法，可以声明为静态。原因：泛型参数时在调用方法时确定的，并非在实例化类时确定。
    public static <E> List<E> copyFromArrayToList(E[] arr){
        return new ArrayList<E>(Arrays.asList(arr));
    }
}
