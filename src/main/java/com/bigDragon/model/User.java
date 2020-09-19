package com.bigDragon.model;

import java.io.Serializable;

/**
 * 对应mysql数据库 user表
 * @author bigDragon
 * @create 2020-05-25 13:24
 */
public class User implements Serializable{
	
	public static final long serialVersionUID = 4235234634535L;
	
    private int userId;
    private String age;
    private String name;
    private String peopleDes;
    private String sexId;		//1为男 2为女


    @Override
    public String toString() {
        return "User"+this.hashCode()+"{" +
                "userId=" + userId +
                ", age='" + age + '\'' +
                ", name='" + name + '\'' +
                ", peopleDes='" + peopleDes + '\'' +
                ", sexId='" + sexId + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getPeopleDes() {
        return peopleDes;
    }

    public void setPeopleDes(String peopleDes) {
        this.peopleDes = peopleDes;
    }

    public String getSexId() {
        return sexId;
    }

    public void setSexId(String sexId) {
        this.sexId = sexId;
    }
}
