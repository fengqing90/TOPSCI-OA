<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	String processStatus = (String) request.getAttribute("processStatus");
%>
<html>
<head>
	<script type="text/javascript" src="/TOPSCI_OA/js/jQuery/jquery-2.0.2.min.js"></script>
	<script src="/TOPSCI_OA/js/jQuery/ui/jquery.ui.core.js"></script>
	<script src="/TOPSCI_OA/js/jQuery/ui/jquery.ui.widget.js"></script>
	<script src="/TOPSCI_OA/js/jQuery/ui/jquery.ui.datepicker.js"></script>
	<script src="/TOPSCI_OA/js/jQuery/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
	
	<link rel="stylesheet" href="/TOPSCI_OA/js/jQuery/themes/base/jquery.ui.core.css">
	<link rel="stylesheet" href="/TOPSCI_OA/js/jQuery/themes/base/jquery.ui.theme.css">
	<link rel="stylesheet" href="/TOPSCI_OA/js/jQuery/themes/base/jquery.ui.datepicker.css">
	<!-- <link rel="stylesheet" href="js/jQuery/themes/base/jquery.ui.all.css"> -->
	<link rel="stylesheet" href="/TOPSCI_OA/js/jQuery/datepicker/css/datepicker.css">
	<link rel="stylesheet" type="text/css" href="/TOPSCI_OA//jsp/css/attendance.css">
	<script type="text/javascript">
		function onload() {
			$("#starDate").datepicker({
				changeMonth : true,
				changeYear : true,
				dateFormat : 'yy-mm-dd',
				showButtonPanel : true
			});
	
			$("#endDate").datepicker({
				changeMonth : true,
				changeYear : true,
				dateFormat : 'yy-mm-dd',
				showButtonPanel : true
			});
		}
		function updateWorkHours(modifyDate){
			window.location.href='initWorkForm.do?modifyDate='+modifyDate;
		}
		
		function resetForm(){
			document.getElementById('starDate').value="";
			document.getElementById('endDate').value="";
		}
		
		function submitForm(processType){
			document.getElementById('processType').value=processType;
			document.getElementById('stat_form').submit();
		}
	</script>
	<style type="text/css">
	body {
		font-size: 12px;
		font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana","sans-serif";
	}
/* 	table#dataTable td:hover:{
		background: #aaa;
		border-left: 5px #000 solid;
		padding-left: 15px;
	} */

	</style>
</head>

<body onload="onload()">
<div style="top: 10px; position: absolute; width: 95%;height: 99%;" id="div_body">
	<div id ="title" align="center" style="font-size: 16;font-weight: bold;">工时统计</div>
	<div align="center">
		<fieldset style="width: 93.7%; text-align: left;">
			<legend  style="font-weight: bold; font-size: 13px;">查询</legend>
			<div id ="queryForm" align="center">
				<form action="/TOPSCI_OA/work/QueryWorkPercentage.do" id="stat_form">
				<input id="processType" name = "processType" value="query" type="hidden">
				开始时间：<input id="starDate" name="starDate" type="text" readonly="readonly" value="${starDate }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				结束时间：<input id="endDate" name="endDate" type="text" readonly="readonly" value="${endDate }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="查询" onclick="submitForm('query')">&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="重置" onclick="resetForm()">&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="导出" onclick="submitForm('reportExcel')">
				<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
				</form>
			</div>
		</fieldset>
	</div>
	<br/>
	<br/>
	<div id="showData" align="center">
		<fieldset style="width: 93.7%;height: 81%;text-align: left;">
			<legend  style="font-weight: bold; font-size: 13px;">查询结果</legend>
			<div style="overflow:auto; width: 1077;height: 100%;">
			<c:if test="${percentageInfoBean!=null}">
				<table id="dataTable" class="TableList" align="center" width="${columnSize<10?100:columnSize*8}%" border="1" style="border: 1px solid #CCCCCC;">
					<tr class="TableHeader" >
						<td rowspan="2" style="width: 50px;">姓名</td>
						<c:forEach items="${percentageInfoBean.columnListGroup}" var="group">
							<td colspan="${group.value}" style="padding: 5px;min-width: 100px;">${group.key}</td>
						</c:forEach>
						<td colspan="2" >合计</td>
					</tr>
					<tr class="TableHeader" >
						<c:forEach items="${percentageInfoBean.columnList}" var="column">
							<td style="padding: 5px;min-width: 30px;background-color: #99cc99;">${column.project_name}</td>
						</c:forEach>
						<td style="width: 50px;min-width: 30px;background-color: #99cc99;">已登记</td>
						<td style="width: 50px;min-width: 30px;background-color: #99cc99;">未登记</td>
					</tr>
					<c:if test="${percentageInfoBean.valueList!=null}">
					<c:forEach items="${percentageInfoBean.valueList}" var="value">
					<tr class="TableData" align="center" style="">
						<td style="border: 1px solid #CCCCCC;">${value.user.user_name}</td>
							<c:if test="${value.workList!=null}">
								<c:forEach items="${value.workList}" var="work">
									<td style="border: 1px solid #CCCCCC;">
										<fmt:formatNumber pattern="0.00" value="${work.working_hours_PCT}"></fmt:formatNumber>
									</td>		
								</c:forEach>
							</c:if>
						<td>
							<c:if test="${value.sum_PCT!=0.0}">
									<fmt:formatNumber pattern="0.00" value="${value.sum_PCT}"></fmt:formatNumber>
							</c:if>
						</td>
						<c:choose>
							<c:when test="${100.0-value.sum_PCT==0.0}">
								<td></td>
							</c:when>
							<c:when test="${100.0-value.sum_PCT!=0.0}">
								<td style="background:orange;"><fmt:formatNumber pattern="0.00" value="${100.0-value.sum_PCT}"/></td>
							</c:when>
						</c:choose>
					</tr>
					</c:forEach>
					</c:if>
				</table>
			</c:if>
			</div>
		</fieldset>
	</div>
	</div>
</body>
</html>
<%
//防止报java.lang.IllegalStateException: getOutputStream() has already been called for this response
String processType = (String)request.getAttribute("processType");
if(processType!=null && processType.equals("reportExcel")){
	out.clear();  
	out = pageContext.pushBody();
}
%>

