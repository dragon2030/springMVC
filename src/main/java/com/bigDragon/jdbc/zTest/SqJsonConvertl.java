package com.bigDragon.jdbc.zTest;

import com.alibaba.fastjson.JSONArray;
import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import com.bigDragon.jdbc.dao.BaseDAO2;
import com.bigDragon.jdbc.util.JDBCUtils;
import com.bigDragon.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

@Slf4j
public class SqJsonConvertl {
    @Test
    public void m(){
        BaseDAO2 temporaryDto = new BaseDAO2<BsAddressTemporary>(){
            @Override
            public void insert(Connection conn, BsAddressTemporary addr) {
                String sql = "insert into bs_address_temporary(id,area_id,parent_id,addr_name)values(?,?,?,?)";
                update(conn, sql,"2",addr.getAreaId(),addr.getParentId(),addr.getAreaName());
            }
        };
//        BsAddressTemporaryDtoImpl temporaryDto = new BsAddressTemporaryDtoImpl();
        String url = "jdbc:mysql://192.168.5.150:8035/051001_online?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        String user = "xfuser1";
        String password = "Ihave0123&UPPERCASE";
        String driverClass = "com.mysql.jdbc.Driver";
        Connection conn = JDBCUtils.getConnection(user,password,url,driverClass);

        BsAddressTemporary bsAddressTemporary = new BsAddressTemporary();
        bsAddressTemporary.setAreaId("018001");
        bsAddressTemporary.setParentId("01");
        bsAddressTemporary.setAreaName("安和北区（D区）");

        temporaryDto.insert(conn,bsAddressTemporary);
    }

    @Test
    public void jsonConvert() throws InterruptedException {
        BaseDAO2 temporaryDto = new BaseDAO2<BsAddressTemporary>(){
            @Override
            public void insert(Connection conn, BsAddressTemporary addr) {
                String sql = "insert into bs_address_temporary(id,area_id,parent_id,addr_name)values(?,?,?,?)";
                update(conn, sql, SnowFlakeUtil.getSnowflakeStr(),addr.getAreaId(),addr.getParentId(),addr.getAreaName());
            }
        };
//        BsAddressTemporaryDtoImpl temporaryDto = new BsAddressTemporaryDtoImpl();
        String url = "jdbc:mysql://192.168.5.150:8035/051001_online?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        String user = "xfuser1";
        String password = "Ihave0123&UPPERCASE";
        String driverClass = "com.mysql.jdbc.Driver";
        Connection conn = JDBCUtils.getConnection(user,password,url,driverClass);

        String readeredTemplate = CommonUtil.readerTemplate("D:\\io_test\\sqlConvert\\json_date.txt");
        log.info("获取读取的导出json："+readeredTemplate);
        List<BsAddressTemporary> bsAddressTemporaries = JSONArray.parseArray(readeredTemplate, BsAddressTemporary.class);
        int executeNum=0;
        for(BsAddressTemporary bsAddressTemporary:bsAddressTemporaries){
//            bsAddressTemporary.setAreaId("018001");
//            bsAddressTemporary.setParentId("01");
//            bsAddressTemporary.setAreaName("安和北区（D区）");
            temporaryDto.insert(conn,bsAddressTemporary);

            log.info("插入条目："+ (++executeNum)+" 数据："+bsAddressTemporary);
            Thread.sleep(100);
        }
    }
}
