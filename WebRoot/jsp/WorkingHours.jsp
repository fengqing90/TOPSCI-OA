<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String processStatus = (String) request.getAttribute("processStatus");
%>

<html>
<head>
<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
<script src="../js/jQuery/ui/jquery.ui.core.js"></script>
<script src="../js/jQuery/ui/jquery.ui.widget.js"></script>
<script src="../js/jQuery/ui/jquery.ui.datepicker.js"></script>
<script src="../js/jQuery/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
<script src="../js/jQuery/ui/jquery.ui.position.js"></script>
<script src="../js/jQuery/ui/jquery.ui.menu.js"></script>
<script src="../js/jQuery/ui/jquery.ui.autocomplete.js"></script>

<!-- <link rel="stylesheet" href="js/jQuery/themes/base/jquery.ui.all.css"> -->
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.core.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.theme.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.datepicker.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.menu.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.autocomplete.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/datepicker/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="../jsp/css/attendance.css">

<style type="text/css">
body {
	font-size: 80.5%;
	font-family: "Trebuchet MS", "Arial", "Helvetica", "Verdana", "sans-serif";
}
.ui-autocomplete-category {
		font-weight: bold;
		padding: .2em .4em;
		margin: .8em 0 .2em;
		line-height: 1.5;
		background: #669900;
}
.ui-autocomplete {
		max-height: 500px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
}

/* .ui-state-hover,
.ui-widget-content .ui-state-hover,
.ui-widget-header .ui-state-hover,
.ui-state-focus,
.ui-widget-content .ui-state-focus,
.ui-widget-header .ui-state-focus {
	background: #aaa;
	border-left: 5px #000 solid;
	padding-left: 15px;
} */

</style>
<script type="text/javascript">
		function onload() {
			/* $("#workaday").datepicker({
				changeMonth : true,
				changeYear : true,
				dateFormat : 'yy-mm-dd',
				showButtonPanel : true
			}); */
			
		$.widget( "custom.catcomplete", $.ui.autocomplete, {
			_renderMenu: function( ul, items ) {
				var that = this,currentCategory = "";
				$.each( items, function( index, item ) {
					if ( item.category != currentCategory ) {
						ul.append( "<li class='ui-autocomplete-category'>类型：" + item.category + "</li>" );
						currentCategory = item.category;
					}
					that._renderItemData( ul, item );
				});
			}
		});
			
		$("#project").catcomplete({
			minLength : 0,
			autoFocus:true,
			source : function(request, response) {
				$.ajax({
					url:"../project/findProjectByName_JS.do",
					type:"POST",
					data:{
						projectName:request.term,
						jsessionid:'<%=request.getParameter("jsessionid") %>'
					},
					success:function(data){
					data = data.split(",");									
					response($.map(data, function( item ) {
						item = item.split("=");
						return {
							label: item[0],
							value: item[1],
							category:item[2]
						}
					}));
					}
				});
			},
			select : function(event, ui) {
				document.getElementById('label_projectName').innerHTML="项目名："+ui.item.label+"&nbsp;&nbsp;&nbsp;";
				return true;
			}
		});
		}
		
	function validate(value){
		var reg = new RegExp("/^\d+$/");
		if (!reg.test(value)) {
			return false;
		}
	}

	function process(type, formName) {
		if (type == 'delete') {
			if (!confirm("确定删除?")) {
				return false;
			}
		} else {
			var form = document.forms[formName];
			if (form.project.value == null || form.project.value == "") {
				alert("项目不能为空!");
				form.project.select();
				return false;
			}
			
//			if(!validate(form.working_hours_PCT.value)){
			if(isNaN(form.working_hours_PCT.value)){
				alert("工时百分比必须为 数值 !");
				form.working_hours_PCT.select();
				return false;
			}

			if (form.working_hours_PCT.value > 100) {
				alert("单项时不能大于100!");
				form.working_hours_PCT.select();
				return false;
			}
		}
		document.getElementById(formName + '_type').value = type;
		document.forms[formName].submit();
	}

	function processStatus(status) {
		if (status != null && status != "" && status != "null") {
			var msg = status.split(',');
			if(msg[0]!='success'){
				alert(msg[1]);
			}
		}
	}
	eval("processStatus('<%=processStatus%>')")
