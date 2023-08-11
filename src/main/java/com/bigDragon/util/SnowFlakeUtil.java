package com.bigDragon.util;

import cn.hutool.core.util.IdUtil;

/**
 * @author: bigDragon
 * @create: 2022/5/13
 * @Description:
 */
public class SnowFlakeUtil {


    /**
     * 雪花算法生成全局唯一id。数据中心和终端id为1
     *
     * @return
     */
    public static String getSnowflakeStr(){
        return IdUtil.getSnowflake(1,1).nextIdStr();
    }

    public static void main(String[] args) {
        for (int i = 0;i < 10;i++){
            String snowflakeStr = getSnowflakeStr();
            System.out.println(snowflakeStr);
            //System.out.println(snowflakeStr.len@gth());
        }
    }
}
