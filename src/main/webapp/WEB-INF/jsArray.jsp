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
    <title>jsArray</title>
    <script src="resources/js/plug_in/jquery-2.1.0.js"></script>
</head>
<body>
<button onclick="join()">join()</button>&nbsp;功能：将数组中所有元素都转化为字符串并连接在一起。
<br>
<button onclick="push()">push()</button>&nbsp;在数组末尾添加一个或多个元素，并返回新数组长度
插入元素<input id="input1" type="text">
<br>
<button onclick="pop()">pop()</button>&nbsp;从数组末尾删除1个元素(删且只删除1个), 并返回被删除的元素
<br>
<button onclick="reverse()">reverse()</button>&nbsp;功能：将数组中的元素颠倒顺序。
<br>
<button onclick="concat()">concat()</button>&nbsp;功能：数组拼接的功能 ,返回新数组，原数组不受影响。
<br>
<button onclick="slice()">slice()</button>&nbsp;截取数组生成新数组，原数组不受影响。
首位<input id="input2" type="text">末位<input id="input3" type="text">
<br>
<button onclick="splice()">splice()</button>&nbsp;功能：从数组中删除元素、插入元素到数组中或者同时完成这两种操作。
<br>
<button onclick="shift()">shift()</button>&nbsp;用于把数组的第一个元素从其中删除，并返回第一个元素的值。
<br>
<button onclick="indexOf()">indexOf()</button>&nbsp;要查找的项和（可选的）表示查找起点位置的索引。
<br>
<button onclick="lastIndexOf()">lastIndexOf()</button>&nbsp;方法可返回一个指定的元素在数组中最后出现的位置，从该字符串的后面向前查找。
<br>
高阶函数。。。。。
<br>
参考网址<a href="https://www.jianshu.com/p/22a4c0b514fa">https://www.jianshu.com/p/22a4c0b514fa</a>
</body>
<script>
    var mycars=new Array()
    mycars[0]="Saab"
    mycars[1]="Volvo"
    mycars[2]="BMW"
    console.info("arrays:"+mycars);

    function join() {
        console.info("join:"+mycars.join());
    }
    function push() {
        var len = mycars.push($("#input1").val());
        console.info("arrays:"+mycars);
        console.info("len:"+len);
        $("#input1").val("");
    }
    function pop() {
        var  param= mycars.pop();
        console.info("arrays:"+mycars);
        console.info("param:"+param);
    }
    function reverse() {
        mycars.reverse();
        console.info("arrays:"+mycars);
    }
    function concat() {
        var str = mycars.concat();
        console.info("arrays:"+mycars);
        console.info("str:"+str);
    }
    function slice() {
        var newArray = mycars.slice($("#input2").val(),$("#input3").val());
        console.info("arrays:"+mycars);
        console.info("newArray:"+newArray);
    }
    function splice() {
        mycars.splice(1,1,'iii','uuu');
        console.info("arrays:"+mycars);
    }
    function shift() {
        var len = mycars.shift();
        console.info("arrays:"+mycars);
        console.info("len:"+len);
    }
    function indexOf() {

        console.info("indexOf:"+ mycars.indexOf("BMW",0));
    }
    function lastIndexOf() {
        console.info("lastIndexOf:"+mycars.lastIndexOf("BMW"));
    }
</script>
</html>
