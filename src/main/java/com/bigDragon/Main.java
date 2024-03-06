package com.bigDragon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.common.CommonValue;
import com.bigDragon.demo.test.entity.User;
import com.bigDragon.javase.faseToObject.interfasce.B;
import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import com.bigDragon.javase.ioStream.excel.poi.excel.common.CommonConstant;
import com.bigDragon.testMain.Student;
import com.bigDragon.util.HttpClientUtil;
import com.bigDragon.util.SnowFlakeUtil;
import com.fasterxml.jackson.core.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.omg.CORBA.SystemException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
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
@Slf4j
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = "#entity";
        if(text.indexOf("#")>=0 || text.indexOf("=")==-1){
            System.out.println(1);
        }

    }
}
