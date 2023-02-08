package com.bigDragon.javase.reflect;

import com.bigDragon.javase.reflect.model.Person;
import com.bigDragon.javase.reflect.model.Student;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 获取运行时类的完整结构
 *
 * @author bigDragon
 * @create 2020-12-08 19:03
 */
public class FieldTest {
    public static void main(String[] args) {
        FieldTest fieldTest = new FieldTest();
        //获取属性结构
        fieldTest.test1();
        //权限修饰符、数据类型、变量名
        fieldTest.test2();
        //赋值和取值
        fieldTest.operateFieldValue();
    }

    /**
     * 获取属性结构
     * clazz.getFields()    获取当前运行时类及其父类中声明为public访问权限的属性
     * clazz.getDeclaredFields()    获取当前运行时类中声明的所有属性。（不包含父类的属性,但包含private等别的修饰符）
     */
    @Test
    public void test1() {
        Class clazz = Person.class;
        //获取属性结构
        //getFields():获取当前运行时类及其父类中声明为public访问权限的属性
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            System.out.println(f);
        }
        System.out.println("************************************");
        //getDeclaredFields():获取当前运行时类中声明的所有属性。（不包含父类的属性,但包含private等别的修饰符）
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            System.out.println(field);
            //System.out.println(field.getName());
        }
    }

    /**
     * 权限修饰符、数据类型、变量名
     * field.getModifiers() 权限修饰符
     * field.getType() 数据类型
     * field.getType().getSimpleName()用于判断是否这个类型
     * field.getName() 变量名
     */
    @Test
    public void test2() {
        Class clazz = Person.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            //1.权限修饰符
            int modifiers = field.getModifiers();
            System.out.print(Modifier.toString(modifiers) + "\t\t");
            //2.数据类型
            Class<?> type = field.getType();
            System.out.print(type.getName() + "\t\t");
            //3.变量名
            String name = field.getName();
            System.out.print(name);

            System.out.println();
        }
    }

    //field.getType().getSimpleName()用于判断是否这个类型
    @Test
    public void test111() {
        Class clazz = Person.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Objects.equals("name", field.getName())) {
                Class<?> type = field.getType();
                System.out.println(type);
                System.out.println(Objects.equals(type.getSimpleName(), "String"));
            }
        }
    }

    /**
     * 泛型
     * field.getGenericType()
     * ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]
     */
    @Test
    public void test3() {
        Class clazz = Person.class;
        //获取属性结构
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("所有属性列表");
        for (Field field : fields) {

            //获取属性类private List<Student> studentList;
            if (Objects.equals(field.getName(), "studentList")) {
                System.out.println("************************测试studentList**************************");
                Type genericType = field.getGenericType();
                System.out.println(genericType);
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
                System.out.println(actualTypeArgument);
            }
            //获取属性类private Student student;
            if (Objects.equals(field.getName(), "student")) {
                System.out.println("************************测试student**************************");
                Type genericType = field.getGenericType();
                System.out.println(genericType);
            }

            System.out.println(field);
        }

    }

    //赋值和取值
    @Test
    public void operateFieldValue() {
        try {
            //赋值
            Person person = new Person();
            Class clazz = Person.class;
            Field field = clazz.getDeclaredField("name");
            field.setAccessible(true);
            field.set(person, "HanMeimei");
            System.out.println(person);//Person(name=HanMeimei, age=0, id=0, studentList=null, student=null)
            //取值
            Field field2 = clazz.getDeclaredField("name");
            field2.setAccessible(true);
            String name = (String)field2.get(person);
            System.out.println(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
