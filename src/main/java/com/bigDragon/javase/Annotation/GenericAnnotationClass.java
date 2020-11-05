package com.bigDragon.javase.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型注解
 * @author bigDragon
 * @create 2020-10-29 9:58
 */
public class GenericAnnotationClass<@GenericAnnotation(value = "123") T> {
    public static void main(String[] args){
        GenericAnnotationClass genericAnnotationClass = new GenericAnnotationClass();
        //类型注解中TYPE_PARAMETER,TYPE_USE
        genericAnnotationClass.show();
    }

    /**
     * 类型注解中TYPE_PARAMETER,TYPE_USE
     * @throws @GenericAnnotation RuntimeException
     */
    public void show() throws @GenericAnnotation RuntimeException{
        List<@GenericAnnotation String> list =new ArrayList<>();
        int num = (@GenericAnnotation int)10L;
    }
}
