<%@page import="com.topsci.utils.OA_Config"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.topsci.bean.AttendanceRecords"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/jsp/";
%>
<html>
<head>
	<base href="<%=basePath%>">
	<title>考勤登记</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/attendance.css">
	<script type="text/javascript">
	var jsessionid ='<%=request.getParameter("jsessionid") %>';
	var OA_TIME1 = new Date();
	function get_time()
	{
	  window.setTimeout( "get_time()", 1000 );
	  
	  timestr=OA_TIME1.toLocaleString();
	  document.getElementById('timetable').innerHTML=timestr;
	  OA_TIME1.setSeconds(OA_TIME1.getSeconds()+1);
	}

	function registered(type){
		$.ajax({
			url:"../ar/IP_Filter.do",
			data:{
				jsessionid:jsessionid
			},
			success:function(data){
				var msg = data.split(',');
				if(msg[0]=='remote_networks'){
					alert('IP:'+msg[1]+' 无法进行考勤登记！');
					return;
				}else{
					if(type=="OFF"){
						$.ajax({
							url:"../work/checkWorkHorus.do",
							data:{
								type:type,
								jsessionid:jsessionid
							},
							success:function(data){
								if((data==null || data=="" || data<100.0) && type=="OFF"){
									if(confirm("每日工时必须为100，今日工时百分比("+data+")还未填写完整,无法进行下班登记。是否进行登记工时？")){
										window.location.href = '../work/initWorkForm.do?jsessionid='+jsessionid;
									}else{
										return ;
									}
								}else{
									window.location.href = '../ar/Register.do?jsessionid='+jsessionid+'&type='+type;
								}
							}
						});
					}else{
						window.location.href = '../ar/Register.do?jsessionid='+jsessionid+'&type='+type;
					}
				}
			}
		});
	}
	
	function remark(id,type){
		var value = prompt("备注信息：",'');
		if(value!=null && value!=""){
			$.ajax({
				url:"../ar/addRemark.do",
				type:"POST",
				data:{
					ar_id : id,
					remark:value,
					type : type,
					jsessionid:jsessionid
				},
				success:function(data){
					if(data!=null && data!=""){
						document.getElementById("remark_"+type+"_"+id).innerHTML=data;
					}else{
						alert("备注添加错误！");
					}
				}
			});
		}
	}
	
	
	//get_time();
</script>
</head>
<body>
<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
<!----  上下班登记 ---->
<!-- <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr align="center">
    <td class="Big">
    <img src="images/attendance.gif" WIDTH="22" HEIGHT="20" ><span class="big3">  当前时间：<span id="timetable"></span></span><br>
    </td>
  </tr>
</table> -->

<table class="MessageBox" align="center" width="80%">
   <tr class="head-no-title">
      <td class="left"></td>
      <td class="center">
      </td>
      <td class="right"></td>
   </tr>
   <tr class="msg">
      <td class="left"></td>
      <td class="center info">
         <div class="msg-content">
         <span style="font-weight: bold;font-size: 18px">上班登记:</span>规定时间 <%=OA_Config.getConfig(OA_Config.ATTENDANCE_ON_STARTDATE) %>~<%=OA_Config.getConfig(OA_Config.ATTENDANCE_ON_ENDDATE) %> 这段时间可进行上班登记。<br/>
         <span style="font-weight: bold;font-size: 18px">下班登记:</span>规定时间 <%=OA_Config.getConfig(OA_Config.ATTENDANCE_OFF_STARTDATE) %>~<%=OA_Config.getConfig(OA_Config.ATTENDANCE_OFF_ENDDATE) %> 这段时间可进行下班登记。</div>
      </td>
      <td class="right"></td>
   </tr>
   <tr class="foot">
      <td class="left"></td>
      <td class="center"></td>
      <td class="right"></td>
   </tr>
</table>

