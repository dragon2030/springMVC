<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Insert title here</title>

<script src="resources/js/plug_in/jquery-2.1.0.js"></script>

</head>
<body>
<button class="staffOperate" onclick="upperCase()">点击</button>

<!-- select标签测试 start-->
<select class="staff " id="staff" name="staff" onchange="test_select()">
    <option value="">选择网点</option>
    <option value="1">1</option>
    <option value="2">2</option>
</select>
<select class="test_select"  id="test_select" name="test_select" >
    <option value="a">A</option>
    <option value="b">B</option>
    <option value="c">C</option>
</select>
<script>
/**
 * 用select一个下拉属性改变另一个select下拉的选中项
 * option[2].selected=true添加属性
 */
function test_select(){
	var staff=$("#staff").val();
	if(staff=="1"){
		var option=$("#test_select").children();
		option[1].selected=true;
	}
	if(staff=="2"){
		var option=$("#test_select").children();
		option[2].selected=true;
	}
}
</script>
<!-- select标签测试 end -->
</body>
<script>
</script>
<script src="resources/js/test.js?v=<%=Math.random()%>" type="text/javascript"></script>
</html>
