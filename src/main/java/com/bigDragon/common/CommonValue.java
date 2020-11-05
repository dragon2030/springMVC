/**
 * 
 */
package com.bigDragon.common;

import com.bigDragon.demo.entity.GluttonousSnakeLengthRecord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 处理系统要使用的公共数据和变量
 * @author: bigDragon
 * @date: 2020年8月14日
 * 
 */
public class CommonValue {
    public static List<GluttonousSnakeLengthRecord> snakeLengthRecords
            = new LinkedList<GluttonousSnakeLengthRecord>();            //贪吃蛇死亡记录链表
    public static final int snakeRankingLength = 10;                    //贪吃蛇死亡记录额定数量
}
