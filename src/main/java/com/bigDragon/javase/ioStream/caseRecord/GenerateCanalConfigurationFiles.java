package com.bigDragon.javase.ioStream.caseRecord;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 *      生成canal配置文件
 */
public class GenerateCanalConfigurationFiles {
    //读数据
    //替换字段处理数据
    //生成文件
    public static void main(String[] args) throws InterruptedException, IOException {
        GenerateCanalConfigurationFiles main = new GenerateCanalConfigurationFiles();

        //遍历获取文件夹下的文件通过获取的文件决定选择，集团-》子公司，子公司-》集团
        String templatePath = "D:\\documentOperation\\txt\\template";//rdb解压后的模板
        String outPutPath = "D:\\documentOperation\\txt\\upload";//需要上传的文件
        String outSpecificPrefix = "merge";//输出文件前缀内容，用于区分不同集团-子公司库关系，如merge_biz_addr_470004_470000.yml
        String separator = "_";//文件名中的分隔符
        String groupCompanyCode = "470000";//集团公司code
        String subCompanyCode = "470004";//子公司code
        //application.yml新增的库属性，也是每个表.yml文中需要修改的熟悉
        String groupCompany_source = "hh_gt_merge_source_470000";//集团公司的源
        String subCompany_source = "hh_gt_merge_source_470004";//子公司的源
        String groupCompany_target = "hh_gt_merge_source_outer_470000";//集团公司的同步配置
        String subCompany_target = "hh_gt_merge_source_outer_470004";//子公司的同步配置
        String groupCompany_database_name = "innover_ibs_470000";//集团公司数据库
        String subCompany_database_name = "innover_ibs_470004";//子公司数据库

        //生成canal配置文件
        main.generateCanalConfigurationFiles(templatePath,outPutPath,outSpecificPrefix,separator,groupCompanyCode,subCompanyCode,
                groupCompany_source,subCompany_source,groupCompany_target,subCompany_target,groupCompany_database_name,subCompany_database_name);
    }
    //生成canal配置文件
    private void generateCanalConfigurationFiles(
            String templatePath,
            String outPutPath,
            String outSpecificPrefix,
            String separator,
            String groupCompanyCode,
            String subCompanyCode,
            String groupCompany_source,
            String subCompany_source,
            String groupCompany_target,
            String subCompany_target,
            String groupCompany_database_name,
            String subCompany_database_name
    ){
        String subCompanyToGroupCompany = groupCompanyCode+separator+subCompanyCode;//子公司同步到集团的文件名
        String groupCompanyToSubCompany = subCompanyCode+separator+subCompanyCode;//集团同步到子公司

        File file = new File(templatePath);
        if(!file.isDirectory()){
            throw new RuntimeException("该文件不是文件夹");
        }
        //通过过滤器过滤该文件夹下的文件和文件夹，返回yml结尾的文件
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //获取根目录下每个文件的File对象
                File file1 = new File(dir, name);
                //编写筛选条件
                return file1.isFile() && file1.getName().endsWith(".yml");
            }
        });
        List<File> filesList = new ArrayList<>(Arrays.asList(files));
        //过滤掉有hh、merge的文件，过滤特殊配置，留下的正常配置同步去重处理
        Map<String, File> fileMap = filesList.stream().filter(i->i.getName().indexOf("hh")==-1 && i.getName().indexOf("merge")==-1)
                .collect(Collectors.toMap(
                        i -> {//截取筛选同步的表名
                            String file1Name = i.getName();
                            int index = file1Name.substring(0, file1Name.lastIndexOf("_")).lastIndexOf("_");
                            String substring = file1Name.substring(0, index);
                            return substring;
                        },
                        Function.identity(), (v1, v2) -> v1
                ));

        for(String keyTableName : fileMap.keySet()){
            File file1 = fileMap.get(keyTableName);
            String file1Name = file1.getName();
            int i = file1Name.lastIndexOf(".");
            int i1 = file1Name.lastIndexOf("_");
            String substring = file1Name.substring(i1+1, i);//最后一个.和最后一个_之间的内容，用于判断是同步到集团还是同步到子公司
            //读取路径文件名中的内容
            String templateContent = this.readerSqlTemplate(templatePath,file1Name);
            String dataPrecessContent = null;
            String fileOutName = null;

            int index = file1Name.substring(0, file1Name.lastIndexOf("_")).lastIndexOf("_");
            String substring2 = file1Name.substring(0, index);
            if(Objects.equals(groupCompanyCode,substring)){//同步到集团
                dataPrecessContent = this.dataPrecessToGroup(templateContent,
                        groupCompany_source, subCompany_source,
                        groupCompany_target, subCompany_target,
                        groupCompany_database_name, subCompany_database_name);
                fileOutName = outSpecificPrefix + separator + substring2 + separator + subCompanyToGroupCompany + ".yml";
            }else{//同步到子公司
                dataPrecessContent = this.dataPrecessToSub(templateContent,
                        groupCompany_source, subCompany_source,
                        groupCompany_target, subCompany_target,
                        groupCompany_database_name, subCompany_database_name);
                fileOutName = outSpecificPrefix + separator + substring2 + separator + groupCompanyToSubCompany + ".yml";
            }
            System.out.println("\t\t\t"+substring);
            //向文件中输出内容
            this.writerOut(dataPrecessContent,outPutPath,fileOutName);
        }
    }
    //读取路径文件名中的内容
    public String readerSqlTemplate(String filePath,String fileName) {
        FileReader fr = null;
        File file = null;
        StringBuffer sqlTemplate = new StringBuffer();
        try {
            file = new File(filePath+"\\"+fileName);
            fr = new FileReader(file);

            char[] cbuffer = new char[1024];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                String str = new String(cbuffer, 0, len);
                sqlTemplate.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //4.资源的关闭
                if (fr != null)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        System.out.println("文件读取完成，文件名："+file.getName());
        return sqlTemplate.toString();
    }

    //向文件中输出内容
    public void writerOut(String content,String filePath,String fileName) {
        FileWriter fileWriter = null;//对原有文件的追加
        File file= null;
        try {
            //1.提供File类的对象，指明写出到的文件
            file = new File(filePath+"\\"+fileName);
            fileWriter = new FileWriter(file, true);
            //3.写出的操作
            fileWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //4.流资源关闭
            try {
                if(fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件输出完成，文件名："+file.getName());
    }

    public String dataPrecessToGroup(String templateContent,
                            String groupCompany_source,String subCompany_source,
                            String groupCompany_target,String subCompany_target,
                            String groupCompany_database_name,String subCompany_database_name){

        StringBuilder stringBuilder = new StringBuilder();
        //子公司同步到集团
        int dataSourceKeyIndex = templateContent.indexOf("dataSourceKey: ");
        int dataSourceKeyContentIndex = dataSourceKeyIndex+15;
        int dataSourceKeyContentEndIndex = templateContent.indexOf("\n",dataSourceKeyContentIndex);
        stringBuilder.append(templateContent, 0, dataSourceKeyContentIndex);
        stringBuilder.append(subCompany_source);

        int outerAdapterKeyIndex = templateContent.indexOf("outerAdapterKey: ", dataSourceKeyContentEndIndex);
        int outerAdapterKeyContentIndex = outerAdapterKeyIndex+17;
        int outerAdapterKeyContentEndIndex = templateContent.indexOf("\n",outerAdapterKeyContentIndex);
        stringBuilder.append(templateContent,dataSourceKeyContentEndIndex,outerAdapterKeyContentIndex);
        stringBuilder.append(groupCompany_target);

        int databaseIndex = templateContent.indexOf("database: ", outerAdapterKeyContentEndIndex);
        int databaseIndexContentIndex = databaseIndex+10;
        int databaseIndexContentEndIndex = templateContent.indexOf("\n",databaseIndexContentIndex);
        stringBuilder.append(templateContent,outerAdapterKeyContentEndIndex,databaseIndexContentIndex);
        stringBuilder.append(subCompany_database_name);
        stringBuilder.append(templateContent,databaseIndexContentEndIndex,templateContent.length());
//        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
    public String dataPrecessToSub(String templateContent,
                                     String groupCompany_source,String subCompany_source,
                                     String groupCompany_target,String subCompany_target,
                                     String groupCompany_database_name,String subCompany_database_name){

        StringBuilder stringBuilder = new StringBuilder();
        //子公司同步到集团
        int dataSourceKeyIndex = templateContent.indexOf("dataSourceKey: ");
        int dataSourceKeyContentIndex = dataSourceKeyIndex+15;
        int dataSourceKeyContentEndIndex = templateContent.indexOf("\n",dataSourceKeyContentIndex);
        stringBuilder.append(templateContent, 0, dataSourceKeyContentIndex);
        stringBuilder.append(groupCompany_source);

        int outerAdapterKeyIndex = templateContent.indexOf("outerAdapterKey: ", dataSourceKeyContentEndIndex);
        int outerAdapterKeyContentIndex = outerAdapterKeyIndex+17;
        int outerAdapterKeyContentEndIndex = templateContent.indexOf("\n",outerAdapterKeyContentIndex);
        stringBuilder.append(templateContent,dataSourceKeyContentEndIndex,outerAdapterKeyContentIndex);
        stringBuilder.append(subCompany_target);

        int databaseIndex = templateContent.indexOf("database: ", outerAdapterKeyContentEndIndex);
        int databaseIndexContentIndex = databaseIndex+10;
        int databaseIndexContentEndIndex = templateContent.indexOf("\n",databaseIndexContentIndex);
        stringBuilder.append(templateContent,outerAdapterKeyContentEndIndex,databaseIndexContentIndex);
        stringBuilder.append(groupCompany_database_name);
        stringBuilder.append(templateContent,databaseIndexContentEndIndex,templateContent.length());
//        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

}
