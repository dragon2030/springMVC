package com.bigDragon.javase.Annotation;

/**
 * 自定义注解调用
 * @author bigDragon
 * @create 2020-10-28 11:19
 */
public class MyAnnotationClass {
    public static void main(String[] args){
        MyAnnotationClass myAnnotation2 = new MyAnnotationClass();
        myAnnotation2.method1();
    }
    /**
     * 自定义注解
     */
    @MyAnnotation(value = "12")
    public void method1(){}
}
