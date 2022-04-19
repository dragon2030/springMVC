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
<input type="text" id="spc" name="spc" />
<button onclick="function1()">按钮</button>
<script>
	function function1() {
		debugger;
		var a1 = "";
		var a2 = null;
		var a3 ;
		if(a1){
			console.info("a1: "+a1)
		}else{
			console.info("!a1: "+a1)
		}
		if(a2){
			console.info("a2: "+a2)
		}else{
			console.info("!a2: "+a2)
		}
		if(a3){
			console.info("a3 :"+a3)
		}else{
			console.info("!a3: "+a3)
		}
	}

</script>
</html>