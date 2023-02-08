package com.bigDragon.javase.reflect.caseRecord.SyncFieldSpecialCharacterAppear;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/*********************************************************
 * <p>文件名称： SyncHouseholdDTO
 * <p>公司名称：杭州先锋电子技术股份有限公司
 * <p>系统名称：智能燃气信息管理系统
 * <p>模块名称：org.jeecg.modules.biz.entity
 * <p>功能说明：
 * <p>开发人员：徐燚敏
 * <p>开发时间：2021-03-03 9:15
 * <p>修改记录：程序版本    修改日期   修改人员   修改单号   修改说明
 *********************************************************/
@Data
public class SyncMeterDTO implements Serializable {
    private static final long serialVersionUID = 1783979978377412492L;
    /**
     * 户号
     */
    private String householdNum;
    /**
     * 表号
     */
    private String meterNum;
    /**
     * 结算方式（1-系统计费；2-表端计费）
     */
    private Integer billType;

    /**
     * 价格id
     */
    private String priceId;

    /**
     * 当前价格
     */
    private BigDecimal currentPrice;


    /**
     * 条码
     */
    private String barcode;
    /**
     * 计量类型（1-气量表；2-金额表）
     */
    private Integer measureType;
    /**
     * 是否远传 0：否 ；1：是
     */
    private Integer isAuto;
    /**
     * 是否支持IC卡：0-否；1-是
     */
    private Integer isIcCard;
    /**
     * 是否已被认证：0-未认证；1-已认证（如果表具类型为非智能远传表类型，则表具信息在录入时默认为已认证）
     */
    private Integer isIdentificated;
    /**
     * 欠费关阀阈值字段（默认0）
     */
    private BigDecimal shutdownBalance;
    /**
     * 表具对应的协议编号
     */
    private String licenceCode;
    /**
     * 厂商
     */
    private String vendorCode;
    /**
     * 表具累计用气量
     */
    private BigDecimal totalDosage;
    /**
     * 底数
     */
    private BigDecimal bottomDosage;
    /**
     * 安装人员
     */
    private String installStaff;
    /**
     * 表具产品品类code
     */
    private String productModelCode;
    /**
     * 出厂时间
     */
    private Date productTime;
    /**
     * 安装时间
     */
    private Date installTime;
    /**
     * 安装位置
     */
    private String installSite;
    /**
     * 有效期
     */
    private Integer period;
    /**
     * 表具通气状态（0-未通气；1-已通气）
     */
    private Integer dosageState;
    /**
     * 通气时间
     */
    private Date dosageTime;
    /**
     * 卡片累购气量
     */
    private BigDecimal cardBuyGas;
    /**
     * 卡片购气次数
     */
    private Integer cardCount;
    /**
     * 补卡次数
     */
    private Integer mendCardCount;
    /**
     * 远程购气次数
     */
    private Integer remoteCount;
    /**
     * 表具状态：0-库存表；1-已预装；2-已开户；3-已用气；4-已拆除
     */
    private Integer lifeStatus;
    /**
     * 0-阀开 1-阀关；2-异常
     */
    private Integer valveState;
    /**
     * 最近上报时间
     */
    private Date recentUpTime;
    /**
     * 开户时间
     */
    private Date openTime;
    /**
     * 销户时间
     */
    private Date cancelTime;
    /**
     * 安检时间
     */
    private Date checkTime;
    /**
     * 抄表时间
     */
    private Date readTime;
    /**
     * 金额表表端累计购气金额
     */
    private BigDecimal totalBuyMoney;
    /**
     * 金额表表端累计用气金额
     */
    private BigDecimal totalUseMoney;

    /**
     * 表端剩余值（根据计量类型判断单位）
     */
    private BigDecimal surplusValue;

    /**
     * 气量表表端累计购气量
     */
    private BigDecimal totalBuyGas;

    /**
     * 远程调价次数
     */
    private Integer priceChangeNum;
    /**
     * 表具状态：0-停用；1-启用
     */
    private Integer meterStatus;
    /**
     * 表类型：1：民用膜式表 2：工业膜式表 3：工业流量计
     */
    private Integer gmType;
    /**
     * sim卡号
     */
    private String simCode;
    /**
     * 生产令
     */
    private String productionOrder;
    /**
     * 生产日期
     */
    private Date manufacturDate;
    /**
     * 进气方向
     */
    private String intakeDirection;
    /**
     * imei号码
     */
    private String imei;
    /**
     * iccid号码
     */
    private String iccid;
    /**
     * ip号
     */
    private String ipCode;
    /**
     * 端口号
     */
    private String portCode;
    /**
     * 通信制式：1-NB, 2-2G, 3-4G, 4-LoRa, 5-FSK, 6-手工
     */
    private Integer telecomType;

    /**
     * 第三方平台Id
     */
    private String thirdPlatId;


    /**
     * 组网状态:0->未组网;1->已组网
     */
    private Integer networkStatus = 0 ;

    /**
     * 运行状态:0->离线 ;1->在线
     */
    private Integer operatingStatus = 0 ;

    /**
     * 供电类型:0->锂电 ;1->市电
     */
    private Integer powerType =  0 ;

    /**
     * 上报类型:1->日上报 ;1->周上报;3->月上报;4->间隔上报
     */
    private Integer reportType = 1;

    /**
     * 累计补量气量
     */
    private BigDecimal sumMendAmount;

    /**
     * 累计补量金额
     */
    private BigDecimal sumMendMoney;

    /**
     * 是否余额同步 0-否  1-是
     */
    private Integer isSynchBalance;
}

