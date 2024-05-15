package com.bigDragon.jdbc.dateSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接池测试
 * c3p0数据源
 */
public class C3P0Test {
	//方式一：c3p0数据源连接方式 及 preparedStatement复习测试
	@Test
	public void testGetConnection() throws Exception{
		//获取c3p0数据库连接池
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" );
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/dragon?useSSL=false&useUnicode=true&autoReconnect=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai" );
		cpds.setUser("root");
		cpds.setPassword("root");
		//通过设置相关的参数，对数据库连接池进行管理：
		//设置初始时数据库连接池中的连接数
		cpds.setInitialPoolSize(10);

		Connection conn = cpds.getConnection();
		System.out.println(conn);

//		PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE id=? ");
//		preparedStatement.setObject(1,1);
//		ResultSet resultSet = preparedStatement.executeQuery();
//		while (resultSet.next()){
//			int id = resultSet.getInt(1);
//			String name = resultSet.getString(2);
//			int age = resultSet.getInt(3);
//			String peopleDes = resultSet.getString(4);
//			String sexId = resultSet.getString(5);
//			System.out.println("id = " + id + ",name = " + name + ",age = " + age + ",peopleDes = " + peopleDes+",sexId = " + sexId);
//		}
//		JDBCUtils.closeResource(conn, preparedStatement, resultSet);

//		PreparedStatement preparedStatement = conn.prepareStatement("SELECT count(*) FROM user ");
//		ResultSet resultSet = preparedStatement.executeQuery();
//		if (resultSet.next()){
//			long count = resultSet.getLong(1);
//			System.out.println(count);
//		}
//		JDBCUtils.closeResource(conn, preparedStatement, resultSet);

//		PreparedStatement preparedStatement = conn.prepareStatement("UPDATE `user` SET name='Mike' WHERE id=?");
//		preparedStatement.setObject(1,3);
//		preparedStatement.executeUpdate();
//		JDBCUtils.closeResource(conn, preparedStatement);

		//销毁c3p0数据库连接池
		DataSources.destroy( cpds );
	}
	//方式二：使用配置文件
	@Test
	public void testGetConnection1() throws SQLException{
		ComboPooledDataSource cpds = new ComboPooledDataSource("hellc3p0");
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}
}
