package com.bigDragon.javase.exceptionError;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 方法重写的规则之一：子类重写的方法抛出的异常类型不大于父类重写的方法抛出的异常类型
 * @author bigDragon
 * @create 2020-10-25 14:46
 */
public class OverrideException {
    public static void main(String[] args){
        OverrideException o  = new OverrideException();
        o.display(new subClass());
    }

    public void display(SuperClass superClass){
        try {
            superClass.method();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SuperClass{
    public void method() throws IOException {}
}

class subClass extends SuperClass{
    public void method() throws FileNotFoundException {}
}

