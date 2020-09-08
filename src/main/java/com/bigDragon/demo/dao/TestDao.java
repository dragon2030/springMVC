package com.bigDragon.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bigDragon.demo.entity.User;

public interface TestDao {
	
	/**
	 * 插入用户数据
	 * @param user
	 * @return
	 */
	Integer saveUser(@Param("user") User user);
	
	/**
	 * 获取用户数据List<Map<String,Object>>
	 * @return
	 */
	List<Map<String,Object>> getUser();
	
	/**
	 * 获取用户数据List<User>
	 * @return
	 */
	List<User> getUserMsg();
}
