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
    <title>GluttonousSnake1.0</title>
<script src="resources/js/plug_in/jquery-2.1.0.js"></script>
<%--<script type="text/javascript" src="static/jquery/jquery-2.1.0.js"></script>
<script type="text/javascript" src="static/jquery/jquery-ui.js"></script>
<script src="static/js/jquery/jquery-ui.js?v=<%=Math.random()%>" type="text/javascript"></script>--%>
<%--<style type="text/css">
    input {
        display:none;
    }
</style>--%>
</head>
<body>
<label id="length" name="length" >当前长度：</label>
<input type="input" class="input1" readonly onkeydown="return noNumbers(event)" />
<div class="div1 div2"></div>
<%--<p>This is a paragraph.</p>--%>
<%--<table class='table1' border='1'>
    <tr class='trLine0'><td class='tr0,td0'>1</td><td class='tr0,td1'>2</td><td class='tr0,td2'>3</td></tr>
    <tr class='trLine1'><td class='tr1,td0'>1</td><td class='tr1,td1'>2</td><td class='tr1,td2'>3</td></tr>
    <tr class='trLine2'><td class='tr2,td0'>1</td><td class='tr2,td1'>2</td><td class='tr2,td2'>3</td></tr>
</table>--%>
</body>
<script>
//插入初始表格
    var a = [];
    var keynum='39';
    var part_keynum='39';
    var r;
    var blackPoint=false;
    a.push("<table class='table1'  >");
    for (var i = 0; i < 20; i ++) {
        a.push("<tr class='trLine"+i+"' height='30'>");
        for ( var j=0;j <20 ;j ++){
            a.push("<td class='tr"+i+" td"+j+" grid' width='30'></td>");
        }
        a.push("</tr>");
    }
    a.push("</table>");
    var str = a.join("");
    a = null;
    $(".div1").empty().append(str);
    $(".tr0,.tr19,.td0,.td19").attr("bgcolor","black");
//插入初始蛇
    $(".tr10.td10,.tr10.td9,.tr10.td8").attr("bgcolor","black");
    var arrays= [];
    arrays.push("10|10");
    arrays.push("10|9");
    arrays.push("10|8");
    console.info(arrays);
// 每秒执行函数
    var timer = setInterval(executeFunction, 500);

    function executeFunction() {
        //判断蛇是否已经挂了
        if(r==true) {
            clearInterval(timer);
            $.ajax({
                type: "GET",
                url: "gluttonousSnake/snakeDeath?length="+(arrays.length-1),
                async: true,
                //contentType: 'application/json;charset=utf-8',
                success: function (data) {
                    console.info(data);
                    document.write(data);  //一句搞定
                    //$("#allBody").html(data);//刷新整个body页面的html
                }
            });
            return;
        }else if(r==false){
            window.location.reload(true);
            return;
        }
        //键盘方向输入改变蛇运动方向
        $(".input1").focus();
        if((part_keynum=='37'&& keynum=='39')||
            (part_keynum=='39'&& keynum=='37')||
            (part_keynum=='38'&& keynum=='40')||
            (part_keynum=='40'&& keynum=='38')
        ){
            console.info("蛇不后退");
        }else{
            keynum=part_keynum;
        }
        if(keynum=='37'){
            console.info("左");
            var firstArray=arrays[0].split("|");
            OneByOneStep(firstArray[0],(parseInt(firstArray[1])-1));
        }else if(keynum=='39'){
            console.info("右");
            var firstArray=arrays[0].split("|");
            OneByOneStep(firstArray[0],(parseInt(firstArray[1])+1));
        }else if(keynum=='38'){
            console.info("上");
            var firstArray=arrays[0].split("|");
            OneByOneStep((parseInt(firstArray[0])-1),firstArray[1]);
        }else if(keynum=='40'){
            console.info("下");
            var firstArray=arrays[0].split("|");
            OneByOneStep((parseInt(firstArray[0])+1),firstArray[1]);
        }

        //随机生成小黑点
        if(blackPoint==false){
            var blackPoint_bool=true;
            while (blackPoint_bool){
                var y=Math.floor(Math.random()*18)+1;
                var x=Math.floor(Math.random()*18)+1;
                if($(".tr"+y+".td"+x).attr("bgcolor")!="black"){
                    $(".tr"+y+".td"+x).attr("bgcolor","black");
                    console.info("blackPoint produce:"+y+" "+x);
                    blackPoint_bool=false;
                }
            }
            blackPoint=true;
        }
    }

function noNumbers(e)
{
    var p_keynum;
    p_keynum = window.event ? e.keyCode : e.which;

    part_keynum=p_keynum;
    console.info(part_keynum);
}
/**
 * 蛇每一步爬行
 * @param st1 下一个要爬行的坐标点 y轴 自上而下
 * @param st2下一个要爬行的坐标点 x轴 自左而右
 */
function OneByOneStep(st1,st2) {
    console.info("蛇爬行中，arrays:"+arrays);
    //判断蛇是否吃到小黑点
    if($(".tr"+st1+".td"+st2).attr("bgcolor")!="black"){
        var array1=arrays.pop().split("|");
        $(".tr"+array1[0]+".td"+array1[1]).attr("bgcolor","#FFFFFF");
    }else {//吃到小黑点
        blackPoint=false;
    }
    snakeDeath(st1,st2);
    arrays.unshift(st1+"|"+st2);
    $(".tr"+st1+".td"+st2).attr("bgcolor","black");
    console.info("蛇爬行中，加入新元素："+st1+" "+st2);
    //页面显示当前长度
    $(".input1").val(arrays.length);
}
/**
 * 判断蛇是否挂了
 * @param st1
 * @param st2
 */
function snakeDeath(st1,st2) {
    var bool=false;
    if(parseInt(st1)<=0 || parseInt(st1)>=19 || parseInt(st2)<=0 || parseInt(st2)>=19){bool=true;}
    for(var i=0;i<arrays.length;i++){
        if(arrays[i]==st1+"|"+st2){bool=true;}
    }
    if(bool) {
        r=confirm("蛇挂了");
    }
}
</script>
</html>
