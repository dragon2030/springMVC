package com.bigDragon.javase.exceptionError;

/**
 * 自定义异常类
 *
 * 如何自定义异常类？
 * 1.继承现有的异常结构，RuntimeException、Exception
 * 2.提供全局常量：serialVersionUID
 * 3.提供重载的构造器
 * @author bigDragon
 * @create 2020-10-25 17:33
 */
public class MyException extends RuntimeException{
    static final long serialVersionUID = -7034897190745436939L;

    public MyException(){}

    public MyException(String message){
       super(message);
    }
}

class Main{
    public static void main(String[] args){
        throw new MyException("自定义异常类");
    }
}
