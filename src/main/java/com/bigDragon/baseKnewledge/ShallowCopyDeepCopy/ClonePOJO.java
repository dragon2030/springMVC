package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;


/**
 * 深拷贝浅拷贝学习POJO
 * @author bigDragon
 * @create 2020-09-19 10:11
 */
public class ClonePOJO {
    private String Str1;
    private int int1;
    private User user;

    public String getStr1() {
        return Str1;
    }

    public void setStr1(String str1) {
        Str1 = str1;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ClonePOJO"+this.hashCode()+"{" +
                "Str1='" + Str1 + '\'' +
                ", int1=" + int1 +
                ", user=" + user +
                '}';
    }
}
