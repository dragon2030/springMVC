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
</head>
<body>
<button onclick="ajaxTest1()">测试ajax中error返回格式1</button>
<button onclick="ajaxTest2()">测试ajax中error返回格式2</button>
<br>
</body>
<script>
	//测试ajax中error返回格式1
	//异常时返回详细错误信息
	function ajaxTest1() {
		$.ajax({
			url:"<%=basePath %>ajaxTest/requestTest",    //请求的url地址
			dataType:"json",   //返回格式为json
			async:true,//请求是否异步，默认为异步，这也是ajax重要特性
			data:{"param":"Mike"},    //参数值
			type:"POST",   //请求方式
			beforeSend:function(){
				//请求前的处理
			},
			success:function(req){
				var jsonArray = req.list;
				var resuleJson = "["+JSON.stringify(jsonArray[0])+","+JSON.stringify(jsonArray[1])+"]";
				console.info(resuleJson);
				console.info(JSON.parse(resuleJson)[0].age);
			},
			complete:function(){
				//请求完成的处理
			},
			error: function (xhr, textStatus, errorThrown) {
				debugger;
				/*错误信息处理*/
				console.info("进入error---");
				console.info("状态码："+xhr.status);
				console.info("状态:"+xhr.readyState);//当前状态,0-未初始化，1-正在载入，2-已经载入，3-数据进行交互，4-完成。
				console.info("错误信息:"+xhr.statusText );
				console.info("返回响应信息："+xhr.responseText );//这里是详细的信息
				console.info("请求状态："+textStatus);
				console.info(errorThrown);
				console.info("请求失败");
			}
		});
	}

	//测试ajax中error返回格式2
	//异常时返回object
	function ajaxTest2() {
		$.ajax({
			url:"<%=basePath %>ajaxTest/requestTest",    //请求的url地址
			dataType:"json",   //返回格式为json
			async:true,//请求是否异步，默认为异步，这也是ajax重要特性
			data:{"param":"Mike"},    //参数值
			type:"POST",   //请求方式
			success:function(req){
				var jsonArray = req.list;
				var resuleJson = "["+JSON.stringify(jsonArray[0])+","+JSON.stringify(jsonArray[1])+"]";
				console.info(resuleJson);
				console.info(JSON.parse(resuleJson)[0].age);

			},
			error: function (data) {
				debugger;
				/*错误信息处理*/
				console.info("系统异常："+data);
			}
		});
	}
</script>
</html>