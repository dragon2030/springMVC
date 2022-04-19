package com.bigDragon.demo.test.entity;

import java.util.Date;

/**
 * 贪吃蛇死亡记录
 * @author bigDragon
 * @create 2020-10-10 15:16
 */
public class GluttonousSnakeLengthRecord implements Cloneable{
    private String id;          //每次记录的id（不重复）
    private int length;         //贪吃蛇最大长度
    private Date time;          //游戏时间
    private String colorFlag;      //颜色记录标志位（1为当前记录为当前产生的记录 记录到页面排行榜中）

    @Override
    public String toString() {
        return "GluttonousSnakeLengthRecord{" +
                "id='" + id + '\'' +
                ", length='" + length + '\'' +
                ", time=" + time +
                ", colorFlag=" + colorFlag +
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
            // 直接调用父类的clone()方法
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getColorFlag() {
        return colorFlag;
    }

    public void setColorFlag(String colorFlag) {
        this.colorFlag = colorFlag;
    }

}
