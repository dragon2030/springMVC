package com.bigDragon;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.common.CommonValue;
import com.bigDragon.javase.faseToObject.interfasce.B;
import com.bigDragon.util.SnowFlakeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.MessageFormat;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bigDragon.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {
    private static final ConcurrentHashMap<String, String> APP_CID_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        List<String> collect = strings.stream().collect(Collectors.toList());
        System.out.println(collect);
    }

}
