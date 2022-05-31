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
// function myFunction() {
//     console.info("push_button执行");
//     $.ajax({
//         url:"/test_ajax",    //请求的url地址
//         dataType:"json",   //返回格式为json
//         async:true,//请求是否异步，默认为异步，这也是ajax重要特性
//         data:{"param":"Mike"},    //参数值
//         type:"POST",   //请求方式
//         beforeSend:function(){
//             //请求前的处理
//         },
//         success:function(req){
//         	if(1==1 && req != null){
// 				alert("请求成功："+req);
// 			}
//         },
//         complete:function(){
//             //请求完成的处理
//         },
//         error:function(){
//             //请求出错处理
//         }
//     });
// }
var json = "{\"info\": [\"1、本人已知晓燃气工程项目规范相关要求\", \"2、本着“选择自由，自愿购买”原则，以上所有收费单价已进行价格公示， 本人自愿选择并购买以上收费产品\", \"3、一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十\"]}";
if(json != null && json != "" && json != undefined) {
	var obj = eval("(" + json + ")");
	var res = "";
	var arr = obj.info;
	for(var i = 0; i < arr.length; i++){
		res += arr[i] + "\r\n";
	}
	console.info(res);
}

//json字符串转对象方法
// function parseJson(json){
//     json = "(" + json + ")";
//     return eval(json);
// }
</script>
</html>
