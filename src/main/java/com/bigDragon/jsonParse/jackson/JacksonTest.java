package com.bigDragon.jsonParse.jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.jsonParse.dto.Student;
import com.bigDragon.jsonParse.dto.Student2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
public class JacksonTest {
    public static void main(String[] args) {
        JacksonTest jacksonTest = new JacksonTest();
        //对象和json通过jackson转换
        jacksonTest.test1();
        //Jackson JsonNode类，是Jackson的json树模型
        jacksonTest.test20220411();
        //Jackson - 注解 @JsonInclude：忽略空字段
        jacksonTest.Jackson_JsonProperty();
    }

    /**
     * 对象和json通过jackson转换
     */
    @Test
    public void test1(){
        try {
            Student student = new Student("Jack",27);
            ObjectMapper objectMapper = new ObjectMapper();
            String studentValueAsString = objectMapper.writeValueAsString(student);
            System.out.println(studentValueAsString);
            Student studentReadValue = objectMapper.readValue(studentValueAsString, Student.class);
            System.out.println(studentReadValue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Jackson JsonNode类，完整路径为com.fasterxml.jackson.databind.JsonNode，是Jackson的json树模型(对象图模型)。Jackson能读JSON至JsonNode实例，写JsonNode到JSON
     * The Jackson JsonNode对象不可变，这意味着不能直接构建JsonNode实例的对象图，但你可以创建JsonNode 的子类ObjectNode实例的对象图。作为JsonNode 的子类，ObjectNode可以在任何使用了JsonNode之处使用
     * 参考文档
     * https://www.cnblogs.com/suizhikuo/p/14528502.html
     * 本文介绍了Jackson的JsonNode和ObjectNode两个类，前者是不可变的，一般用于读取。后者可变，一般用于创建Json对象图。
     * @throws IOException
     */
    @Test
    public void test20220411() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("mchid","1316723001")
                    .put("appid", "wx4ccbaf2192766766")
                    .put("description", "下单测试")
                    .put("notify_url", "https://test.innoveronline.com:16004/wwt/pay/notify_url/icRecharge/")
                    .put("out_trade_no", "2022022410115900001");
            rootNode.putObject("amount")
                    .put("total", 1);
            objectMapper.writeValue(bos, rootNode);
            System.out.println(bos);
            String parseStr = "{\"name\":\"Jack\",\"age\":27}";
            JsonNode jsonNode = objectMapper.readTree(parseStr);
            //从json中读JsonNode
            System.out.println(jsonNode.get("name").asText());
            //写JsonNode至json
            System.out.println(objectMapper.writeValueAsString(jsonNode));
            //获取JsonNode 字段
            JsonNode age = jsonNode.get("age");
            //使用at方法获取JsonNode字段
            String parseStr2 = "{\"student\":{\"age\":27,\"name\":\"Jack\"}}";
            JsonNode jsonNode2 = objectMapper.readTree(parseStr2);
            JsonNode nameNode = jsonNode2.at("/student/name");
            System.out.println(nameNode.asText());
            JsonNode ageNode = jsonNode2.at("/student/age");
            System.out.println(ageNode.asInt());
            //设置ObjectNode属性
            ObjectNode parentNode = objectMapper.createObjectNode();
            JsonNode childNode = objectMapper.readTree("{\"name\":\"Jack\",\"age\":27}");
            parentNode.set("child1", childNode);
            System.out.println(objectMapper.writeValueAsString(parentNode));
            //设置ObjectNode属性值为原始数据类型值
            ObjectNode originNode = objectMapper.createObjectNode();
            originNode.put("field1", "value1");
            originNode.put("field2", 123);
            originNode.put("field3", 999.999);
            System.out.println(objectMapper.writeValueAsString(originNode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Jackson - 注解 @JsonInclude：忽略空字段
     * 参考博客：https://blog.csdn.net/qq_42764468/article/details/124672619
     */
    @Test
    public void Jackson_JsonProperty(){
        try {
            Student2 student = new Student2("Jack",27);
            ObjectMapper objectMapper = new ObjectMapper();
            String studentValueAsString = objectMapper.writeValueAsString(student);
            System.out.println(studentValueAsString);//{"name":"Jack","age":27}
            Student studentReadValue = objectMapper.readValue(studentValueAsString, Student.class);
            System.out.println(studentReadValue);//Student(id=null, name=Jack, age=27)
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
