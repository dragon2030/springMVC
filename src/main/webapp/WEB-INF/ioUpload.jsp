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
上传方式一：ajax提交<br>
    <form action="${pageContext.request.contextPath}/uploadDownload/uploadDemo2"
          enctype="multipart/form-data" method="post" name="form1" id="form1">
        上传用户：<input type="text" name="username"><br>
        上传文件1（字节流）：<input type="file" name="meFile"><br>
    </form>
    <button onclick="submitButton()">外部提交</button>
    <br>ajax提交状态： &nbsp;&nbsp; <label id="result1"></label> <br>
    <hr>
上传方式二:表单提交<br>
    <form action="${pageContext.request.contextPath}/servlet/UploadDemo"
          enctype="multipart/form-data" method="post" name="form2">
        上传用户：<input type="text" name="username"><br>
        上传文件1（字节流）：<input type="file" name="meFile"><br>
        <input type="submit" value="提交" name="submitButton">
        <br>表单提交状态： &nbsp;&nbsp; ${message}
    </form>
    <br><hr>
    显示阅览：<br>
    <c:forEach var="me" items="${fileList}">
        <c:url value="/uploadDownload/download" var="downUrl">
            <c:param name="filename" value="${me}"></c:param>
        </c:url>
        ${me}<a href="${downUrl}">下载</a>
        <br/>
    </c:forEach>
    <hr />
    下载：<br>
    <c:forEach var="me" items="${fileList}">
        <c:url value="/uploadDownload/download2" var="downUrl">
            <c:param name="filename" value="${me}"></c:param>
        </c:url>
        ${me}<a href="${downUrl}">下载</a>
        <br/>
    </c:forEach>

</body>
<script>
    function submitButton() {
        debugger;
        var url = $("form[name='form1']").attr("action");
        var formData = new FormData(document.getElementById("form1"));
        console.info(formData);
        $.ajax({
            url: url,    //请求的url地址
            data: formData,
            async: true,//请求是否异步，默认为异步，这也是ajax重要特性
            type: "POST",   //请求方式
            //ajax2.0可以不用设置请求头，但是jq帮我们自动设置了，这样的话需要我们自己取消掉
            contentType:false,
            //取消帮我们格式化数据，是什么就是什么
            processData:false,
            success: function (req) {
                console.info(req);
                $("#result1").children().remove().end().append(req);
            },
            error: function (data) {
                console.info("系统异常：" + data);
            }
        });
    }

</script>
</html>
