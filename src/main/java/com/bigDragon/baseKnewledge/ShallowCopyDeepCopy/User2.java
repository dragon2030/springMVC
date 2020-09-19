package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;

/**
 * @author bigDragon
 * @create 2020-09-19 13:19
 */
public class User2 implements Cloneable{
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
    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone(){
        //浅拷贝
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
