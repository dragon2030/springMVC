package com.bigDragon.demo.service;
import java.util.List;
import java.util.Map;

import com.bigDragon.demo.entity.User;


public interface TestService {

	/**
	 * 插入用户数据
	 * @param user
	 * @return
	 */
	int saveUser(User user);

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

	/**
	 * 数据处理
	 * @return
	 */
	Integer dataDispose(String userNo);

	/**
	 * 数据处理2
	 * @return
	 */
	Integer dataDispose2(List<String> list);
}
