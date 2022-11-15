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
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.bigDragon.*;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String s1="INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ('{0}', f6817f48af4fb3af11b9e8bf182f618b, 1588070063412510721, NULL, 2022-11-15 13:14:57, 115.236.0.2);\n" +
                "INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ({1}, f6817f48af4fb3af11b9e8bf182f618b, 1588070497451671554, NULL, 2022-11-15 13:14:57, 115.236.0.2);\n" +
                "INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ({2}, f6817f48af4fb3af11b9e8bf182f618b, 1588070852365287426, NULL, 2022-11-15 13:14:57, 115.236.0.2);\n" +
                "INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ({3}, f6817f48af4fb3af11b9e8bf182f618b, 1588071450938605570, NULL, 2022-11-15 13:14:57, 115.236.0.2);\n" +
                "INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ({4}, f6817f48af4fb3af11b9e8bf182f618b, 1588072096341327874, NULL, 2022-11-15 13:14:57, 115.236.0.2);\n" +
                "INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`) VALUES ({5}, f6817f48af4fb3af11b9e8bf182f618b, 1588072897579220994, NULL, 2022-11-15 13:14:57, 115.236.0.2);";
        String snowflakeStr0 = SnowFlakeUtil.getSnowflakeStr();
        String snowflakeStr1 = SnowFlakeUtil.getSnowflakeStr();
        String snowflakeStr2 = SnowFlakeUtil.getSnowflakeStr();
        String snowflakeStr3 = SnowFlakeUtil.getSnowflakeStr();
        String snowflakeStr4 = SnowFlakeUtil.getSnowflakeStr();
        String snowflakeStr5 = SnowFlakeUtil.getSnowflakeStr();
        String s2 = MessageFormat.format(s1,snowflakeStr0,snowflakeStr1,snowflakeStr2,
                snowflakeStr3,snowflakeStr4,snowflakeStr5);
        System.out.println(s2);
    }
}
