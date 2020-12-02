package com.bigDragon.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bigDragon.demo.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TestDao extends Mapper<User> {
	
	/**
	 * 插入用户数据
	 * @param user
	 * @return
	 */
	Integer saveUser(@Param("user") User user);
	
	/**
	 * 获取用户数据List<map<String,Object>>
	 * @return
	 */
	List<Map<String,Object>> getUser();
	
	/**
	 * 获取用户数据List<User>
	 * @return
	 */
	List<User> getUserMsg();


	/**
	 * 数据处理
	 * @return
	 */
	Integer dataDispose(@Param("userNo") String userNo);
}
