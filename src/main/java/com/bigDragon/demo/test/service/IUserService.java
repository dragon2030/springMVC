package com.bigDragon.demo.test.service;

import com.bigDragon.demo.test.entity.User;

import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/7/14
 * @Description:
 */
public interface IUserService {
    /**
     * 获取用户数据List<User>
     * @return
     */
    List<User> selectAll();
}
