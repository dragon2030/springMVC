package com.bigDragon.javase.ioStream.caseRecord.i18n;

import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import com.bigDragon.util.HttpClientUtil;
import com.bigDragon.util.UuidUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

//单文本翻译-有道接口翻译
public class TranslateApi {
    public static void main(String[] args) {
        String originContent = CommonUtil.readerTemplate("D:\\io_test\\i18n\\originContent_enum.txt");
    }

    public static String translateApi(String enStr){
        //参数准备
        String APP_KEY = "00b1e87df393d72c";//应用ID
        String APP_SECRET = "vTlDm684mNatpTeMsnWbov2jp84iIKPL";//应用秘钥
        String q = "苹果成熟了，可以吃了";//输入翻译的文本
        String salt = UuidUtil.generate();
//        String input = content.length()<=20?content:content.substring(0,10)+content.length()+content.substring(content.length()-10);
        String curtime = new Date().getTime()/1000+"";

        String url = "https://openapi.youdao.com/api";
        StringBuffer body = new StringBuffer();
        body.append("q="+q);
        body.append("&from=zh-CHS");
        body.append("&to=en");
        body.append("&appKey="+APP_KEY);
        body.append("&salt="+salt );

        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        body.append("&sign="+ sign);
        body.append("&signType=v3");
        body.append("&curtime="+curtime);

//        System.out.println("url:" + url);
        System.out.println("body:" + body.toString());
        try {
            String response = HttpClientUtil.getInstance().sendHttpPost(url, body.toString());
            System.out.println("response:"+response);
            ObjectMapper mapper = new ObjectMapper();
            //可以遍历获取全部的值
            Map<String, Object> map = mapper.readValue(response, Map.class);
//            for(Map.Entry<String, Object> entrySet:map.entrySet()){
//                System.out.println(entrySet.getKey()+" : "+entrySet.getValue());
//            }
            String translationResult = map.get("translation").toString().replaceAll("\\[", "").replaceAll("\\]", "");
//            System.out.println(translationResult);
            return translationResult;
//            JsonNode rootNode = mapper.readTree(response);
//            JsonNode translationNode = rootNode.get("translation");
//            System.out.println(translationNode.asText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}

