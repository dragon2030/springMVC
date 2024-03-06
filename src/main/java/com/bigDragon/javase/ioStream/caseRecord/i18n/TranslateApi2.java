package com.bigDragon.javase.ioStream.caseRecord.i18n;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

//多文本翻译-有道接口翻译
public class TranslateApi2 {
    private static Logger logger = LoggerFactory.getLogger(TranslateApi2.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/v2/api";

    private static final String APP_KEY = "00b1e87df393d72c";

    private static final String APP_SECRET = "vTlDm684mNatpTeMsnWbov2jp84iIKPL";

    //配置文件map  配置文件变量key-中文需要翻译内容value
    static Map<String, String> fileContentMap = new HashMap<>();//翻译中只用到了value

    //翻译结果map  中文需要翻译内容key-英文翻译后内容value
    static Map<String, String> translateMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        //获取中文配置文件文本，组成配置文件map  配置文件变量key-中文需要翻译内容value
        String originContent = CommonUtil.readerTemplate("D:\\io_test\\i18n\\chineseText.txt");
        TranslateApi2.getChineseText(originContent);
        System.out.println("********************************************************");
        //将文本中午部分组成字符串数组，传入doMultiTranslate方法
        Collection<String> needTranslate = fileContentMap.values();
        String[] needTranslateArray = needTranslate.toArray(new String[needTranslate.size()]);
        String response = TranslateApi2.doMultiTranslate(needTranslateArray);
        System.out.println("********************************************************");
        //doMultiTranslate方法返回结果解析，组成翻译结果map  中文需要翻译内容key-英文翻译后内容value
        responseConvert(response);
        System.out.println("********************************************************");
        //生成配置新文件，将每个配置文件变量key->中文需要翻译内容value->英文翻译后内容value
        String newProperties = generateNewProperties(originContent);
        CommonUtil.writerOut(newProperties,"D:\\io_test\\i18n\\afterTranslateText.txt");
    }
    public static String doMultiTranslate(String[] qArray) {
        try {
            Map<String, String> params = new HashMap<String, String>();
//            String[] qArray = {"待输入的文字1", "待输入的文字2", "待输入的文字3"};
            String salt = String.valueOf(System.currentTimeMillis());
            params.put("from", "zh-CHS");
            params.put("to", "en");
            params.put("signType", "v3");
            String curtime = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("curtime", curtime);
            String signStr = APP_KEY + truncate(qArray) + salt + curtime + APP_SECRET;
            String sign = getDigest(signStr);
            params.put("appKey", APP_KEY);
            params.put("salt", salt);
            params.put("sign", sign);
//        params.put("vocabId", "您的用户词表ID");
            /** 处理结果 */
            String string = requestForHttp(YOUDAO_URL, params, qArray);
            return string;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, String> responseConvert(String json){
//        Map<String, String> stringStringHashMap = new HashMap<>();
//        String json = "{\"translateResults\":[{\"query\":\"待输入的文字1\",\"translation\":\"Text to be entered 1\",\"type\":\"zh-CHS2en\"},{\"query\":\"待输入的文字2\",\"translation\":\"Text to be entered 2\",\"type\":\"zh-CHS2en\"},{\"query\":\"待输入的文字3\",\"translation\":\"Text to be entered 3\",\"type\":\"zh-CHS2en\"}],\"requestId\":\"32ed1f27-37da-4830-ad18-5602341761be\",\"errorCode\":\"0\",\"l\":\"zh-CHS2en\"}\n";
        JSONObject jsonObject = JSONObject.parseObject(json);
        String jsonArrayStr = jsonObject.get("translateResults").toString();
        JSONArray jsonArray = JSONObject.parseArray(jsonArrayStr);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject innerJSONObject = jsonArray.getJSONObject(i);
            String key = innerJSONObject.get("query").toString();
            String value = innerJSONObject.get("translation").toString();
            translateMap.put(key,value);
            System.out.println("translateMap "+key+":"+value);
        }
        return translateMap;
    }
    public static String generateNewProperties(String originContent){
        String[] split = originContent.split("\r\n");//按照换行符进行逐行分割
        StringBuilder newPropertiesBuilder = new StringBuilder();
        for (String text : split) {
            StringBuilder newPropertiesLine = new StringBuilder();
            if(text.indexOf("#")>=0 || text.indexOf("=")==-1){
                newPropertiesLine.append(text).append("\r\n");
            }else{
                String[] split1 = text.split("=");
                String key = split1[0];
                String value = split1[1];
                newPropertiesLine.append(key).append("=");
                if(StringUtils.isNotBlank(value) && translateMap.containsKey(value)){
                    String translateValue = translateMap.get(value);
                    newPropertiesLine.append(translateValue);
                }else{
                    newPropertiesLine.append("[ERROR]翻译出错，系統原因导致不能翻译转换!!!!");
                }
                newPropertiesLine.append("\r\n");
            }
            System.out.println("新的新的配置文件写入数据："+newPropertiesLine);
            newPropertiesBuilder.append(newPropertiesLine);
        }
        return newPropertiesBuilder.toString();
    }
    //获取文件中的数据每行按照=分割，如果没有=就跳过 # 跳过
    public static void getChineseText(String originContent){
//        Map<String, String> fileContentMap = new HashMap<>();
//        String originContent = CommonUtil.readerTemplate("D:\\io_test\\i18n\\chineseText.txt");
        String[] split = originContent.split("\r\n");//按照换行符进行逐行分割

        for (String text : split) {
            if(text.indexOf("#")>=0 || text.indexOf("=")==-1){continue;}
            String[] split1 = text.split("=");
            String key = split1[0];
            String value =  split1[1];
            fileContentMap.put(key,value);
            System.out.println("fileContentMap "+key+":"+value);
        }
    }
    public static String requestForHttp(String url, Map<String, String> params, String[] qArray) throws IOException {

        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        for (int i = 0; i < qArray.length; i++) {
            paramsList.add(new BasicNameValuePair("q", qArray[i]));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            System.out.println("Content-Type:" + contentType[0].getValue());
            /** 响应不是音频流，直接显示结果 */
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
//            logger.info(json);
            System.out.println("接口返回翻译结果："+json);
            return json;
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                logger.info("## release resouce error ##" + e);
            }
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

    public static String truncate(String[] qArray) {
        if (qArray == null) {
            return null;
        }
        String batchQStr = String.join("", qArray);
        int len = batchQStr.length();
        return len <= 20 ? batchQStr : (batchQStr.substring(0, 10) + len + batchQStr.substring(len - 10, len));
    }
}




