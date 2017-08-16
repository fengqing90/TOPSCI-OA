<%@page import="com.topsci.utils.MySessionContext"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'"); 
%>
<html>
<head>
<script src="../js/jQuery/jquery-2.0.2.min.js" type="text/javascript"></script>
<script src="../js/jQuery/navigation/menu.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="../js/jQuery/navigation/style.css" />
<script type="text/javascript">
	var jsessionid ='<%=request.getParameter("jsessionid") %>';
	function forword(url) {
		top.frames['main'].location.href = url+"?jsessionid="+jsessionid;
	}

	function initMenu() {
		//$('#menu ul').hide();
		//$('#menu ul:last').hide();
		$('#menu li a').click(function() {
			var checkElement = $(this).next();
			if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
				return false;
			}
			if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
				$('#menu ul:visible').slideUp('normal');
				checkElement.slideDown('normal');
				return false;
			}
		});
	}

</script>
</head>
<body onload="initMenu()">
<c:set scope="session" value='<%=MySessionContext.getInstance().getSession(request.getParameter("jsessionid")).getAttribute("user") %>' var="user" ></c:set>
<div style="left: 1pt; position: absolute; top: 0pt;">
	<ul id="menu">
		<li><a href="#">考勤</a>
			<ul>
				<li><a href="javascript:forword('../ar/initAR.do')">考勤登记</a></li>
				<li><a href="javascript:forword('../ar/QueryAR.do')">考勤查看</a></li>
				<c:if test="${user.user_type=='ADMIN'}">
				<li><a href="javascript:forword('../ar/QueryARStatis.do')">考勤统计</a></li>
				</c:if>
			</ul>
		</li>
		<li><a href="#">工时</a>
			<ul>
				<li><a href="javascript:forword('../work/initWorkForm.do')">工时登记</a></li>
				<li><a href="javascript:forword('../work/QueryWork.do')">工时查看</a></li>
				<li><a href="javascript:forword('../work/QueryWorkPercentage.do')">工时统计</a></li>
			</ul>
		</li>
		<li><a href="#">人员维护</a>
			<ul>
				<li><a href="javascript:forword('../user/initUserForm.do')">个人信息</a></li>
				<c:if test="${user.user_type=='ADMIN'}">
				<li><a href="javascript:forword('../user/QueryUser.do')">人员信息维护</a></li>
				</c:if>
			</ul>
		</li>
		<c:if test="${user.user_type=='ADMIN'}">
			<li><a href="javascript:forword('../project/initProjectForm.do')">项目维护</a></li>
		</c:if>
		<%-- <li><%=request.getParameter("jsessionid") %></li> --%>
	</ul>
</div>
	<!-- <ul>
		<li><a href="javascript:forword('../AttendanceRecords.do?method=enter')">考勤登记</a></li>
		<li><a href="javascript:forword('../AttendanceRecords.do?method=query&pageNum=1')">考勤查看</a></li>
		<li><a href="javascript:forword('../work/initWorkForm.do')">工时登记</a></li>
		<li><a href="javascript:forword('../work/QueryWork.do')">工时查看</a></li>
		<li><a href="javascript:forword('../work/QueryWorkPercentage.do')">工时统计</a></li>
		<li><a href="javascript:forword('../project/initProjectForm.do')">项目维护</a></li>
		<li><a href="javascript:forword('../user/initUserForm.do')">人员维护</a></li>
	</ul> -->
	
</body>
</html>
