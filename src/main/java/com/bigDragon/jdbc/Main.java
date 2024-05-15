package com.bigDragon.jdbc;

import com.bigDragon.jdbc.dBUtils.QueryRunnerTest;
import com.bigDragon.jdbc.dao.BaseDAO;
import com.bigDragon.jdbc.dao.CustomerDAOImpl;
import com.bigDragon.jdbc.dao.CustomerDAOImpl2;
import com.bigDragon.jdbc.dao.junit.CustomerDAOImplTest;
import com.bigDragon.jdbc.dateSource.C3P0Test;
import com.bigDragon.jdbc.dateSource.DBCPTest;
import com.bigDragon.jdbc.dateSource.DruidTest;
import com.bigDragon.jdbc.preparedStatement.crud.CustomerForQuery;
import com.bigDragon.jdbc.preparedStatement.crud.OrderForQuery;
import com.bigDragon.jdbc.preparedStatement.crud.PreparedStatementQueryTest;
import com.bigDragon.jdbc.preparedStatement.crud.PreparedStatementUpdateTest;
import com.bigDragon.jdbc.preparedStatement.practice.BatchInsertTest;
import com.bigDragon.jdbc.preparedStatement.practice.BlobTest;
import com.bigDragon.jdbc.statement.crud.StatementTest;
import com.bigDragon.jdbc.transactionTest.TransactionTest;
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

        //除了解决statement的拼串、sgl问题之外，Preparedstatement的好处：1、操作Blbb的数据 2、更高效的批量操作
        new BlobTest();
        new BatchInsertTest();
        // JDBC事务处理：1、基础的手动提交开启事务，提交数据与错误回滚，关闭手动提交 2、测试jdbc控制打印/设置隔离级别
        new TransactionTest();

        //通用的dao层父类方法
        BaseDAO.getInstance();
        //Customer表的dto类接口类/接口实现类/测试类
        Class.forName("com.bigDragon.jdbc.dao.CustomerDAO");
        new CustomerDAOImpl();
        new CustomerDAOImplTest();

        //BaseDAO2为BaseDAO升级——通过泛型类和反射，解决了调用方法时还需要传入对象关系映射类
        Class.forName("com.bigDragon.jdbc.dao.BaseDAO2");
        new CustomerDAOImpl2();

        //数据库连接池
        //C3P0数据源
        new C3P0Test();
        JDBCUtils.getConnection_c3p0();
        //DBCP数据源
        new DBCPTest();
        JDBCUtils.getConnection_dbcp();
        //Druid数据源
        new DruidTest();
        JDBCUtils.getConnection_druid();

         //commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
         //其核心源码等同于com.bigDragon.jdbc.dao.BaseDAO2,健壮性有所提升
        new QueryRunnerTest();
        //DbUtil关闭连接
        JDBCUtils.closeResource1(null,null,null);
    }


}
