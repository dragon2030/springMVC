package com.bigDragon.javase.reflect;

import com.bigDragon.javase.reflect.model.Person;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 获取运行时类的完整结构
 *
 * @author bigDragon
 * @create 2020-12-08 19:03
 */
public class FieldTest {
    public static void main(String[] args){
        FieldTest fieldTest = new FieldTest();
        //获取属性结构
        fieldTest.test1();
        //权限修饰符、数据类型、变量名
        fieldTest.test2();
    }
    /**
     * 获取属性结构
     */
    @Test
    public void test1(){
        Class clazz = Person.class;
        //获取属性结构
        //getFields():获取当前运行时类及其父类中声明为public访问权限的属性
        Field[] fields = clazz.getFields();
        for(Field f : fields){
            System.out.println(f);
        }
        System.out.println("************************************");
        //getDeclaredFields():获取当前运行时类中声明的所有属性。（不包含父类的属性）
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field:declaredFields){
            System.out.println(field);
            //System.out.println(field.getName());
        }
    }

    /**
     * 权限修饰符、数据类型、变量名
     */
    @Test
    public void test2(){
        Class clazz = Person.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field:declaredFields){
            //1.权限修饰符
            int modifiers = field.getModifiers();
            System.out.print(Modifier.toString(modifiers)+"\t");
            //2.数据类型
            Class<?> type = field.getType();
            System.out.print(type.getName()+"\t");
            //3.变量名
            String name = field.getName();
            System.out.print(name);

            System.out.println();
        }
    }
}
