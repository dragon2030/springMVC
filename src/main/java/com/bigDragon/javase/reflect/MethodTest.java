package com.bigDragon.javase.reflect;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.bigDragon.javase.reflect.model.Person;

/**
 * 运行时类的方法结构
 * 
 * @author bigDragon
 * @create 2020-12-08 20:01
 */
public class MethodTest {
    public static void main(String[] args){
        MethodTest methodTest = new MethodTest();
        //获取方法结构
        methodTest.test1();
        // @Xxx 权限修饰符 返回值类型 方法名(参数类型 形参名1，...) throws XxxException{}
        methodTest.test2();

    }
    /**
     * 获取方法结构
     */
    @Test
    public void test1(){
        Class<Person> personClass = Person.class;
        //getMethods():获取当前运行时类及其所有父类中声明为public权限的方法
        Method[] methods = personClass.getMethods();
        for(Method method : methods){
            System.out.println(method);
        }
        //getDeclaredMethods():获取当前运行时类中声明的所有方法。（不包含父类中声明的方法）
        Method[] declaredMethods = personClass.getDeclaredMethods();
        for(Method method : declaredMethods){
            System.out.println(method);
        }
    }

    /**
     * @Xxx
     * 权限修饰符 返回值类型 方法名(参数类型 形参名1，...) throws XxxException{}
     */
    @Test
    public void test2(){
        Class<Person> personClass = Person.class;
        Method[] declaredMethod = personClass.getDeclaredMethods();
        for (Method method : declaredMethod){
            System.out.println("methodName:"+method.getName());
            //1.获取方法声明的注解
            Annotation[] annotations = method.getAnnotations();
            for(Annotation a : annotations){
                System.out.println("annotations:"+a + "\t");
            }

            //2.权限修饰符
            System.out.println("权限修饰符:"+Modifier.toString(method.getModifiers()) + "\t");

            //3.权限修饰符
            System.out.println("权限修饰符:"+method.getReturnType().getName() + "\t");

            //4.方法名
            System.out.println("方法名:"+method.getName() + "\t");

            //5.形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            if( parameterTypes.length > 0){
                System.out.print("形参列表:(");
                for(int i = 0;i < parameterTypes.length;i++){
                    if(i == parameterTypes.length-1){
                        System.out.print(parameterTypes[i].getName() + " args_" + i);
                        break;
                    }
                    System.out.print(parameterTypes[i].getName() + " args_" + i + ",");
                }
                System.out.print(")");
            }

            //6.获取的异常
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            if(exceptionTypes.length > 0){
                System.out.println();
                System.out.print("获取的异常:throws ");
                for(int i = 0;i < exceptionTypes.length;i++){
                    if(i == exceptionTypes.length-1){
                        System.out.print(exceptionTypes[i].getName() + " args_" + i);
                        break;
                    }
                    System.out.print(exceptionTypes[i].getName()+",");
                }
            }
            System.out.println();
            System.out.println("************************************");
        }
    }
}
