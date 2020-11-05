package com.bigDragon.javase.exceptionError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手动生成异常
 * @author bigDragon
 * @create 2020-10-25 16:05
 */
public class ManualException {
    public static void main(String[] args){
        try {
            Student student = new Student();
            student.register("v");
            System.out.println(student.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Student{
    private String id;
    public void register(String id){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(id);
        if(m.matches()){
            this.id = id;
        }else {
            throw new RuntimeException("请输入正整数");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
