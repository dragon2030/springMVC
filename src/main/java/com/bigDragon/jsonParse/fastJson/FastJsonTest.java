package com.bigDragon.jsonParse.fastJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.demo.jsonParse.controller.JsonParseController;
import com.bigDragon.demo.test.entity.User;
import com.bigDragon.jsonParse.dto.SmsZX;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
public class FastJsonTest {

    public static void main(String[] args){
        FastJsonTest faskJsonTest=new FastJsonTest();

        //JSONArray
        //测试生成JSONArray字符串
        String jsonText=faskJsonTest.setJSONArray();
        System.out.println(jsonText);
        //测试获取JSONArray中某一值
        JSONArray jsonArray = JSON.parseArray(jsonText);
        JSONObject jsonObject=jsonArray.getJSONObject(0);
        System.out.println(jsonObject.get("a"));

        //JSONObject
        //测试生成JSONObject字符串
        String jsonText2=faskJsonTest.setJSONObject();
        //测试获取JSONObject中某一值
        JSONObject jsonObject2=JSONObject.parseObject(jsonText2);
        System.out.println(jsonObject2.get("a"));

        //用fastjson打印容器中的内容
        faskJsonTest.javaBeanToPrintStr();

        //JSONArray.parseArray(json,UpdateStateYQX.class);

        //@JsonFormat 处理前端传的时间格式字符串，到dto中的Date，vo返回的Date转换为前端需要回显的时间格式字符串
        new JsonParseController().jackson_jsonFormat_Annotation(null);
    }

    @Test
    public void test1(){
        String str="{\"code\":0,\"msg\":\"SUCCESS\",\"data\":\"[{\\\"content\\\":\\\"回复3\\\",\\\"moTime\\\":1607343727778,\\\"msgId\\\":\\\"2001183465308160\\\",\\\"phone\\\":\\\"15372050554\\\"},{\\\"content\\\":\\\"回复2\\\",\\\"moTime\\\":1607343727778,\\\"msgId\\\":\\\"2001183465308160\\\",\\\"phone\\\":\\\"15372050554\\\"},{\\\"content\\\":\\\"回复1\\\",\\\"moTime\\\":1607343727778,\\\"msgId\\\":\\\"2001183465308160\\\",\\\"phone\\\":\\\"15372050554\\\"}]\"}";
        JSONObject json=JSONObject.parseObject(str);
        List<SmsZX>list=JSONArray.parseArray(str, SmsZX.class);
        System.out.println(list);
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
    public String javaBeanToPrintStr(){
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
        return JSON.toJSONString(jsonObject);
    }

    //String转User
    @Test
    public void printStrToJavaBean1(){
        User user1=new User();
        user1.setAge("22");
        user1.setName("Jack");
        user1.setSexId("1");
        String jsonStr = JSON.toJSONString(user1);
        User user = JSON.parseObject(jsonStr, User.class);
        System.out.println(user);
    }
    //String转List<User>
    @Test
    public void printStrToJavaBean2(){
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
        String jsonStr = JSON.toJSONString(list);
        List<User> users = JSON.parseArray(jsonStr, User.class);
        System.out.println(users);
    }



}
