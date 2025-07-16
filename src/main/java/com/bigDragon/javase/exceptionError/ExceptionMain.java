package com.bigDragon.javase.exceptionError;

/**
 * 异常处理
 * @author bigDragon
 * @create 2020-10-21 16:08
 */
public class ExceptionMain {
    public static void main(String[] args){
        //异常概述
        new ExceptionOverview();
        //异常处理的方式一：try-catch-finally的使用
        new TryCatchFinallyException();
        //异常处理的方式二：throws+异常类型
        new ThrowsException();
        //方法重写的规则之一：子类重写的方法抛出的异常类型不大于父类重写的方法抛出的异常类型
        new OverrideException();
        //手动生成异常
        new ManualException();
    }
    
    public void m1(){
        new RuntimeException("111");
    }
}
