package com.bigDragon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties文件工具类
 * @author 1918
 */
public class PropertiesUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * 读取指定文件名的配置文件
	 */
	public static Properties getProperties(String fileName) {
		InputStream inputStream = null;
		Properties properties = null;
		try {
			inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("config/" + fileName);
			if (inputStream == null) {
				logger.warn("未找到名称为【{0}】的资源！", fileName);
				return null;
			}
			properties = new Properties();
			properties.load(inputStream);
		} catch (Exception e) {
			logger.error("读取properties文件发生异常，文件名【{0}】", fileName);
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("读取properties文件关闭输入流发生异常");
				}
			}
		}
		return properties;
	}

	/**
	 * 读取指定文件的指定属性值
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(String fileName, String key) {
		Properties properties = getProperties(fileName);
		if(null == properties) {
			return null;
		}
		
		try {
			return properties.getProperty(key);
		} catch (Exception e) {
			logger.error("读取properties文件属性发生异常，文件名【{0}】,属性值【{1}】", fileName, key);
		}
		return null;
	}
}
