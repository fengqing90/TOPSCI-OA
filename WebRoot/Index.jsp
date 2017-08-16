<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";
	response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
%>
<html>
<head>
<base href="<%=basePath%>">
</head>

<frameset cols="15%,*" border=1 framespacing="0">
	<frame name="menu" src="jsp/Left.jsp?jsessionid=${session_id}" marginheight="100%" scrolling="auto">
	<frame name="main" src="ar/initAR.do?jsessionid=${session_id}" marginheight="100%" scrolling="auto">
<!-- 	<frame name="menu" src="jsp/Left.jsp" marginheight="100%" scrolling="auto">
	<frame name="main" src="AttendanceRecords.do?method=enter" marginheight="100%" scrolling="auto"> -->
</frameset>
</html>
