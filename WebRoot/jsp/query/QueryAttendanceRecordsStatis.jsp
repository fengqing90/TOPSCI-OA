<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.menu.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.autocomplete.js"></script>
<script type="text/javascript" src="../js/jQuery/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>



<!-- <link rel="stylesheet" href="js/jQuery/themes/base/jquery.ui.all.css"> -->
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.core.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.theme.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.datepicker.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/datepicker/css/datepicker.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.menu.css">
<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.autocomplete.css">
<link rel="stylesheet" type="text/css" href="../jsp/css/attendance.css">
<link rel="stylesheet" type="text/css" href="../jsp/css/paging.css">
<script type="text/javascript">
	var jsessionid ='<%=request.getParameter("jsessionid")%>';
	function split(val) {
		return val.split(/,\s*/);
	}
	function extractLast(term) {
		return split(term).pop();
	}
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
		$("#query_userName")
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
							url:"../user/QueryUserName.do",
							type:"POST",
							data:{
								userName:extractLast(request.term),
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
						if ( term.length < 1 ) {
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
	}

	function remark(id, type) {
		var value = prompt("备注信息：", '');
		if (value != null && value != "") {
			$
					.ajax({
						type : "POST",
						data : {
							ar_id : id,
							remark : value,
							type : type,
							jsessionid : jsessionid
						},
						url : "../ar/addRemark.do",
						success : function(data) {
							if (data != null && data != "") {
								document.getElementById("remark_" + type + "_"
										+ id).innerHTML = data;
							} else {
								alert("备注添加错误！");
							}
						}
					});
		}
	}
	function queryProjectByPageNumber(pageNumber) {
		if (isNaN(pageNumber)) {
			alert('分页参数不是数字？pageNumber:' + pageNumber);
			return false;
		}
		document.getElementById('pageNum').value = pageNumber;
		document.getElementById('queryForm').submit();
	}
	function restForm(){
		$('#startTime').val('');
		$('#endTime').val('');
		$('#query_userName').val('');
	}
	function submitForm(processType){
		document.getElementById('processType').value=processType;
		document.getElementById('queryForm').submit();
	}
</script>
<style type="text/css">
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
	<div id="title" align="center" style="font-size: 16;font-weight: bold;">考勤统计</div>
	<form action="../ar/QueryARStatis.do" id="queryForm" method="post">
		<input type="hidden" value="${jsessionid}" name="jsessionid" id="jsessionid">
		<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum">
		<input id="processType" name = "processType" value="query" type="hidden">
		<div align="center">
		<fieldset style="width: 93.7%;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">查询</legend>
			<div id ="queryForm" align="center">
			<table style="text-align: center;width: 90%;font-size: 13px;" border="0">
				<tr>
					<td>开始时间：</td>
					<td><input id="startTime" name="starDate" type="text" readonly="readonly" value="${starDate }" ></td>
					<td>结束时间：</td>
					<td><input id="endTime" name="endDate" type="text" readonly="readonly" value="${endDate }" ></td>
					<td>姓名：</td>
					<td><textarea id="query_userName" name="query_userName"style="width:100%;height:22px;font-family: Trebuchet MS;font-size:14px;">${query_userName}</textarea></td>
					<td>
					<input type="button" value="查询" onclick="submitForm('query')">&nbsp;&nbsp;
					<input type="button" onclick="restForm()" value="重置">
					<input type="button" value="导出" onclick="submitForm('reportExcel')">
					</td>
				</tr>
			</table>
			</div>
		</fieldset>
		</div>
	</form>
	<br/>
	<br/>
	<div id="showData"  align="center">
	<fieldset style="text-align: left;width: 93.7%;">
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
		<div style="font-size: 13px;">&nbsp;&nbsp;&nbsp;提示：鼠标放到 <img src="../jsp/images/ar-yes.gif"/> 上可看到 "具体时间" 和 "备注信息" </div>
		<div style="clear: left;">
		<div style="overflow:auto;width: 1088px;">
			<table class="TableList" align="center"  border="1" style="text-align: center;width: 100%;">
				<tr class="TableHeader" style="font-size: 11pt;">
					<td rowspan="2" style="padding: 20px;">姓名</td>
					<c:forEach items="${statisInfoBean.columnList}" var="column" >
					<td colspan="2" style="padding:10px;width: 10px;">
						<fmt:formatDate value="${column}" pattern="MM-dd"/><br>
						<fmt:formatDate value="${column}" pattern="E"/>
					</td>
					</c:forEach>
				</tr>
				<tr style="background: #A2CD5A;">
				<c:forEach items="${statisInfoBean.columnList}" var="column" >
					<td style="height: 1px;padding:0;border: 1px solid #9CB269;">上</td>
					<td style="height: 1px;padding:0;border: 1px solid #9CB269;">下</td>
				</c:forEach>
				</tr>
				<c:forEach items="${statisInfoBean.valueList}" var="infoBean">
				<tr class="TableData">
					<td style="width: 30px;border: 1px solid #CCCCCC;">${infoBean.user.user_name}</td>
					<c:forEach items="${infoBean.arList}" var="ar">
					<td style="border: 1px solid #CCCCCC;">
					<c:choose>
						<c:when test="${ar.on_Date!=null}">
							<img src="../jsp/images/ar-yes.gif"  title="<fmt:formatDate value="${ar.on_Date}" pattern="HH:mm:ss"/>&#13;${ar.on_description}" />
						</c:when>
						<c:otherwise>
							<img src="../jsp/images/ar-no.gif"/>
						</c:otherwise>
					</c:choose>
					</td>
					<!-- <td style="background:#F5F5F5;"> -->
					<td >
					<c:choose>
						<c:when test="${ar.off_Date!=null}">
							<img src="../jsp/images/ar-yes.gif"  title="<fmt:formatDate value="${ar.off_Date}" pattern="HH:mm:ss"/>&#13;${ar.off_description}" />
						</c:when>
						<c:otherwise>
							<img src="../jsp/images/ar-no.gif"/>
						</c:otherwise>
					</c:choose>
					</td>
					</c:forEach>
				</tr>
				</c:forEach>
			</table>
		</div>
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
