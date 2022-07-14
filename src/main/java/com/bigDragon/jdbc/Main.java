package com.bigDragon.jdbc;

import com.bigDragon.jdbc.preparedStatement.crud.CustomerForQuery;
import com.bigDragon.jdbc.preparedStatement.crud.OrderForQuery;
import com.bigDragon.jdbc.preparedStatement.crud.PreparedStatementQueryTest;
import com.bigDragon.jdbc.preparedStatement.crud.PreparedStatementUpdateTest;
import com.bigDragon.jdbc.statement.crud.StatementTest;
import com.bigDragon.jdbc.util.JDBCUtils;

/**
 * @author: bigDragon
 * @create: 2022/7/8
 * @Description:
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //加载驱动简历数据库连接的五个方式，递进，最后一种为最终方式
        new ConnectionTest();

        //Statement做增删改查操作，已弃用，因为sql注入等问题，被PreparedStatement替换
        new StatementTest();
        //PreparedStatement核心主体
        //工具类诶提供方法获取连接
        JDBCUtils.getConnection();
        //PreparedStatement增删改操作及PreparedStatement增删改通用方法
        new PreparedStatementUpdateTest().update("",new String());
        //3、PreparedStatement查询基PreparedStatement查询通用方法
        new CustomerForQuery();
        new OrderForQuery();
        new PreparedStatementQueryTest().getForList(Main.class,"",new String());


    }
}
