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
    <title>demo2</title>
    <script src="resources/js/plug_in/jquery-2.1.0.js"></script>
</head>
<body>
    <button id="btn" onclick="setValue()">按钮测试</button>
    <button id="btn2" onclick="showValue()">按钮测试</button>
</body>
<script type="javascript">
    var a = 1;

    function setValue(){
        a=2;
    };
    function showValue(){
        console.info(a);
    };
</script>
</html>
