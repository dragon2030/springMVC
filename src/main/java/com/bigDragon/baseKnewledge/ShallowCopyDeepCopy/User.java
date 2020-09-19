package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;

/**
 * @author bigDragon
 * @create 2020-09-19 11:22
 */
public class User {
    private String age;
    private String name;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User"+this.hashCode()+"{" +
                "age='" + age + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
