package com.bigDragon.javase.reflect.caseRecord.SyncFieldSpecialCharacterAppear;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/*********************************************************
 * <p>文件名称： SyncBaseHouseholdDTO
 * <p>公司名称：杭州先锋电子技术股份有限公司
 * <p>系统名称：智能燃气信息管理系统
 * <p>模块名称：org.jeecg.modules.biz.entity
 * <p>功能说明：用戶信息同步DTO
 * <p>开发人员：徐燚敏
 * <p>开发时间：2021-03-03 9:08
 * <p>修改记录：程序版本    修改日期   修改人员   修改单号   修改说明
 *********************************************************/
@Data
public class SyncBaseMeterDTO implements Serializable {
    private static final long serialVersionUID = 6232886853038985765L;
    /**
     * 用户集合
     */
    private List<SyncMeterDTO> meters;
    /**
     * 删除的用户户号集合
     */
    private  List<String> delMeterNums;

    public void changFieldsToNoBlank(){
        try {
            String replace_0=String.valueOf('\u0000');
            String replace_Emp="";
            for(SyncMeterDTO syncDTO : this.meters){
                Class clazz = syncDTO.getClass();
                Field[] declaredFields = clazz.getDeclaredFields();
                for(Field field : declaredFields){
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if(Objects.equals(type.getSimpleName(),"String")){
                        String name  = (String)field.get(syncDTO);
                        if(StringUtils.isNotBlank(name) && name.contains(replace_0)){
                            name = name.replace(replace_0,replace_Emp);
                            field.set(syncDTO,name);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("解决同步数据会带\\0的问题异常",e);
        }
    }

}

