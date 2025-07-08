package com.bigDragon.testMain;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author: bigDragon
 * @create: 2025/6/25
 * @Description:
 */
public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3307/seata?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "123456";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("连接成功！");
            conn.close();
        } catch (Exception e) {
            System.out.println("连接失败：");
            e.printStackTrace();
        }
    }
}
