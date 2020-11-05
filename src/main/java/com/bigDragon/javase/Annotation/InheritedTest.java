package com.bigDragon.javase.Annotation;

import java.lang.annotation.Annotation;

/**
 * @Inherited 测试类
 * @author bigDragon
 * @create 2020-10-28 11:22
 */
public class InheritedTest extends Parent{
    public static void main(String[] args){
        Annotation[] annotations=Son.class.getAnnotations();
        for (Annotation annotation:annotations){
            System.out.println(annotation);
        }
    }
}
@MyAnnotation
class Parent{
}


class Son extends Parent{}