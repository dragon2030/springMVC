package com.bigDragon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.common.CommonValue;
import com.bigDragon.common.DateUtil;
import com.bigDragon.javase.faseToObject.interfasce.B;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.DoubleToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bigDragon.*;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {

    public static void main(String[] args) {
        try {
            Main main = new Main();
            main.method();
        } catch (Exception e) {
            System.out.println(exceptionPrint(e));
            e.printStackTrace();

        }
    }

    public static String exceptionPrint(Throwable e){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        return exception;
    }


    public void method(){
        int i = 1;
        int i2 = 0;
        throw new RuntimeException("运行时异常");
//        System.out.println(i/i2);
    }

}
