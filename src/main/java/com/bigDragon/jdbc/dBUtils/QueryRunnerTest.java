package com.bigDragon.jdbc.dBUtils;

import com.bigDragon.jdbc.dto.Customer;
import com.bigDragon.jdbc.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 * 其核心源码等同于com.bigDragon.jdbc.dao.BaseDAO2,健壮性有所提升
 */
public class QueryRunnerTest {

	//测试插入
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			//使用Druid数据连接池获取一个数据库连接
			conn = JDBCUtils.getConnection_druid();
			String sql = "insert into customers(name,email,birth)values(?,?,?)";
			int insertCount = runner.update(conn, sql, "蔡徐坤","caixukun@126.com","1997-09-08");
			System.out.println("添加了" + insertCount + "条记录");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			JDBCUtils.closeResource(conn, null);
		}

	}

	//测试查询
	/*
	 * ResultSetHandler:结果集处理器
	 * 结果是多种多样的，ResultSetHandler接口提供了不同的处理器来应对不同的需求
	 * 此处使用的是实现类BeanHandler，用于封装表中的一条记录
	 */
	@Test
	public void testQuery1(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();
			String sql = "select id,name,email,birth from customers where id = ?";
			BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, handler, 23);
			System.out.println(customer);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}

	/*
	 * ResultSetHandler:结果集处理器
	 * 结果是多种多样的，ResultSetHandler接口提供了不同的处理器来应对不同的需求
	 * 此处使用的是实现类BeanHandler，用于封装表中的一条记录，用于封装表中的多条记录构成的集合。
	 */
	@Test
	public void testQuery2() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();
			String sql = "select id,name,email,birth from customers where id < ?";

			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);

			List<Customer> list = runner.query(conn, sql, handler, 23);
			list.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{

			JDBCUtils.closeResource(conn, null);
		}

	}

	/*
	 * MapHander:是ResultSetHandler接口的实现类，对应表中的一条记录。
	 * 将字段及相应字段的值作为map中的key和value
	 */
	@Test
	public void testQuery3(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();
			String sql = "select id,name,email,birth from customers where id = ?";
			MapHandler handler = new MapHandler();
			Map<String, Object> map = runner.query(conn, sql, handler, 23);
			System.out.println(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}

	/*
	 * MapListHander:是ResultSetHandler接口的实现类，对应表中的多条记录。
	 * 将字段及相应字段的值作为map中的key和value。将这些map添加到List中
	 */
	@Test
	public void testQuery4(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();
			String sql = "select id,name,email,birth from customers where id < ?";

			MapListHandler handler = new MapListHandler();
			List<Map<String, Object>> list = runner.query(conn, sql, handler, 23);
			list.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}
	/*
	 * ScalarHandler:用于查询特殊值
	 * 查询数量
	 */
	@Test
	public void testQuery5(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();

			String sql = "select count(*) from customers";

			ScalarHandler handler = new ScalarHandler();
			Long count = (Long) runner.query(conn, sql, handler);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}

	/*
	 * ScalarHandler:用于查询特殊值
	 * 查询日期
	 */
	@Test
	public void testQuery6(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();

			String sql = "select max(birth) from customers";

			ScalarHandler handler = new ScalarHandler();
			Date maxBirth = (Date) runner.query(conn, sql, handler);
			System.out.println(maxBirth);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}

	/*
	 * 自定义ResultSetHandler的实现类
	 */
	@Test
	public void testQuery7(){
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils.getConnection_druid();

			String sql = "select id,name,email,birth from customers where id = ?";
			ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>(){

				@Override
				public Customer handle(ResultSet rs) throws SQLException {
//					System.out.println("handle");
//					return null;

//					return new Customer(12, "成龙", "Jacky@126.com", new Date(234324234324L));

					if(rs.next()){
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String email = rs.getString("email");
						Date birth = rs.getDate("birth");
						Customer customer = new Customer(id, name, email, birth);
						return customer;
					}
					return null;

				}

			};
			Customer customer = runner.query(conn, sql, handler,23);
			System.out.println(customer);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, null);

		}

	}

}
