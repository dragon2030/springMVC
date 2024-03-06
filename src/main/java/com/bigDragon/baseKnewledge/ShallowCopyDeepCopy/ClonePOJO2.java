package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 浅拷贝
 * @author bigDragon
 * @create 2020-09-19 11:26
 */
public class ClonePOJO2 implements Cloneable{

    private String Str1;
    private int int1;
    private User user;

    private Date date1;

    private BigDecimal decimal1;



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

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public BigDecimal getDecimal1() {
        return decimal1;
    }

    public void setDecimal1(BigDecimal decimal1) {
        this.decimal1 = decimal1;
    }

    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone(){
        //浅拷贝
        try {
            // 直接调用父类的clone()方法
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "ClonePOJO2{" +
                "Str1='" + Str1 + '\'' +
                ", int1=" + int1 +
                ", user=" + user +
                ", date1=" + date1 +
                ", decimal1=" + decimal1 +
                '}';
    }
}
