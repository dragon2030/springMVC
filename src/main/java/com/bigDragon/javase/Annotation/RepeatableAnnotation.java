package com.bigDragon.javase.Annotation;

/**
 * jdk 8.0 之前的可重复注解实现方式
 * @author bigDragon
 * @create 2020-10-28 13:52
 */
public @interface RepeatableAnnotation {
    MyAnnotation[] value();
}
