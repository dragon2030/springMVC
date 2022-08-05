package com.bigDragon.demo.test.service.impl;

import com.bigDragon.demo.test.dao.UserDao;
import com.bigDragon.demo.test.entity.User;
import com.bigDragon.demo.test.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/7/14
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    UserDao userDao;

    @Override
    public List<User> selectAll(){
        List<User> listMap = userDao.selectAll();
        return listMap;
    }
}
