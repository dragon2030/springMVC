package com.bigDragon.lombok;

/**
 * @author: bigDragon
 * @create: 2022/12/20
 * @Description:
 */
public class Animal {
    private String id;
    private String name;
    private Integer age;
    private String masterName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", masterName='" + masterName + '\'' +
                '}';
    }
}
