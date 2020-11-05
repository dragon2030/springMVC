<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<base href="<%=basePath %>" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>hello world</title>
</head>
<body>
 	<div style="color:red">
		<h1 >HELLO WORLD</h1>
	</div>
	<hr>
<a href=<%=basePath%>uploadDownload/main>file upload and download</a>
<hr>
<a href=${pageContext.request.contextPath}/gluttonousSnake/gluttonousSnake1.0>play a game</a>
<hr>
<a href=<%=basePath%>test/main>test page</a>
</body>
<script>
</script>
</html>