package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 深拷贝
 * @author bigDragon
 * @create 2020-09-19 13:18
 */
public class ClonePojo3 implements Cloneable{

    private String Str1;
    private int int1;

    private User2 user2;

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

    public User2 getUser2() {
        return user2;
    }

    public void setUser2(User2 user2) {
        this.user2 = user2;
    }

    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone(){
        try {
            ClonePojo3 clonePojo3 = (ClonePojo3)super.clone();
            clonePojo3.user2 = (User2)user2.clone();
            return clonePojo3;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String toString() {
        return "ClonePOJO"+this.hashCode()+"{" +
                "Str1='" + Str1 + '\'' +
                ", int1=" + int1 +
                ", user2=" + user2 +
                '}';
    }
}
