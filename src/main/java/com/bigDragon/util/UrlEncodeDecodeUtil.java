package com.bigDragon.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * url转义
 * @author: bigDragon
 * @create: 2022/10/19
 * @Description:
 */
public class UrlEncodeDecodeUtil {
    public static void main(String[] args) {

        String encodeStr = " ";
        String decodeStr="%20";
//        decodeStr+="biz_site_manage%2Csite_name%2Cid%2Csite_type+not+in+%28%274%27%2C%275%27%2C%277%27%29+and+del_flag+%3D+%270%27";

        UrlEncodeDecodeUtil main = new UrlEncodeDecodeUtil();

        String encodeUtf8 = main.urlEncodeUtf8(encodeStr);
        System.out.println(encodeUtf8);
        String decodeUtf8 = main.urlDecodeUtf8(decodeStr);
        System.out.println(decodeUtf8);
    }

    //url加密，utf-8
    public String urlEncodeUtf8(String url){
        String encode = null;
        try {
            encode = URLEncoder.encode(url,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    //url解密，utf-8
    public String urlDecodeUtf8(String url){
        String decode = null;
        try {
            decode = URLDecoder.decode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decode;
    }
}
