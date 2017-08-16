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
	<link rel="stylesheet" type="text/css" href="/TOPSCI_OA/jsp/css/attendance.css">
	<link rel="stylesheet" type="text/css" href="/TOPSCI_OA/jsp/css/paging.css">
	<script type="text/javascript">
		var jsessionid ='<%=request.getParameter("jsessionid") %>';
		function onload() {
			$("#startTime").datepicker({
				changeMonth : true,
				changeYear : true,
				dateFormat : 'yy-mm-dd',
				showButtonPanel : true
			});
	
			$("#endTime").datepicker({
				changeMonth : true,
				changeYear : true,
				dateFormat : 'yy-mm-dd',
				showButtonPanel : true
			});
		}
		function updateWorkHours(modifyDate){
			window.location.href='initWorkForm.do?jsessionid='+jsessionid+'&modifyDate='+modifyDate;
		}
		function queryProjectByPageNumber(pageNumber){
			if(isNaN(pageNumber)){
				alert('分页参数不是数字？pageNumber:'+pageNumber);
				return false;
			}
			document.getElementById('pageNum').value=pageNumber;
			document.getElementById('queryForm').submit();
		}
	</script>
	<style type="text/css">
	body {
		font-size: 12px;
		font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana","sans-serif";
	}
	</style>
</head>

<body onload="onload()">
<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
	<div id ="title" align="center" style="font-size: 16;font-weight: bold;">工时查看</div>
	<div align="center">
		<fieldset style="width: 93.7%;text-align: left;">
		<legend  style="font-weight: bold; font-size: 13px;">查询</legend>
			<div id ="div_queryForm" align="center">
				<form action="/TOPSCI_OA/work/QueryWork.do" id="queryForm">
					<input name = "method" value="query" type="hidden">
					开始时间：<input id="startTime" name="starDate" type="text" readonly="readonly" value="${starDate }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					结束时间：<input id="endTime" name="endDate" type="text" readonly="readonly" value="${endDate }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="submit" value="查询">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="重置">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum">
					<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
				</form>
			</div>
		</fieldset>
	</div>
	<br/>
	<br/>
	<div id="showData" align="center">
		<fieldset style="width: 93.7%;text-align: left;">
			<legend  style="font-weight: bold; font-size: 13px;">查询结果</legend>
			<div id = "pagingDiv" class="pagination">
				<span>总页数：${pager.pageCount}</span>
				<a href="javascript:queryProjectByPageNumber('1')">首页</a>
				<c:forEach begin="${pager.pageNumber>5?pager.pageNumber-5:(pager.pageNumber-3>0?pager.pageNumber-3:1)}" end="${pager.pageCount-5>pager.pageNumber?pager.pageNumber+5:pager.pageCount}" step="1" var="page" varStatus="status">
					<c:choose>
						<c:when test="${page==pager.pageNumber}">
							<a href="javascript:queryProjectByPageNumber('${page}')" class="current">${page}</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:queryProjectByPageNumber('${page}')" >${page}</a>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<a href="javascript:queryProjectByPageNumber('${pager.pageCount}')">尾页</a>
			</div>
			<c:if test="${pager.list!=null}">
				<c:forEach var="workInfoBean" items="${pager.list}" >
					<table class="TableList" align="center" width="100%" border="0" style="margin-bottom: 5px;">
						<tr class="TableHeader" >
							<td style="width: 8%">日期</td>
							<td style="width: 7%">姓名</td>
							<c:forEach var="work" items="${workInfoBean.workList}">
								<td style="width: ${80/workInfoBean.workCount}%">${work.project.project_name}<br>( ${work.project.project_type} )</td>
							</c:forEach>
							<td style="width: 5%">合计</td>
							<td style="width: 5%">操作</td>
						<tr>
						<c:choose>
							<c:when test="${workInfoBean.sum_PCT==0.0}">
								<tr align="center" style="background:#CD2626;">
							</c:when>
							<c:when test="${workInfoBean.sum_PCT<100}">
								<tr align="center" style="background:#FF8247;">
							</c:when>
							<c:otherwise>
								<tr class="TableData" align="center">
							</c:otherwise>
						</c:choose>
							<td>
							<fmt:formatDate value="${workInfoBean.date}" type="date" pattern="yyyy-MM-dd" /><br/>
							<fmt:formatDate value="${workInfoBean.date}" type="date" pattern="E" />
							</td>
							<td>${workInfoBean.user.user_name}</td>
							<c:forEach var="work" items="${workInfoBean.workList}">
								<td>${work.working_hours_PCT}</td>
							</c:forEach>
							<td>${workInfoBean.sum_PCT}</td>
							<td style="width: 5%">
								<input type="button" value="修改" onclick="updateWorkHours('<fmt:formatDate value="${workInfoBean.date}" type="date" pattern="yyyy-MM-dd" />')">
								<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum_${workInfoBean.user.user_id}">
							</td>
					</table>
				</c:forEach>
			</c:if>
			
				<!-- <table class="TableList" align="center" width="95%">
					<tr class="TableHeader" >
						<td>日期</td>
						<td>姓名</td>
						<td>项目1</td>
						<td>项目2</td>
						<td>项目3</td>
						<td>项目4</td>
						<td>合计</td>
						<td>操作</td>
					</tr>
					<tr class="TableData" align="center">
						<td>2013-06-30</td>
						<td>冯庆</td>
						<td>40</td>
						<td>20</td>
						<td>10</td>
						<td>30</td>
						<td>100</td>
						<td>
							<input type="button" value="保存">
						</td>
					</tr>
				</table>
			
			<table class="TableList" align="center" width="95%">
					<tr class="TableHeader" >
						<td>日期</td>
						<td>姓名</td>
						<td>项目1</td>
						<td>项目2</td>
						<td>项目3</td>
						<td>项目4</td>
						<td>合计</td>
						<td>操作</td>
					</tr>
					<tr class="TableData" align="center">
						<td>2013-06-30</td>
						<td>张三</td>
						<td>50</td>
						<td>10</td>
						<td>30</td>
						<td>10</td>
						<td>100</td>
						<td>
							<input type="button" value="保存">
						</td>
					</tr>
			</table> -->
		</fieldset>
	</div>
	</div>
</body>
</html>
