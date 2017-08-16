<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%  response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'"); %>
<html>
	<head>
		<title>招通致晟网络智能办公系统</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link rel="stylesheet" type="text/css"href="jsp/css/Login.css" />
		<link rel="shortcut icon" href="jsp/images/tongda.ico" />
		<script type="text/javascript">
			function CheckForm()
			{
			   return true;
			}
		</script>
		<style type="text/css">
			#logo #form {
			    height: 60px;
			    padding-left: 100px;
			    width: 245px;
			}
		</style>
	</head>
	<body onload="javascript:document.form.password.focus();">
		<form name="form" method="post" action="user/userLogin.do" onsubmit="return CheckForm();">
			<div class="msg">
				<div></div>
				<div></div>
				<div></div>
			</div>
			<div id="logo">
				<div id="form">
					<div class="left">
						<div class="user">
							<input type="text" class="text" name="userName" maxlength="20"
								 onclick="this.select()" >
						</div>
						<div class="pwd">
							<input type="password" class="text" name="userPwd"
								 onclick="this.select()">
						</div>
						<!-- <a href="user/initUserForm.do?type=reg">注册</a> -->
					</div>
					<div class="right">
						<input type="submit" class="submit" title="登录" value="" />
					</div>
				</div>
			</div>
		</form>
		
	</body>
</html>