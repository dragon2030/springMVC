package com.bigDragon.javase.ioStream.caseRecord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestIO {
    public static void main(String[] args) {
        SubCompanyNeedExecuteSqlUtil main = new SubCompanyNeedExecuteSqlUtil();
        String allCompany = main.readerAllCompany("D:\\io_test\\all_subcompany.txt");
        System.out.println("获取所有分公司:\n"+allCompany);
        String excludeCompanyCode = "ibs_000000,ibs_001001,ibs_022001,ibs_035001,ibs_075001,ibs_077001,ibs_078002,ibs_101902,ibs_102101,ibs_102901,ibs_103403,ibs_104201,ibs_104401,ibs_104601,ibs_104901,ibs_105102,ibs_105401,ibs_105501,ibs_106901,ibs_107501,ibs_107601,ibs_108601,ibs_109101,ibs_109401,ibs_109501,ibs_109801,ibs_109901,ibs_110101,ibs_110201,ibs_110301,ibs_110701,ibs_110901,ibs_111101,ibs_111201,ibs_111301,ibs_111401,ibs_111601,ibs_111701,ibs_111801,ibs_111901,ibs_112201,ibs_117701,ibs_117801,ibs_145001,ibs_171001,ibs_186012,ibs_303002,ibs_343001,ibs_343006,ibs_378001,ibs_411001,ibs_418001,ibs_418002,ibs_418003,ibs_470001,ibs_470002,ibs_470004,ibs_470006,ibs_470007,ibs_470008,ibs_530001,ibs_590001,ibs_611130,ibs_611134,ibs_617001,ibs_631001,ibs_759001,ibs_815001,ibs_861001,ibs_907003";
        System.out.println("获取排除不需要生成的数据库名称:\n"+excludeCompanyCode);
        System.out.println("******************************************************************");

        //去除分公司行间空行
        String noLineBreak = allCompany.replace("\n","");
        noLineBreak = noLineBreak.replace("\r","");
        noLineBreak = noLineBreak.replace("\\","");
//        noLineBreak = noLineBreak.replace(" ","");
        System.out.println("去除分公司行间空行:\n"+noLineBreak);
        String[] split = noLineBreak.split(",");
        System.out.println(split.length);

        String[] excludeSplit = excludeCompanyCode.split(",");
        Set<String> excludeCmpanySet = new HashSet<>();
        for(String excludeCompany : excludeSplit){
            excludeCmpanySet.add(excludeCompany.replace("ibs_",""));
        }
        System.out.println("排除不需要生成的companyCode:\n"+excludeCmpanySet);
        System.out.println(excludeCmpanySet.size());
        System.out.println("******************************************************************");

        StringBuilder stringBuilder = new StringBuilder();
        for(String subCompany : split){
            if(excludeCmpanySet.contains(subCompany)){continue;}
            String operationSubCompany = subCompany.trim();
            stringBuilder.append(operationSubCompany).append(",");
        }
        if(stringBuilder.length()>0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        System.out.println(stringBuilder.toString());
        System.out.println(stringBuilder.toString().split(",").length);
        //187-70  118 这个生成的118个公司都是配置文件的，已经删除有加异名索引
    }
}