<table class="TableList" align="center" width="95%">
    <tr class="TableHeader">
      <td nowrap align="center">登记次序</td>
      <td nowrap align="center">登记类型</td>
      <td nowrap align="center">规定时间</td>
      <td nowrap align="center">登记时间</td>
      <td nowrap align="center" style="width: 30%;" colspan="2">操作</td>
    </tr>
     <c:if test="${AttendanceRecordsList!=null}">
	   <c:forEach items="${AttendanceRecordsList}" var="attendance">
	   	<tr class="TableData">
			<td nowrap align="center">第1次登记</td>
			<td nowrap align="center">上班登记</td>
			<td nowrap align="center"><%=OA_Config.getConfig(OA_Config.ATTENDANCE_ON) %></td>
			<td nowrap align="center">
			<%-- <c:if test="${isRemote=='local_networks'}"> --%>
			<c:choose>
				<c:when test="${attendance.is_enable_on=='N' and attendance.on_Date==null}">
					未登记
				</c:when>
				<c:when test="${attendance.is_enable_on=='Y' and attendance.on_Date==null}">
					<a href="javascript:registered('ON')">上班登记</a>
				</c:when>
				<c:when test="${attendance.on_Date!=null}">
					<fmt:formatDate value="${attendance.on_Date}" type="date" pattern="HH:mm:ss"/>
				</c:when>
			</c:choose>
			<%-- </c:if> --%>
			<%-- <c:if test="${isRemote=='remote_networks'}">
				本IP无法进行登记
			</c:if> --%>
			<c:if test="${attendance.is_enable_on=='L'}">
					<span class=big4>迟到</span>
			</c:if>
			</td>
			<td nowrap align="center">
				<div id="remark_ON_${attendance.ar_id}">${attendance.on_description}</div>
			</td>
			<td style="width: 7%;">
				<c:if test="${attendance.on_Date!=null }">
					<a href="javascript:remark('${attendance.ar_id}','ON');">说明情况</a>
				</c:if>
			</td>
	   	</tr>
	   	<tr class="TableData">
			<td nowrap align="center">第2次登记</td>
			<td nowrap align="center">下班登记</td>
			<td nowrap align="center"><%=OA_Config.getConfig(OA_Config.ATTENDANCE_OFF) %></td>
			<td nowrap align="center">
			<%-- <c:if test="${isRemote=='local_networks'}"> --%>
			<c:choose>
				<c:when test="${attendance.off_Date==null && attendance.is_enable_off=='N'}">
					未登记
				</c:when>
				<c:when test="${attendance.off_Date==null && attendance.is_enable_off=='Y'}">
					<a href="javascript:registered('OFF')">下班登记</a>
				</c:when>
				<c:otherwise>
					<fmt:formatDate value="${attendance.off_Date}" type="date" pattern="HH:mm:ss"/>
					<a href="javascript:registered('OFF')">再次登记</a>
				</c:otherwise>
			</c:choose>
			<%-- </c:if> --%>
			<c:if test="${isRemote=='remote_networks'}">
				本IP无法进行登记
			</c:if>
			</td>
			<td nowrap align="center">
				<div id="remark_OFF_${attendance.ar_id}">
					<c:if test="${attendance.is_enable_off=='N' and attendance.off_description==null}">
						不在登记时间段
					</c:if>
					<c:if test="${attendance.off_description!=null}">
						${attendance.off_description}
					</c:if>
				</div>
			</td>
			<td>
				<c:if test="${attendance.off_Date!=null}">
					<a href="javascript:remark('${attendance.ar_id}','OFF');">说明情况</a>
				</c:if>
			</td>
	 	</tr>
	   </c:forEach>
   </c:if>
</table>
  
<%--
<table class="TableList" align="center" width="95%">
    <tr class="TableHeader">
      <td nowrap align="center">登记次序</td>
      <td nowrap align="center">登记类型</td>
      <td nowrap align="center">规定时间</td>
      <td nowrap align="center">登记时间</td>
      <td nowrap align="center">操作</td>
    </tr>
 <tr class="TableData">
   <td nowrap align="center">第1次登记</td>
   <td nowrap align="center">上班登记</td>
   <td nowrap align="center">9:00:00</td>
   <td nowrap align="center">09:10:45 <span class=big4>迟到</span></td>

   <td nowrap align="center"> 已考勤 <a href="javascript:remark('1','2013-06-26 09:10:45');">说明情况</a>
   </td>
 </tr>

 <tr class="TableData">
   <td nowrap align="center">第2次登记</td>
   <td nowrap align="center">下班登记</td>

   <td nowrap align="center">18:00:00</td>
   <td nowrap align="center">未登记</td>
   <td nowrap align="center">不在登记时间段   </td>
 </tr>
</table>
--%>
</div>
	</body>
</html>