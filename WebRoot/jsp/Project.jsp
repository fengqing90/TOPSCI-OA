<%@page import="com.topsci.service.impl.ProjectServiceImpl"%>
<%@page import="com.topsci.service.IProjectService"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<html>
<head>

	<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.core.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.widget.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.position.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.menu.js"></script>
	<script src="../js/jQuery/ui/jquery.ui.autocomplete.js"></script>
	
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.core.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.theme.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.menu.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.autocomplete.css">
	<link rel="stylesheet" type="text/css" href="../jsp/css/attendance.css">
	<link rel="stylesheet" type="text/css" href="../jsp/css/paging.css">
	<script type="text/javascript">
		
		function process(type,formName){
			if(type=='delete'){
				if(!confirm("确定删除?")){
					return false;
				}
			}
			document.getElementById(formName+'_type').value=type;
			document.getElementById(formName).submit();
			//document.forms[formName].submit(); ???为什么不能获取到指定的form
		}
		
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}
		function onload(){
			$("#query_projectCode")
			.bind("keydown",
					function(event) {
						if (event.keyCode === $.ui.keyCode.TAB && $(this).data("ui-autocomplete").menu.active) {
							event.preventDefault();
						}
					})
			.autocomplete({
						minLength : 0,
						source: function( request, response) {
							$.ajax({
								url:"../project/findProjectNameOrProjectCode.do",
								data:{
									type:'code',
									param:extractLast(request.term),
									jsessionid:'<%=request.getParameter("jsessionid") %>'
								},
								success:function(data){
								data = data.split(",");									
								response($.map(data, function( item ) {
									return {
										label: item,
										value: item
									}
								}));
								}
							});
						},
						search: function() {
							var term = extractLast( this.value );
							if ( term.length < 2 ) {
								return false;
							}
						},
						focus : function() {
							return false;
						},
						select : function(event, ui) {
							var terms = split(this.value);
							terms.pop();
							terms.push(ui.item.value);
							terms.push("");
							this.value = terms.join(", ");
							return false;
						}
					});
			
			$("#query_projectName")
			.bind("keydown",
					function(event) {
						if (event.keyCode === $.ui.keyCode.TAB
								&& $(this).data("ui-autocomplete").menu.active) {
							event.preventDefault();
						}
					})
			.autocomplete({
						minLength : 0,
						source : function(request, response) {
							$.ajax({
								url:"../project/findProjectNameOrProjectCode.do",
								type:"POST",
								data:{
									param:extractLast(request.term),
									type:'name',
									jsessionid:'<%=request.getParameter("jsessionid") %>'
								},
								success:function(data){
								data = data.split(",");									
								response($.map(data, function( item ) {
									return {
										label: item,
										value: item
									}
								}));
								}
							});
						},
						focus : function() {
							return false;
						},
						select : function(event, ui) {
							var terms = split(this.value);
							terms.pop();
							terms.push(ui.item.value);
							terms.push("");
							this.value = terms.join(", ");
							return false;
						}
					});
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
		font-size: 62.5%;
		font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana",
			"sans-serif";
	}
	table {
		font-size: 1em;
	}
	.demo-description {
		clear: both;
		padding: 12px;
		font-size: 1.3em;
		line-height: 1.4em;
	}
	
	.ui-draggable,.ui-droppable {
		background-position: top;
	}
	.ui-autocomplete {
			max-height: 500px;
			overflow-y: auto;
			/* prevent horizontal scrollbar; */
			overflow-x: hidden;
		}
