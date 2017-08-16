<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
</head>
<body>
<div id="msgDiv" style="margin-top: 25%;text-align: center;">
	<c:choose>
		<c:when test="${user==null and errorMsg==null}">
			<p>用户超时，请重新登陆!</p>
			<input type="button" value="重新登陆" onclick="javascript:top.location.href ='#';">
		</c:when>
		<c:when test="${errorMsg!=null}">
			<p>${errorMsg}</p>
			<input type="button" value="返回" onclick="javascript:history.go(-1);">
		</c:when>
	</c:choose>
	
</div>
</body>
</html>
