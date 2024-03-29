package com.bigDragon.demo.test.service.impl;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bigDragon.demo.test.dao.TestDao;
import com.bigDragon.demo.test.entity.User;
import com.bigDragon.demo.test.service.TestService;

import javax.annotation.Resource;

@Service
@Transactional(propagation= Propagation.REQUIRED)
@Slf4j
public class TestServiceImpl implements TestService{
	private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
	/**
	 * @Resource的作用相当于@Autowired，只不过@Autowired按byType自动注入，
	 * 而@Resource默认按 byName自动注入罢了。@Resource有两个属性是比较重要的，
	 * 分是name和type，Spring将@Resource注解的name属性解析为bean的名字，
	 * 而type属性则解析为bean的类型。所以如果使用name属性，则使用byName的自动注入策略，
	 * 而使用type属性时则使用byType自动注入策略。如果既不指定name也不指定type属性，
	 * 这时将通过反射机制使用byName自动注入策略。 @Resource装配顺序
	 *
	 *1.如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常
	 *2.
	 *如果指定了name，则从上下文中查找名称（id）匹配的bean进行装配，找不到则抛出异常
	 *3.
	 *如果指定了type，则从上下文中找到类型匹配的唯一bean进行装配，找不到或者找到多个，都会抛出异常
	 *4.
	 *如果既没有指定name，又没有指定type，则自动按照byName方式进行装配；如果没有匹配，则回退为一个原始类型进行匹配，如果匹配则自动装配；
	 */

	@Resource
	TestDao testDao;

	@Override
	public int saveUser(User user){
		int num=0;
		try{
			num=testDao.saveUser(user);
//			List<String>list=new ArrayList<String>();
//			list.get(0);
		}catch(Exception e){
			num=999;
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public List<Map<String,Object>> getUser(){
		List<Map<String,Object>> listMap=testDao.getUser();
		return listMap;
	}

	@Override
	public Integer dataDispose(String userNo){
		return testDao.dataDispose(userNo);
	}

	@Override
	public Integer dataDispose2(List<String> list){
		int successNum = 0;
		for(int i=0;i<list.size();i++){
			int result = testDao.dataDispose(list.get(i));
			if(result == 1){
				successNum++;
			}
			logger.info("当前插入数为:"+(i+1));
		}
		return successNum;
	}

}