</style>
</head>
<body onload="onload()">
<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
	<div id="title"  style="font-size: 16;font-weight: bold;text-align: center;">项目维护</div>
	<div id="projectInfo" align="center">
		<div id="projectFormDiv" align="center">
			<fieldset style="width: 93.7%;margin-bottom: 10px;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">项目详细信息</legend>
			<sf:form method="POST" id="projectForm" action="../project/saveOrUpdate.do" modelAttribute="currProject" commandName="currProject">
				<table border="0" style="width: 100%;text-align: center;" class="TableList">
					<tr class="TableHeader">
						<td>项目编号</td>
						<td>项目名称</td>
						<td>项目类型</td>
						<td>项目描述</td>
						<td>操作</td>
					</tr>
					<tr align="center">
						<td><sf:input path="project_code" cssStyle="width:100%;height:120%"/></td>
						<td><sf:input path="project_name" cssStyle="width:100%;height:120%"/></td>
						<td><sf:textarea path="project_type" cssStyle="width:100%;height:120%;font-size:12px;"/></td>
						<td><sf:textarea path="description"  cssStyle="width:100%;height:120%;font-size:12px;"/></td>
						<td>
							<input type="hidden" id="projectForm_type" name="process_type" value="update">
							<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
							<input type="submit" value="新增/修改" onclick="process('add','projectForm')" />
						</td>
					</tr>
				</table>
			</sf:form>
			</fieldset>
		</div>
	</div>
	<div id="projectQuery" align="center">
		<fieldset style="width: 93.7%;margin-bottom: 10px;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">查询</legend>
			<div id="div_queryForm" align="center">
				<form action="../project/QueryProject.do" method="post" id="queryForm">
					<table style="width: 80%;text-align: center; font-size: 13px;">
						<tr>
							<td>项目编号：</td>
							<td><textarea id="query_projectCode" name="query_projectCode" style="width:100%;height:22px;font-family: Trebuchet MS;font-size:14px;">${query_projectCode}</textarea></td>
							<td>项目名称：</td>
							<td><textarea id="query_projectName" name="query_projectName" style="width:100%;height:22px;font-family: Trebuchet MS;font-size:14px;">${query_projectName}</textarea></td>
							<td>
								<input type="submit" value="查询">&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="reset" value="重置">&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid }">
								<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum">
							</td>
						</tr>
					</table>
				</form>
			</div>
		</fieldset>
	</div>
	
	<div id="resultset" align="center">
		<fieldset style="width: 93.7%;margin-bottom: 20px;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">查询结果</legend>
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
			<table border="0" style="width: 100%;text-align: center;" class="TableList">
				<tr class="TableHeader">
					<td>创建日期</td>
					<td>项目编号</td>
					<td>项目名称</td>
					<td>项目类型</td>
					<td>项目描述</td>
					<td>操作</td>
				</tr>
				<c:if test="${pager.list!=null}">
					<c:forEach var="project" items="${pager.list}">
						<form method="post" action="../project/saveOrUpdate.do" id="${project.project_code}">
							<tr class="TableData" align="center">
								<td>
									<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum_${query_projectCode}">
									<input type="hidden" name="query_projectName" value="${query_projectName}"/>
									<input type="hidden" name="query_projectCode" value="${query_projectCode}"/>
									<input type="hidden" name="create_date" value="${project.create_date}"/>
									<fmt:formatDate value="${project.create_date}" type="date" pattern="yyyy-MM-dd" />
								</td>
								<td>
									<input type="hidden" name="project_code" value="${project.project_code}"/>
									<c:out value="${project.project_code}"></c:out>
								</td>
								<td><input type="text" name="project_name" value="${project.project_name}" style="width:100%;height:120%"></td>
								<td><textarea name="project_type"  style="width:100%;height:120%;font-size:12px;">${project.project_type}</textarea></td>
								<td><textarea name="description"  style="width:100%;height:120%;font-size:12px;">${project.description}</textarea></td>
								<td>
									<input type="hidden" id="${project.project_code}_type" name="process_type" value="update">
									<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid }">
									<input type="submit" value="修改" onclick="process('update','${project.project_code}')">
									<input type="button" value="删除" onclick="process('delete','${project.project_code}')">
								</td>
							</tr>
						</form>
					</c:forEach>
				</c:if>
				<%-- <tr class="TableData" align="center">
					<td>2010-05-05</td>
					<td>1</td>
					<td>名称</td>
					<td>类型</td>
					<td>描述</td>
					<td>
						<input type="hidden" id="${work.work_id}_type" name="type" value="update">
						<input type="submit" value="修改" >
						<input type="button" value="删除" >
					</td>
				</tr>
				<tr class="TableData" align="center">
					<td>2011-07-15</td>
					<td>2</td>
					<td>名称</td>
					<td>类型</td>
					<td>描述</td>
					<td>
						<input type="submit" value="修改">
						<input type="button" value="删除">
					</td>
				</tr> --%>
			</table>
		</fieldset>
	</div>
	</body>
</html>
