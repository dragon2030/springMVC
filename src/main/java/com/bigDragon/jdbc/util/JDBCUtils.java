package com.bigDragon.jdbc.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @Description 操作数据库的工具类
 * @author shkstart Email:shkstart@126.com
 * @version
 * @date 上午9:10:02
 *
 */
public class JDBCUtils {

	/**
	 *
	 * @Description 获取数据库的连接
	 * @author shkstart
	 * @date 上午9:11:23
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			// 1.读取配置文件中的4个基本信息
			//ClassLoader.getSystemClassLoader()与this.getClass().getClassLoader()一样，此处都可获得系统类加载器
			//InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	public static Connection getConnection(String user,String password,String url,String driverClass){
		Connection conn = null;
		try {
			// 2.加载驱动
			Class.forName(driverClass);

			// 3.获取连接
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	/**
	 *
	 * @Description 关闭连接和Statement的操作
	 * @author shkstart
	 * @date 上午9:12:40
	 * @param conn
	 * @param ps
	 */
	@SuppressWarnings("all")
	public static void closeResource(Connection conn,Statement ps){
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 重点：所有的方法形参都是接口，体现了面向接口编程，不出现任何第三方的api
	 * @Description 关闭资源操作
	 * @author shkstart
	 * @date 上午10:21:15
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	@SuppressWarnings("all")
	public static void closeResource(Connection conn,Statement ps,ResultSet rs){
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 *
	 * @Description 使用C3P0的数据库连接池技术
	 * @author shkstart
	 * @date 下午3:01:25
	 * @return
	 * @throws SQLException
	 */
	//数据库连接池只需提供一个即可。
	private static ComboPooledDataSource source_c3p0 = new ComboPooledDataSource("hellc3p0");
	public static Connection getConnection_c3p0() throws SQLException{
		Connection conn = source_c3p0.getConnection();

		return conn;
	}

	/**
	 *
	 * @Description 使用DBCP数据库连接池技术获取数据库连接
	 * @author shkstart
	 * @date 下午3:35:25
	 * @return
	 * @throws Exception
	 */
	//创建一个DBCP数据库连接池
	private static DataSource source_dbcp;
//	static{
//		try {
//			Properties pros = new Properties();
//			FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
//			pros.load(is);
//			source_dbcp = BasicDataSourceFactory.createDataSource(pros);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public static Connection getConnection_dbcp() throws Exception{

		Connection conn = source_dbcp.getConnection();

		return conn;
	}

	/**
	 * 使用Druid数据库连接池技术
	 */
	private static DataSource source_druid;
	static{
		try {
			Properties pros = new Properties();

			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

			pros.load(is);

			source_druid = DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection_druid() throws SQLException{

		Connection conn = source_druid.getConnection();
		return conn;
	}


	/**
	 *
	 * @Description 使用dbutils.jar中提供的DbUtils工具类，实现资源的关闭
	 * @author shkstart
	 * @date 下午4:53:09
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResource1(Connection conn,Statement ps,ResultSet rs){
//		try {
//			DbUtils.close(conn);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(ps);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(ps);
		DbUtils.closeQuietly(rs);
	}
}
