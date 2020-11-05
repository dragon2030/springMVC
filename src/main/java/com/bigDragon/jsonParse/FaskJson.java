/**
 * 
 */
package com.bigDragon.jsonParse;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.demo.entity.User;

/**
 * alibaba fastjson 
 * @author: bigDragon
 * @date: 2020年8月13日
 * 
 */
public class FaskJson {
	
	public static void main(String[] args){
		FaskJson faskJson=new FaskJson();
		
		//测试生成JSONArray字符串
		String jsonText=faskJson.setJSONArray();
		//System.out.println(jsonText);
		//测试获取JSONArray中某一值
		JSONArray jsonArray = JSON.parseArray(jsonText);
		JSONObject jsonObject=jsonArray.getJSONObject(0);
		//System.out.println(jsonObject.get("a"));
		
		//测试生成JSONObject字符串
		String jsonText2=faskJson.setJSONObject();
		//测试获取JSONObject中某一值
		JSONObject jsonObject2=JSONObject.parseObject(jsonText2);
		System.out.println(jsonObject2.get("a"));
		//用fastjson打印容器中的内容
		//faskJson.jsonPrint();
		
	}
	
	/**
	 * 测试生成JSONArray字符串
	 * @return
	 */
	public String setJSONArray(){
		JSONArray jsonArray=new JSONArray();
		for(int i = 0 ;i < 5 ;i++){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("a", i+"1");
			jsonObject.put("b", i+"2");
			jsonObject.put("c", i+"3");
			jsonArray.add(jsonObject);
		}
		return JSON.toJSONString(jsonArray);
	}
	
	/**
	 * 测试生成JSONObject字符串
	 * @return
	 */
	public String setJSONObject(){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("a", "1");
		jsonObject.put("b", "2");
		jsonObject.put("c", "3");
		return JSON.toJSONString(jsonObject);
	}
	
	/**
	 * 用fastjson打印容器中的内容
	 */
	public void jsonPrint(){
		List<User> list=new ArrayList<User>();
		User user1=new User();
		user1.setAge("22");
		user1.setName("Jack");
		user1.setSexId("1");
		list.add(user1);
		User user2=new User();
		user2.setAge("23");
		user2.setName("Sam");
		user2.setSexId("2");
		list.add(user2);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("list", list);
		System.out.println(JSON.toJSONString(jsonObject));
	}
}
