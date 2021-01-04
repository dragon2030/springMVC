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
<button onclick="ajaxStandard()">ajax标准格式</button><br>
<button onclick="GetRequestResponseInfo()">获取ajax请求中HttpServletRequest，HttpServletResponse中的信息</button><br>
<button onclick="ajaxTest()">ajax试验</button><br>
<button onclick="ajaxTestError()">测试ajax中error返回格式</button><br>
<button onclick="ajaxDataType()">测试ajax中参数dataType</button><br>
<button onclick="ajaxContentType()">测试ajax中参数contentType</button>
<br>
</body>
<script>
	//jquary中ajax标准格式
	function ajaxStandard(){
		$.ajax({
			url:"<%=basePath %>ajaxTest/standardAjax",    //请求的url地址
			//contentType: "application/json;charset=UTF-8",//发送给服务器的格式
			dataType:"json",   //返回格式为json
			async:true,//请求是否异步，默认为异步，这也是ajax重要特性
			data:{"param":"Mike"},    //参数值
			type:"POST",   //请求方式
			beforeSend:function(){//请求前的处理
			},
			success:function(req){//成功返回数据
				console.info(req);
			},
			complete:function(){//请求完成的处理
			},
			error: function (xhr, textStatus, errorThrown) {/*错误信息处理*/
				console.info("进入error---"+"状态码："+xhr.status+"状态:"+xhr.readyState+"错误信息:"+xhr.statusText+"返回响应信息："
						+xhr.responseText+"请求状态："+textStatus+errorThrown+"请求失败");
			}
		});
	}
	//获取ajax请求中HttpServletRequest，HttpServletResponse中的信息
	function GetRequestResponseInfo() {
		$.ajax({
			url:"<%=basePath %>ajaxTest/GetRequestResponseInfo",    //请求的url地址
			data:{"param":"Mike"},    //参数值
			type:"POST",   //请求方式
			success:function(req){//成功返回数据
				console.info(req);
			}
		});
	}
	//ajax试验
	function ajaxTest() {
		$.ajax({
			url: "<%=basePath %>ajaxTest/ajaxTest",    //请求的url地址
			//dataType:"json",   //返回格式为json
			async: true,//请求是否异步，默认为异步，这也是ajax重要特性
			data: {"param": "Mike"},    //参数值
			type: "POST",   //请求方式
			success: function (req) {
				console.info(req);
				console.info(req.list[0].name);
				console.info(JSON.stringify(req));
			},
			error: function (data) {
				console.info("系统异常：" + data);
			}
		});
	}

	/*
	测试ajax中dataType参数
	测试结果：设置该值时response Headers Content-Type: application/json;charset=UTF-8
			  不设置该值是response Headers Content-Type: text/html;charset=UTF-8
	 */
	function ajaxDataType() {
		$.ajax({
			url: "<%=basePath %>ajaxTest/ajaxDataType",    //请求的url地址
			//dataType:"json",   //返回格式为json
			data: {"name": "Mike","age":"22"},    //参数值
			type: "POST",   //请求方式
			success: function (req) {
				console.info(req);
				console.info(req.list[0].name);
				console.info(JSON.stringify(req));
			},
			error: function (data) {
				console.info("系统异常：" + data);
			}
		});
	}

	/*
	测试ajax中参数contentType
	测试结果：
		设置该值时request Headers Content-Type:application/x-www-form-urlencoded; charset=UTF-8
		返回结果：name=Mike&age=22
		设置该值时request Headers Content-Type: application/json;charset=UTF-8
		返回结果：{"name":"Mike","age":"22"}

	理论说明：
	在 jquery 的 ajax 中， contentType都是默认的值：application/x-www-form-urlencoded，这种格式的特点就是，name/value 成为一组，
	每组之间用 & 联接，而 name与value 则是使用 = 连接。
	 */
	function ajaxContentType() {
		var data = {"name": "Mike","age":"22"};
		//默认
		$.ajax({
			url:"<%=basePath %>ajaxTest/ajaxContentType",
			//contentType: "application/json;charset=UTF-8",//发送给服务器的格式
			data:data,    //参数值
			type:"POST",   //请求方式
			success:function(req){//成功返回数据
				console.info(req);
			}
		});
		//contentType: "application/json;charset=UTF-8"
		$.ajax({
			url:"<%=basePath %>ajaxTest/ajaxContentType",
			contentType: "application/json;charset=UTF-8",//发送给服务器的格式
			data:JSON.stringify(data),    //参数值
			type:"POST",   //请求方式
			success:function(req){//成功返回数据
				console.info(req);
			}
		});
	}

		//测试ajax中error返回格式,异常时返回详细错误信息
		function ajaxTestError() {
			//简单错误信息
			$.ajax({
				url: "<%=basePath %>ajaxTest/ajaxError",    //请求的url地址
				data: {"param": "Mike"},    //参数值
				dataType:"json",   //返回格式为json
				type: "POST",   //请求方式
				success: function (req) {
					console.info(req);
				},
				error: function (e) {/*错误信息处理*/
					console.info("错误信息：" + e);
				}
			});
			//详细错误信息
			$.ajax({
				url: "<%=basePath %>ajaxTest/ajaxError",    //请求的url地址
				data: {"param": "Mike"},    //参数值
				dataType:"json",   //返回格式为json
				type: "POST",   //请求方式
				success: function (req) {
					console.info(req);
				},
				error: function (xhr, textStatus, errorThrown) {/*错误信息处理*/
					console.info("进入error---");
					console.info("状态码：" + xhr.status);
					console.info("状态:" + xhr.readyState);//当前状态,0-未初始化，1-正在载入，2-已经载入，3-数据进行交互，4-完成。
					console.info("错误信息:" + xhr.statusText);
					console.info("返回响应信息：" + xhr.responseText);//这里是详细的信息
					console.info("请求状态：" + textStatus);
					console.info(errorThrown);
					console.info("请求失败");
				}
			});
		}

</script>
</html>