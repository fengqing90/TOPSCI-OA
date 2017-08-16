<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.topsci.bean.AttendanceRecords"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
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
		<link rel="stylesheet" type="text/css" href="../jsp/css/paging.css">
		<script type="text/javascript">
			var jsessionid ='<%=request.getParameter("jsessionid") %>';
			function onload(){
				
				$( "#startTime" ).datepicker({
					changeMonth: true,
					changeYear: true,
					dateFormat: 'yy-mm-dd',
					showButtonPanel: true
				});
				
				$( "#endTime" ).datepicker({
					changeMonth: true,
					changeYear: true,
					dateFormat: 'yy-mm-dd',
					showButtonPanel: true
				});
			}
			
			function remark(id,type){
				var value = prompt("备注信息：",'');
				if(value!=null && value!=""){
					$.ajax({
						type:"POST",
						data:{
							ar_id : id,
							remark:value,
							type : type,
							jsessionid:jsessionid
						},
						url:"../ar/addRemark.do",
						success:function(data){
							if(data!=null && data!=""){
								document.getElementById("remark_"+type+"_"+id).innerHTML=data;
							}else{
								alert("备注添加错误！");
							}
						}
					});
				}
			}
			function queryProjectByPageNumber(pageNumber){
				if(isNaN(pageNumber)){
					alert('分页参数不是数字？pageNumber:'+pageNumber);
					return false;
				}
				document.getElementById('pageNum').value=pageNumber;
				document.getElementById('queryForm').submit();
			}
			function restForm(){
				$('#startTime').val('');
				$('#endTime').val('');
			}
		</script>
		<style type="text/css">
		.TableList .TableData td, .TableList td.TableData{
			border: 0
		}
		</style>
	</head>
	<body onload="onload()">
		<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
		<div id="title" align="center" style="font-size: 16;font-weight: bold;">考勤查看</div>
			<form action="QueryAR.do" id="queryForm">
				<input type="hidden" value="${jsessionid}" name="jsessionid" id="jsessionid">
				<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum">
				<div align="center">
				<fieldset style="width: 93.7%;text-align: left;">
					<legend style="font-weight: bold; font-size: 13px;">查询</legend>
					<div id ="queryForm" align="center">
					<table style="text-align: center;width: 55%;font-size: 13px;" border="0">
						<tr>
							<td>开始时间：</td>
							<td><input id="startTime" name="starDate" type="text" readonly="readonly" value="${starDate }" ></td>
							<td>结束时间：</td>
							<td><input id="endTime" name="endDate" type="text" readonly="readonly" value="${endDate }" ></td>
							<td><input type="submit" value="查询">&nbsp;&nbsp;<input type="button" value="重置" onclick="restForm()"></td>
						</tr>
					</table>
					</div>
				</fieldset>
				</div>
			</form>
		<br/>
		<br/>
		<div id="showData"  align="center">
			<fieldset style="width: 93.7%;text-align: left;">
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
				<table class="TableList" align="center" width="100%" border="0">
				<tr class="TableHeader">
					<td nowrap align="center" style="width: 10%;">登记日期</td>
					<td nowrap align="center" style="width: 10%;">登记类型</td>
					<td nowrap align="center">规定时间</td>
					<td nowrap align="center">登记时间</td>
					<td nowrap align="center" style="width: 30%;" colspan="2">操作</td>
				</tr>
				<c:if test="${pager.list!=null}">
					<c:forEach items="${pager.list}" var="attendance" varStatus="s">
					<%-- <c:out value="${s.index}"></c:out> --%>
						<tr class="TableData">
							<td nowrap align="center">
								<fmt:formatDate value="${attendance.create_date}" type="date" pattern="yyyy-MM-dd" />
							</td>
							<td nowrap align="center">上班登记</td>
							<td nowrap align="center">09:00:00</td>
							<td nowrap align="center">
								<c:choose>
									<c:when test="${attendance.on_Date==null}">
										未登记
									</c:when>
									<c:otherwise>
										<fmt:formatDate value="${attendance.on_Date}" type="date" pattern="HH:mm:ss" />
										<c:if test="${attendance.is_enable_on=='L'}">
											<span class=big4>迟到</span>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
							<td nowrap align="center">
								<c:if test="${attendance.on_description!=null}">
									<div id="remark_ON_${attendance.ar_id}">${attendance.on_description}</div>
								</c:if>
							</td>
							<td style="width: 7%;">
								<a href="javascript:remark('${attendance.ar_id}','ON');">说明情况</a>
							</td>
						</tr>
						<tr class="TableData">
							<td nowrap align="center">
								<fmt:formatDate value="${attendance.create_date}" type="date" pattern="yyyy-MM-dd" />
							</td>
							<td nowrap align="center">下班登记</td>
							<td nowrap align="center">18:00:00</td>
							<td nowrap align="center">
								<c:choose>
									<c:when test="${attendance.off_Date==null}">
										未登记
									</c:when>
									<c:otherwise>
										<fmt:formatDate value="${attendance.off_Date}" type="date" pattern="HH:mm:ss"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td nowrap align="center">
								<div id="remark_OFF_${attendance.ar_id}">
									<c:if test="${attendance.off_description!=null}">
										${attendance.off_description}
									</c:if>
								</div>
							</td>
							<td>
								<c:if test="${attendance.off_Date!=null}">
									<a href="javascript:remark('${attendance.ar_id}','OFF');">说明情况</a>
								</c:if>
							</td>
						</tr>
						<tr style="height: 2px;">
						</tr>
					</c:forEach>
				</c:if>
			</table>
			</fieldset>
		</div>
		</div>
	</body>
</html>
