package com.bigDragon.util;

import org.junit.Test;

import java.util.UUID;

/**
 * @author: bigDragon
 * @create: 2022/5/12
 * @Description:
 */
public class UuidUtil {
    public static String generate(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String uuidString = uuid.toString();
        String replaceAll = uuidString.replaceAll("-", "");
        System.out.println(replaceAll);
        System.out.println(replaceAll.length());
        return replaceAll;
    }

    public static void main(String[] args) {
        UuidUtil.generate();
    }
}