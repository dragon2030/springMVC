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
<title>FIRST PAGE</title>
<script src="resources/js/plug_in/jquery-2.1.0.js"></script>
<script src="js/test.js?v=<%=Math.random()%>" type="text/javascript"></script>
</head>
<body>
	<h1 style="color:blue">HELLO WORLD</h1>
	<button onclick="myFunction()" id="button1">按钮</button>
</body>
<script>
function myFunction() {
    console.info("push_button执行");
    $.ajax({
        url:"/test_ajax",    //请求的url地址
        dataType:"json",   //返回格式为json
        async:true,//请求是否异步，默认为异步，这也是ajax重要特性
        data:{"param":"Mike"},    //参数值
        type:"POST",   //请求方式
        beforeSend:function(){
            //请求前的处理
        },
        success:function(req){
        	if(1==1 && req != null){
				alert("请求成功："+req);
			}
        },
        complete:function(){
            //请求完成的处理
        },
        error:function(){
            //请求出错处理
        }
    });
}
</script>
</html>