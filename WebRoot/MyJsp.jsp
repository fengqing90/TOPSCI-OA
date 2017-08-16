<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<title>qfeng</title>
<script type="text/javascript" src="js/jQuery/jquery-2.0.2.min.js"></script>
<script type="text/javascript">

	function updateTime() {
		var form = document.forms['form_updateTime'];
		$.ajax({
			url : "ar/mandatoryUpdate.do",
			data : {
				userName : form.userName.value,
				userPwd : form.userPwd.value,
				type : form.type.value,
				date : form.date.value,
				dateTime : form.dateTime.value,
				jsessionid:'MyJsp'
			},
			success : function(data) {
				alert(data);
			}
		});
	}
	
	function updateUser(){
		var form = document.forms['form_updateUser'];
		$.ajax({
			url : "user/updateUser.do",
			data : {
				userName : form.userName.value,
				userPwd : form.userPwd.value,
				user_type : form.user_type.value,
				jsessionid:'MyJsp'
			},
			success : function(data) {
				alert(data);
			}
		});
	}
</script>
</head>

<body>
	<div style="text-align: center;">特殊功能页面</div>
	<div>
	==============强制修改考勤==============
	<form action="" id="form_updateTime">
		<table>
			<tr>
				<td>用户名：</td>
				<td><input type="text" name="userName"></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="password" name="userPwd"></td>
			</tr>
			<tr>
				<td>登记类型</td>
				<td><select name="type">
						<option value="ON">上班</option>
						<option value="OFF">下班</option>
				</select></td>
			</tr>
			<tr>
				<td>所在日期：</td>
				<td><input type="text" name="date" value="<fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd"/>"/> 格式：<fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd"/></td>
			</tr>
			<tr>
				<td>修改时间：</td>
				<td><input type="text" name="dateTime" value="<fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/>"/> 格式：<fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
			<tr>
				<td><input type="button" value="修改" onclick="updateTime()"></td>
				<td><input type="reset" value="重置"></td>
			</tr>
		</table>
	</form>
	</div>
	<div>
	==============人员类型修改==============
	<form action="" id="form_updateUser">
		<table>
			<tr>
				<td>用户名：</td>
				<td><input type="text" name="userName"></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="password" name="userPwd"></td>
			</tr>
			<tr>
				<td>类型：</td>
				<td>
					<select name="user_type" style="width:100%">
						<option value="ADMIN">管理员</option>
						<option value="EMPLOYE">职工</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="button" value="修改" onclick="updateUser()"></td>
				<td><input type="reset" value="重置"></td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>
