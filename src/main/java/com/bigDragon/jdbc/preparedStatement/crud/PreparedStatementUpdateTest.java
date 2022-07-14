package com.bigDragon.jdbc.preparedStatement.crud;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Test;

import com.bigDragon.jdbc.util.JDBCUtils;

/*
 * 使用PreparedStatement来替换Statement,实现对数据表的增删改操作
 * sql对数据操作主要为增删改查，此处演示”增删改"
 * testInsert()为新增一条数据的操作
 * testUpdate()为修改数据的操作
 *
 * 主要模式都为
 * 1.获取数据库的连接
 * 2.预编译sql语句，返回PreparedStatement的实例
 * 3.填充占位符
 * 4.执行
 * 5.资源的关闭
 * 其中只有23都改变，可以书写一个通用的增删改操作
 * 注意：PreparedStatement在创建时，已经写入sql语句，规定了sql的执行类型，而Statement还需后期执行sql
 */
public class PreparedStatementUpdateTest {

	// 向customers表中添加一条记录
	@Test
	public void testInsert() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1.读取配置文件中的4个基本信息
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

			Properties pros = new Properties();
			pros.load(is);

			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");

			// 2.加载驱动
			Class.forName(driverClass);

			// 3.获取连接
			conn = DriverManager.getConnection(url, user, password);

//		System.out.println(conn);

			//4.预编译sql语句，返回PreparedStatement的实例
			String sql = "insert into customers(name,email,birth)values(?,?,?)";//?:占位符
			/**
			 * 此处?叫发上为占位符，而泛型中的?叫法上为通配符
			 * 正因为此占位符，才能解决Statement的弊端，解决sql注入问题
			 */
			ps = conn.prepareStatement(sql);
			/**
			 * 5.填充占位符
			 * 注意：填充占位符时索引从1开始
			 * PreparedStatement.setString api说明
			 parameterIndex – the first parameter is 1, the second is 2, ...
			 */
			ps.setString(1, "哪吒");
			ps.setString(2, "nezha@gmail.com");
			/**
			 * 插入时间类型的占位符字段时，需要插入java.sql.Date
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));

			//6.执行操作
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			/**
			 * 7.资源的关闭
			 * 此处操作同与io流的操作
			 * try catch因为关闭操作可能会抛出异常
			 * if非空判断因为方式生成null对象，调用方法导致的空指针异常
			 */
			try {
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}
	//修改customers表的一条记录
	@Test
	public void testUpdate(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//1.获取数据库的连接
			conn = JDBCUtils.getConnection();
			//2.预编译sql语句，返回PreparedStatement的实例
			String sql = "update customers set name = ? where id = ?";

			/**
			 * 此处即为PreparedStatement区别于Statement的预编译
			 * PreparedStatement在创建时，已经写入sql语句，规定了sql的执行类型，而Statement还需后期执行sql
			 */
			//Statement statement = conn.createStatement();
			ps = conn.prepareStatement(sql);

			/**
			 * 	3.填充占位符
			 * 	可以直接用PreparedStatement.setObject()
			 * 	如此就不需要根据类型调用setString()、setInt()、setDate()等
			 */
			ps.setObject(1,"莫扎特");
			ps.setObject(2, 18);
			//4.执行
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//5.资源的关闭
			JDBCUtils.closeResource(conn, ps);

		}
	}


	/**
	 * 优化增删改
	 * 调用通用的增删改操作方法
	 * 执行增删改操作
	 */
	@Test
	public void testCommonUpdate(){
//		String sql = "delete from customers where id = ?";
//		update(sql,3);

		String sql = "update `order` set order_name = ? where order_id = ?";
		update(sql,"DD","2");

	}

	/**
	 * 优化增删改
	 * 通用的增删改操作
	 */
	public void update(String sql,Object ...args){//sql中占位符的个数与可变形参的长度相同！
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//1.获取数据库的连接
			conn = JDBCUtils.getConnection();
			//2.预编译sql语句，返回PreparedStatement的实例
			ps = conn.prepareStatement(sql);
			//3.填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);//小心参数声明错误！！
			}
			//4.执行
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//5.资源的关闭
			JDBCUtils.closeResource(conn, ps);

		}


	}






}
