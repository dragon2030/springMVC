package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;

/**
 * 浅拷贝
 * @author bigDragon
 * @create 2020-09-19 11:26
 */
public class ClonePOJO2 implements Cloneable{

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
        return "ClonePOJO"+this.hashCode()+"{" +
                "Str1='" + Str1 + '\'' +
                ", int1=" + int1 +
                ", user=" + user +
                '}';
    }
}
