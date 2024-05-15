package com.bigDragon.jdbc.dao;

import com.bigDragon.jdbc.dto.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * BaseDAO2为BaseDAO升级——通过泛型类和反射，解决了调用方法时还需要传入对象关系映射类
 */
public class CustomerDAOImpl2 extends BaseDAO2<Customer> implements CustomerDAO{


	@Override
	public void insert(Connection conn, Customer cust) {
		String sql = "insert into customers(name,email,birth)values(?,?,?)";
		update(conn, sql,cust.getName(),cust.getEmail(),cust.getBirth());
	}

	@Override
	public void deleteById(Connection conn, int id) {
		String sql = "delete from customers where id = ?";
		update(conn, sql, id);
	}

	@Override
	public void update(Connection conn, Customer cust) {
		String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
		update(conn, sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = getInstance(conn, sql,id);
		return customer;
	}

	@Override
	public List<Customer> getAll(Connection conn) {
		String sql = "select id,name,email,birth from customers";
		List<Customer> list = getForList(conn, sql);
		return list;
	}

	@Override
	public Long getCount(Connection conn) {
		String sql = "select count(*) from customers";
		return getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		String sql = "select max(birth) from customers";
		return getValue(conn, sql);
	}

}
