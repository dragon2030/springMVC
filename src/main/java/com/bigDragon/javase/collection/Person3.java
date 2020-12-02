package com.bigDragon.javase.collection;

/**
 * 继承Comparable接口，按照name属性进行比较
 * @author bigDragon
 * @create 2020-11-16 15:19
 */
public class Person3 implements Comparable<Person3>{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person3(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person3() {
    }

    @Override
    public String toString() {
        return "Person3{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * 泛型之前的方式
     * 按照姓名从小到大进行排序
     * 按照姓名从大到小进行排序,按照年龄从小到大排序
     * @param o
     * @return
     */
/*    @Override
    public int compareTo(Object o) {
        if (o instanceof Person3){
            Person3 person3 = (Person3)o;
            //一级排序
            //return this.name.compareTo(person3.name);
            //二级排序
            int compare = -this.name.compareTo(person3.name);
            if(compare != 0){
                return compare;
            }else {
                return Integer.compare(this.age,person3.age);
            }
        }else{
            throw new RuntimeException("输入类型不匹配");
        }
    }*/

    @Override
    public int compareTo(Person3 person3) {
        //一级排序
        return this.name.compareTo(person3.name);
    }
}
