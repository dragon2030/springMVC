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
    <title>snakeDeath</title>
    <script src="resources/js/plug_in/jquery-2.1.0.js"></script>
</head>
<body>
<b>snakeDeath</b>
<br>
<b>本次贪吃蛇最大长度为：${length}</b>
<hr>
<%--<div class="div1 "></div>--%>
<table name="table1" id="table1" border="1">
    <thead>
        <tr>
            <td>排序</td>
            <td>贪吃蛇长度</td>
            <td>记录时间</td>
        </tr>
    </thead>
    <c:forEach items="${list}" var="item" varStatus="rowNum">
        <c:if test="${item.colorFlag=='1'}">
            <tr  height='40' bgcolor="#f4a460">
        </c:if>
        <c:if test="${empty item.colorFlag}">
            <tr  height='40'>
        </c:if>
            <td width='100' >${rowNum.count}</td>
            <td width='100'>${item.length}</td>
            <td width='500'>${item.time}</td>
            <td hidden>${item.colorFlag}</td>
        </tr>
    </c:forEach>
</table>
<hr>
<a href="gluttonousSnake/gluttonousSnake1.0" >返回游戏</a>
<a href="index.jsp">首页</a>
</body>
<script type="javascript">

</script>
</html>
