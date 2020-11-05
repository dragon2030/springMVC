package com.bigDragon.javase.Annotation;

/**
 * 可重复注解测试类
 * @author bigDragon
 * @create 2020-10-28 13:53
 */
public class RepeatableAnnotationClass {
    public static void main(String[] args){
        RepeatableAnnotationClass repeatableAnnotationClass = new RepeatableAnnotationClass();
        //jdk1.8之前实现可重复注解
        repeatableAnnotationClass.method1();
        //jdk1.8实现可重复注解
        repeatableAnnotationClass.method2();
    }

    /**
     * jdk1.8之前实现可重复注解
     */
    @RepeatableAnnotation({@MyAnnotation(value = "123"),@MyAnnotation(value = "456")})
    public void method1(){
    }

    /**
     * jdk1.8实现可重复注解
     */
    @MyAnnotation(value = "123")
    @MyAnnotation(value = "456")
    public void method2(){
    }
}
