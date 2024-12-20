package com.bigDragon.util;

import org.junit.Test;

import java.util.UUID;

/**
 * @author: bigDragon
 * @create: 2022/5/12
 * @Description:
 */
public class UuidUtil {
    public static String generateCompletion(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString;
    }
    public static String generateNoIntervalSymbol(){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String replaceAll = uuidString.replaceAll("-", "");
        return replaceAll;
    }
    public static String generate(){
        return generateNoIntervalSymbol();
    }

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            System.out.println(generateCompletion());
        }
        System.out.println("*****************************************");
        for(int i=0;i<10;i++){
            System.out.println(generateNoIntervalSymbol());
        }
    }
}
