package com.bigDragon.javase.Generics;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义泛型类方法
 * @author: bigDragon
 * @create: 2022/5/12
 * @Description:
 */
@Data
public class Order2 {

    String orderName;
    int orderId;



    //泛型方法：在方法中出现了泛型的结构，泛型参数与类的泛型参数没有任何关系。
    //泛型方法做属的类是不是泛型类都没有关系
    //泛型方法，可以声明为静态。原因：泛型参数时在调用方法时确定的，并非在实例化类时确定。
    //https://www.cnblogs.com/chen-z-w/p/15588284.html
    public <E> List<E> copyFromArrayToList(E[] arr){
        return new ArrayList<E>(Arrays.asList(arr));
    }

    public <T> T genCode() {
        Object object = new Object();
        return (T) object;
    }
}
