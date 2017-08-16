<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<%
	String processStatus = (String)request.getAttribute("processStatus");
	%>
	<head>
	<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.core.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.widget.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.datepicker.js"></script>
	<script src="../js/jQuery/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
	
	<!-- <link rel="stylesheet" href="js/jQuery/themes/base/jquery.ui.all.css"> -->
	<link rel="stylesheet" href="../js/jQuery/themes/base/jquery.ui.core.css">
	<link rel="stylesheet" href="../js/jQuery/themes/base/jquery.ui.theme.css">
	<link rel="stylesheet" href="../js/jQuery/themes/base/jquery.ui.datepicker.css">
	<link rel="stylesheet" href="../js/jQuery/datepicker/css/datepicker.css">

	<link rel="stylesheet" type="text/css" href="../jsp/css/attendance.css">
	<style type="text/css">
		.TableList .TableHeader td, .TableList td.TableHeader{
		    text-align: right;
		    width: 80px;
		}
		.TableList {
			border: 0;
		}
	</style>
	
	<script type="text/javascript">
		function updateProcessType(type){
			document.getElementById('processType').value=type;
			document.forms['userForm'].submit();
		}
		
		function processStatus(status){
			var msg = status.split(',');
			if(msg[0]=="error"){
				alert(msg[1]);
			}
		}
		
		function onload(){
			$( "#birthday" ).datepicker({
				changeMonth: true,
				changeYear: true,
				dateFormat: 'yy-mm-dd',
				showButtonPanel: true
			});
			
			/* $( "#entrant_time" ).datepicker({
				changeMonth: true,
				changeYear: true,
				dateFormat: 'yy-mm-dd',
				showButtonPanel: true
			}); */
		}
		eval("processStatus('<%=processStatus%>')")
	</script>
	</head>
	<body onload="onload()">
	<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
	<div id="title"  style="font-size: 16;font-weight: bold;text-align: center;">个人信息维护</div>
		<!-- <input type="hidden" id="process" value="processStatus()"> -->
		<div id="fromDiv" >
			<sf:form id="userForm" action="../user/saveOrUpdate.do"  modelAttribute="currUser" commandName="currUser" onsubmit="">
				<sf:hidden path="user_id"/>
				<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
				<input type="hidden" name="type" value="${type}"/>
				<sf:hidden path="is_dimission"/>
				<sf:hidden path="user_type"/>
				<sf:hidden path="create_date"/>
				<fieldset style="width: 93.7%;margin-bottom: 20px; margin-left: 2.5%;">
					<legend  style="font-weight: bold; font-size: 13px;">登录信息</legend>
					<table class="TableList" border="0">
						<tr>
							<td >账户名：</td>
							<td><sf:input path="alias_name" readonly="true"/></td>
						</tr>
						<%-- <tr>
							<td >密码 ：</td>
							<td><sf:password path="user_pwd"/></td>
						</tr> --%>
					</table>
				</fieldset>
				<fieldset style="width: 93.7%;margin-bottom: 20px; margin-left: 2.5%;text-align: left;">
					<legend  style="font-weight: bold; font-size: 13px;">个人信息</legend>
					<table class="TableList" border="0" style="text-align: right;" >
						<tr>
							<td >姓名：</td>
							<td><sf:input path="user_name"/></td>
							<td >性别：</td>
							<td>
								<sf:select path="sex" cssStyle="width:100%">
									<sf:option value="M" label="男"/>
									<sf:option value="F" label="女"/>
								</sf:select>
							</td>
							<td >电话号码：</td>
							<td><sf:input path="phone_number"/></td>
						</tr>
						<tr>
							<td >邮箱：</td>
							<td><sf:input path="email"/></td>
							<td >生日：</td>
							<td><sf:input path="birthday" id="birthday" readonly="readonly"/></td>
							<td >入职时间：</td>
							<td><sf:input path="entrant_time" id="entrant_time" readonly="true"/></td>
						</tr>
						<tr>
							<td>部门：</td>
							<td colspan="5"><sf:input path="user_dept" id="user_dept" readonly="true" style="width: 100%;"/></td>
						</tr>
						<tr>
							<td >住址：</td>
							<td colspan="4"><sf:textarea path="address" style="width:100%" /></td>
							<%-- <td >是否离职：</td>
							<td>
								<sf:select path="is_dimission" cssStyle="width:100%">
									<sf:option value="Y" label="是"/>
									<sf:option value="N" label="否"/>
								</sf:select>
							</td> --%>
							<td rowspan="2">
								<input type="hidden" id="processType" name="processType" value="update">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<c:choose>
									<c:when test="${type=='reg'}">
										<input type="submit" value="新增" onclick="updateProcessType('add')" style="width: 100%; height: 80px;"/>
									</c:when>
									<c:otherwise>
										<input type="submit" value="修改" onclick="updateProcessType('update')" style="width: 100%; height: 80px;"/>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td rowspan=2 >备注：</td>
							<td rowspan=2 colspan=4><sf:textarea path="remark" style="width:100%"/></td>
						</tr>
					</table>
				</fieldset>
			</sf:form>
		</div>
		</div>
	</body>
</html>
