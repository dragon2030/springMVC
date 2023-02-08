package com.bigDragon.javase.ioStream.excel.poi.excel.dto;

import com.bigDragon.javase.ioStream.excel.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author: bigDragon
 * @create: 2022/1/13
 * @Description:
 */
public class AcHisTeamRptDto {
    @Excel(exportName = "业务团队",exportFieldWidth = 15)
    private String teamId;
    @Excel(exportName = "统计天数",exportFieldWidth = 15)
    private BigDecimal queryDts;
    @Excel(exportName = "上线人力",exportFieldWidth = 15)
    private BigDecimal workNum;
    @Excel(exportName = "成交率",exportFieldWidth = 15,exportConvertSign = 1)
    private BigDecimal closingRatioHF;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public BigDecimal getQueryDts() {
        return queryDts;
    }

    public void setQueryDts(BigDecimal queryDts) {
        this.queryDts = queryDts;
    }

    public BigDecimal getWorkNum() {
        return workNum;
    }

    public void setWorkNum(BigDecimal workNum) {
        this.workNum = workNum;
    }

    public BigDecimal getClosingRatioHF() {
        return closingRatioHF;
    }

    public void setClosingRatioHF(BigDecimal closingRatioHF) {
        this.closingRatioHF = closingRatioHF;
    }
    public String getClosingRatioHFConvert(BigDecimal closingRatioHF) {
        if(null == closingRatioHF){
            return "";
        }else if(closingRatioHF.compareTo(BigDecimal.ZERO) < 0){
            return "N/A";
        }else if(closingRatioHF.compareTo(BigDecimal.ZERO) == 0){
            return "0";
        }else {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(closingRatioHF.doubleValue() * 100) + "%";
        }
    }
    public void setClosingRatioHFConvert(String closingRatioHF) {
        //TODO 暂时省略
        this.closingRatioHF = new BigDecimal("0");
    }
}
