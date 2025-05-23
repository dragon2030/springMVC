package com.bigDragon.lombok.printCase;

import com.alibaba.fastjson.JSON;

public class People3 {
    private String name;
    private Integer age;
    private String hobby;

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

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