</script>
</head>

<body onload="onload()">
<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
	<div id="title" align="center" style="font-size: 16;font-weight: bold;">工时登记</div>
	<div id="workFromDiv"  align="center">
		<sf:form method="POST" id="workForm" action="../work/saveOrUpdate.do" modelAttribute="currWork" commandName="currWork">
			<input type="hidden" name="jsessionid" value="${jsessionid }"/>
			<sf:hidden path="work_id" />
			<fieldset style="width: 93.7%;margin-bottom: 20px;text-align: left;">
				<legend style="font-weight: bold; font-size: 13px;">工时登记</legend>
				<table border="0" style="width: 98%;text-align: center;" class="TableList">
					<tr class="TableHeader">
						<td style="width: 18.8%;">日期</td>
						<td>项目</td>
						<td>百分比工时</td>
						<td>操作</td>
					</tr>
					<tr>
						<td>
							<input id="workaday" name="workaday" readonly="readonly" value="<fmt:formatDate value="${currWork.workaday}" type="date" pattern="yyyy-MM-dd" />"/>
						</td>
						<td>
							<label id="label_projectName"></label>
							<input id ="project" name="project" onclick="javascript:this.select();"/>
						</td>
						<td><sf:input path="working_hours_PCT"  onclick="javascript:this.select();"/></td>
						<td>
							<input type="hidden" id="workForm_type" name="type" value="update" class="fontStyle">
							<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
							<input type="button" value="新增/修改" onclick="process('add','workForm')" />
						</td>
					</tr>
				</table>

			</fieldset>
		</sf:form>
	</div>

	<div id="queryDiv"  align="center">
		<div id="queryForm"></div>
		<div id="showDate">
			<fieldset style="width: 93.7%;text-align: left;">
				<legend  style="font-weight: bold; font-size: 13px;">个人工时</legend>
				<c:if test="${page.list!=null}">
					<table border="0" style="width: 100%;text-align: center;" class="TableList">
						<tr class="TableHeader">
							<td style="width: 18.8%;">日期</td>
							<td style="width: 32%;">项目</td>
							<td style="width: 32%;">百分比工时</td>
							<td>操作</td>
						</tr>
						<c:forEach items="${page.list }" var="work">
							<form id="${work.work_id}" method="POST" action="../work/saveOrUpdate.do">
							<tr class="TableDate" style="background: #E6F1D8;">
								<td>
									<input type="hidden" name="work_id" value="${work.work_id}"/>
									<input type="hidden" name="workaday" value="<fmt:formatDate value="${work.workaday}" type="date" pattern="yyyy-MM-dd" />"/>
									<fmt:formatDate value="${work.workaday}" type="date" pattern="yyyy-MM-dd" />
									<%-- <fmt:formatDate value="${work.workaday}" type="date" pattern="E" /> --%>
								</td>
								<td>
									<input type="hidden" name="project" value="${work.project.project_code }"/>
									<c:out value="${work.project.project_name}"/>
									<br>( ${work.project.project_type} )
								</td>
								<td><input name="working_hours_PCT" value="${work.working_hours_PCT}"  onclick="javascript:this.select();"></td>
								<td>
									<input type="hidden" id="${work.work_id}_type" name="type" value="update">
									<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
									<input type="button" value="修改" onclick="process('update','${work.work_id}')">
									<input type="button" value="删除" onclick="process('delete','${work.work_id}')">
								</td>
							</tr>
							</form>
						</c:forEach>
					</table>
				</c:if>
			</fieldset>
		</div>
	</div>
	</div>
</body>
</html>
