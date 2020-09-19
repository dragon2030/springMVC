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
HELLO WORLD
</body>
<script>
    var text = "///15372050554ddd";

    var mat2 = text.match(/\d{11}/g);

    var text3 = "///15370554ddd";
    var mat3 = text3.match(/\d{11}/g);

    console.info(mat2[0]);
    console.info(mat3==null);
</script>
</html>
