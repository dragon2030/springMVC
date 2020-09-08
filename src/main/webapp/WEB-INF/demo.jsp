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

<!-- plug_in -->
<script type="text/javascript" src="resources/js/plug_in/jquery-ui.js"></script>

<!-- plug_in -->

<!-- com.bigDragonn.common.model js -->
<script type="text/javascript" src="resources/js/model_js/dataConvert.js"></script>
<script type="text/javascript" src="resources/js/model_js/webService.js"></script>
<!-- com.bigDragonn.common.model js -->


    <script type="text/javascript" >
    </script>
</head>
<body>
	<a href="helloWorld" class="hidden">hello world</a><br>
	<a href="test/encodingTest.jsp?name=张三&age=22">encodingTest</a>
	<form action="${pageContext.request.contextPath}/test/save" method="post" class="form1" id="form1" data-parsley-required="true">
		<div>
			<label class="control-label" for="name">姓名：</label>
			<input type="text" name="name" id="name" data-parsley-required="true">
		</div>
		
		<div>
			<label class="control-label" for="age">年龄：</label>
			<input type="text" name="age" id="age" data-parsley-required="true">
		</div>
		
		<div>
			<label class="control-label" for="peopleDes">人物描述：</label>
			<input type="text" name="peopleDes" id="peopleDes" data-parsley-required="true">
		</div>
		
		<div>
			<label class="control-label" for="sexId">性别：</label>
			<select class="form-control" id="sexId" name="sexId" data-parsley-required="true">
				<option value="0"></option>
	            <c:forEach var="enums" items="${sexEnum}">
	            	<option value="${enums.value}" >${enums.message}</option>
	            </c:forEach>
	        </select>
        </div>
		<!-- <div id="datepicker"></div> -->
 		<div>
			<label class="control-label" for="bornDate">出生日期：</label>
			<input type="text" name="bornDate" id="bornDate" class="datepicker-default">
		</div> 
		
 		<div>
			<label class="control-label" for="phoneNo">手机号码：</label>
			<input type="text" name="phoneNo" id="phoneNo"  data-parsley-required="true"
			data-parsley-phoneemail='#input1' data-parsley-phoneemail-message="can't equal!">
		</div>
		
		<input type="submit" id="tosubmit" class="tosubmit" value="表单提交">
		<input type="button" id="tosubmit2" class="tosubmit2" value="表单提交2">
		<input type="button" id="lookOver" class="lookOver" value="查看所有用户数据" >
		
	</form>
	<br>
	<div id="divTable1" style="display:none;">
		<table class="showTable" id="showTable" border="1">
		</table>
	</div>
	<!-- convert --><br>
	convert:<br>
	<button id="displayTable" class="displayTable" onclick="displayTable()">展示表数据</button>
	<button id="hiddenTable" class="hiddenTable" onclick="copyText()">隐藏表数据</button>
	<button id="test_Map_Json" class="test_Map_Json" onclick="test_Map_Json()">测试map-json转换</button>
 	<button id="test_Json_Json" class="test_Json_Json" onclick="test_Json_Json()">测试json-json转换</button>
 	<button id="test_List" class="test_List" onclick="test_List()">测试后台list数据</button>
 	<button id="testJson" class="testRequestResponse" onclick="test_request_response()">测试RequestResponse数据</button>
 	<button id="testJson" class="testJson" onclick="post_json()">测试application/json;charset=utf-8传输(json)</button>
 	<button id="testForm" class="testForm" onclick="test_form()">测试application/x-www-form-urlencoded传输</button>
 	
 	<!-- jquary --><br><br>
 	jquary:<br>
 	<input type=text id="change_event" value="change_event">
 	<input type=text id="onchange_event" value="onchange_event" onchange="onchange_event(this.id)">
 	<br>
 	<div id="div_1"><span></span><p></p></div>
 	<button id="div_btn" onclick="div_test()">html标签处理</button>
 	
 	
 	
 	<!-- IO --><br>
 	IO:<br>
 	下载数据：<input id="loading" value="C:\Users\3759\Desktop\壁纸\timg.jpg">
 	<a id="loading_btn" href="<%=basePath%>com.bigDragon.common.test/loading?loading_path=C:\Users\3759\Desktop\wallpaper\timg.jpg">loading</a>
 	
    <c:url value="/test/loading" var="downurl">
      	<c:param name="filename" value="C:\Users\3759\Desktop\壁纸\timg.jpg"></c:param>
   	</c:url>
       下载<a href="${downurl}">下载</a><br>
 	<!-- <img src="/img/Chevrolet Silverado.png" alt="" /> -->
 	<!-- <img src="com.bigDragonn.common.test/showImgGet?pathName=D://FPS//10_093427_18.JPG" alt="" /> -->
 	<form action="<%=basePath%>/test/loading" method="post">
	 	<input hidden type="text" name="filename" id="filename" value="C:\Users\3759\Desktop\wallpaper\timg.jpg">
	 	<input type="submit" name="sub_load_post" id="sub_load_post" value="表单POST实现下载提交">
 	</form>
 	<br>
 	<form action="${pageContext.request.contextPath}/test/upload" enctype="multipart/form-data" method="post">
		上传文件1：<input type="file" name="file1"><br/>
		<input type="submit" value="表单POST实现上传提交">
	</form>
 	<!-- plug in --> <br><br>
 	<input id="qrcode_text" type="text" value="http://www.baidu.com"/><br/>
 	<!-- <div id="qrcode" style="width:100px; height:100px; margin-top:15px;"></div><br/> -->
 	<button class="w2ui-btn" onclick="popup()">Open Popup</button>
 	<br>
 	<button id="testAJAX" name="testAJAX" onclick="test_ajax()">testAJAX</button>
