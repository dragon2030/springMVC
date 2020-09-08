<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath %>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script src="resources/js/plug_in/jquery-2.1.0.js"></script>
    <%--<script src="resources/js/json2.js"/>--%>
</head>
<body>
<button onclick="test1()">js创建json数组</button>
<br>
测试 ajax传值中 dataType:"json" ，当有此参数时自动将返回值转换为json对象
<button onclick="test2()">ajax中json处理1</button>
<button onclick="test3()">ajax中json处理2</button>
<br>
<button onclick="test4()">json字符串与json对象的转换</button>
</body>
<script>
    //js创建json数组
    function test1() {
        //创建json数组
        var datas=[];
        //创建json对象
        var data = {};
        data["id"] = 1;
        data["name"] = "test" + 1;
        data["age"] = 1 * 2;
        datas.push(data);
        var jsonString = JSON.stringify(datas);  //[{"id":1,"name":"test1","age":2}]
        console.info(jsonString);
    }

    //ajax返回数据中json处理
    function test2() {
        $.ajax({
            url:"<%=basePath %>jsParseJson/ajaxTest",    //请求的url地址
            dataType:"json",   //返回格式为转变为json
            async:true,//请求是否异步，默认为异步，这也是ajax重要特性
            data:{"param":"Mike"},    //参数值
            type:"POST",   //请求方式
            success:function(req){
                console.info("data:"+JSON.stringify(req))
                console.info("第一个user中name:"+req.list[0].name);
                console.info("第一个user中age:"+req.list[0].age);
                console.info("第一个user中people_des:"+req.list[0].peopleDes);
                console.info("第一个user中sex_id:"+req.list[0].sexId);

            },
            error:function(data){
                //请求出错处理
                alert("系统异常"+data);
            }
        });
    }

    //ajax返回数据中json处理2
    function test3() {
        $.ajax({
            url:"<%=basePath %>jsParseJson/ajaxTest",    //请求的url地址
            async:true,//请求是否异步，默认为异步，这也是ajax重要特性
            data:{"param":"Mike"},    //参数值
            type:"POST",   //请求方式
            success:function(req){
                var data= JSON.parse(req);
                console.info("data:"+JSON.stringify(data))
                console.info("第一个user中name:"+data.list[0].name);
                console.info("第一个user中age:"+data.list[0].age);
                console.info("第一个user中people_des:"+data.list[0].peopleDes);
                console.info("第一个user中sex_id:"+data.list[0].sexId);
            },
            error:function(data){
                //请求出错处理
                alert("系统异常"+data);
            }
        });
    }

    //json字符串与json对象的转换
    function test4() {
        //创建JSON对象
        var json1 = {};
        json1["id"] = 1;
        json1["name"] = "test" + 1;
        console.info("创建JSON对象-json1.id："+json1.id+" json1.name:"+json1.name);
        //JSON对象:
        var json2 = { "name": "cxh", "sex": "man" };
        console.info("JSON对象-json2.name:"+json2.name+" json2.sex:"+json2.sex);

        //JSON字符串:
        var str1 = '{ "name": "cxh", "sex": "man" }';
        console.info("JSON字符串-"+str1);

        //JSON对象转JSON字符串
        //方法一：
        //var str2=json2.toJSONString(); //json.js包版本太低
        //console.info("JSON对象转JSON字符串-"+str1);
        //方法二
        var str3=JSON.stringify(json2);
        console.info("JSON对象转JSON字符串-JSON.stringify(json2)-"+str1);

        //JSON字符串转JSON对象
        //方法一
        var obj1 = eval('(' + str1 + ')');
        console.info("JSON字符串转JSON对象-eval('(' + str1 + ')')-obj1.name:"+obj1.name+" obj1.sex:"+obj1.sex);
        //方法二
        var obj2 = $.parseJSON(str1);
        console.info("JSON字符串转JSON对象-$.parseJSON(str1)-obj2.name:"+obj2.name+" obj2.sex:"+obj2.sex);
        //方法三
        var obj3 = JSON.parse(str1);
        console.info("JSON字符串转JSON对象-JSON.parse(str1)-obj3.name:"+obj3.name+" obj3.sex:"+obj3.sex);
        //方法四
         //var obj4 = str1.parseJSON(); //json.js包版本太低
    }
</script>
</html>
