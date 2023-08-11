package com.bigDragon.javase.Annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 自定义注解
 * @author bigDragon
 * @create 2020-10-27 19:24
 */
//被 @Inherited 注解修饰的注解，如果作用于某个类上，其子类是可以继承的该注解的。反之，如果一个注解没有被 @Inherited注解所修饰，
// 那么他的作用范围只能是当前类，其子类是不能被继承的。
@Inherited
@Repeatable(RepeatableAnnotation2.class)//1.8之后的可重复的注解
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value() default "hello";
}