</body>
<script>

	$(function(){
	   $('#form1').parsley();//调用parsley表单验证插件(提交时才会验证 是否必填和输入格式)
		//$('#form').parsley().validate();//调用parsley表单验证插件(页面加载时就验证 是否必填和输入格式)
		
	});
	function test_ajax(){
		$.ajax({
		    type: 'POST',
		    /* 同步异步开关 */
		    async: false,
		    url: 'com.bigDragon.common.test/testAJAX',
		    success: function (data) {
		    	console.info(data);

		    },
		    error: function (data) {
		      alert("系统异常"+data);
		    }
		});
		console.info("AJAX之后的命令执行！！");
	}
	
	$("#tosubmit2").click(function(){
		var form1Serialize=$("#form1").serialize();
		console.info(form1Serialize);
		$.ajax({
		    type: 'POST',
		    url: 'com.bigDragon.common.test/save',
		    data:form1Serialize, 
		    success: function (data) {
		    	console.info(data);
		    	console.info("name:"+data.name);
		    },
		    error: function (data) {
		      alert("系统异常"+data);
		    }
		});
	});
	
	
	  	//ajax后台数据回显
		$(".lookOver").click(function(){
			$.ajax({
			    type: 'GET',
			    url: 'com.bigDragon.common.test/getUser',
			    success: function (data) {
			    	console.info(data);
					var table=document.getElementById("showTable");
					//表头
					var thead=document.createElement("thead");
					var tr_head=document.createElement("tr");
					for (var key in data[0]){
						var td_head=document.createElement("td");
						td_head.style.width="300px";
						td_head.id="column_head_"+key;
						var column_head=document.createTextNode(key);
						td_head.appendChild(column_head);
						tr_head.appendChild(td_head);
		        	}
					thead.appendChild(tr_head);
					table.appendChild(thead);
					//document.getElementById("showTable").innerHTML("<thead><tr><td>id</td><td>姓名</td><td>年龄</td><td>个人描述</td></tr></thead>");
					//内容
					var tbody=document.createElement("tbody");
			        for(var i=0;i<data.length;i++){
		        		var tr_body=document.createElement("tr");
			        	for (var key in data[i]){
			        		var td_body=document.createElement("td");
			        		td_body.style.width="300px";
			        		td_body.id="column_body_"+i+"_"+key;
			        		var column_body=document.createTextNode(data[i][key]);
			        		td_body.appendChild(column_body);
				        	tr_body.appendChild(td_body);
			        	}
			        	//补全表中空白格子
			        	tbody.appendChild(tr_body);
			        }
			        table.appendChild(tbody);
			        
			        document.getElementById("divTable1").style.display='block';
			    },
			    error: function (data) {
			      alert("系统异常"+data);
			    }
			});
		});
	/* jquary */
	$("#change_event").change(function(){
		console.info($("#change_event").val());
		});
	
	function onchange_event(x){
		console.info(document.getElementById(x).value)
	}
	
	function div_test(){
		var HTML='<p>HELLO WORLD</p>';
		console.info($("#div_1"));
		console.info($("#div_1").find("span"));
		console.info(".children()");
		console.info($("#div_1").children());
		console.info($("#div_1").children().remove());
		console.info(".children().remove().end().children()");
		console.info($("#div_1").children().remove().end().children());
		console.info(".children().remove().end().append(HTML)");
		//$("#div_1").children().remove().end().append(HTML);
		//document.getElementById("div_1").innerHTML(HTML);
		document.getElementById('div_1').innerHTML=HTML;
	}
	
	/* IO */
	$("#loading_btn").click(function(){
/* 		var loading_path=$("#loading").val();
		   $.ajax({
		    type: 'POST',
		    url: 'com.bigDragonn.common.test/loading',
		    contentType: 'application/json;charset=utf-8',
		    data: loading_path,
		    success: function (data) {

		    	console.info(data);
		    },
		    error: function (data) {
		      alert("系统异常"+data);
		    }
		}); */
		window.location.href("com.bigDragon.common.test/loading?loading_path=C:\Users\3759\Desktop\壁纸\timg.jpg");
	  });
	
 	<!-- plug in -->
  	$('.datepicker-default').datepicker({
	    todayHighlight: true
	}); 
	/* $( "#datepicker" ).datepicker(); */
	/*qrcode*/

	function makeCode () {
		var qrcode = new QRCode(document.getElementById("qrcode"), {
			width : 100,
			height : 100
		});
		
		var elText = document.getElementById("qrcode_text");
		if (!elText.value) {
			console.info("Input a text");
			elText.focus();
			return;
		}
		qrcode.makeCode(elText.value);
	}

	$("#qrcode_text").on("blur", function () {
	    makeCode();
	}).on("keydown", function (e) {
	    if (e.keyCode == 13) {
	        makeCode();
	    }
	});
	
	/* w2ui popup */
	function popup() {
	    w2popup.open({
	        title     : 'Popup Title',
	        body      : '<div class="w2ui-centered" id="qrcode">This is text inside the popup</div>',
	        buttons   : '<button class="w2ui-btn" onclick="w2popup.close();">Close</button> '+
	                    '<button class="w2ui-btn" onclick="w2popup.lock(\'Loading\', true); '+
	                    '        setTimeout(function () { w2popup.unlock(); }, 2000);makeCode();">Lock</button>',
	        width     : 500,
	        height    : 300,
	        overflow  : 'hidden',
	        color     : '#333',
	        speed     : '0.3',
	        opacity   : '0.8',
	        modal     : true,
	        showClose : true,
	        showMax   : true,
	        onOpen    : function (event) { console.log('open'); },
	        onClose   : function (event) { console.log('close'); },
	        onMax     : function (event) { console.log('max'); },
	        onMin     : function (event) { console.log('min'); },
	        onKeydown : function (event) { console.log('keydown'); }
	    });
	}
</script>
<script src="resources/js/test.js?v=<%=Math.random()%>" type="text/javascript"></script>
</html>