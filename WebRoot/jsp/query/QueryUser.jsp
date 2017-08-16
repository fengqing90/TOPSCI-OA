<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<html>
<head>
	<script type="text/javascript" src="../js/jQuery/jquery-2.0.2.min.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.position.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.menu.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript" src="../js/jQuery/ui/i18n/jquery.ui.datepicker-zh-CN.js"></script>
	
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.core.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.theme.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.menu.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.autocomplete.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/navigation/style.css" />
	<link rel="stylesheet" type="text/css" href="../js/jQuery/themes/base/jquery.ui.datepicker.css">
	<link rel="stylesheet" type="text/css" href="../js/jQuery/datepicker/css/datepicker.css">
	<link rel="stylesheet" type="text/css" href="../jsp/css/attendance.css">
	<link rel="stylesheet" type="text/css" href="../jsp/css/paging.css">
	<style type="text/css">
		ul#menu,ul#menu ul {
			width: 100%;
		}
		.TableList {
			border: 0;
		}
		.ui-autocomplete {
			max-height: 500px;
			overflow-y: auto;
			/* prevent horizontal scrollbar; */
			overflow-x: hidden;
		}
	</style>
	
	<script type="text/javascript">
		function process(type,formName){
			if(type=='delete'){
				if(!confirm("确定删除?")){
					return false;
				}
			}
			if(type=='add'){
				if(document.getElementById('userForm_'+formName).alias_name==""){
					alert("账户名不能为空！");
					return false;
				}
			}
			document.getElementById('processType_'+formName).value=type;
			document.getElementById('userForm_'+formName).submit();
		}
		
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}
		function onload() {
			$('#menu ul').hide();
			//$('#menu ul:first').show();
			$('#menu li a').click(function(click) {
 				var user_id = click.target.id.split("_")[1];	
				
				 $("#birthday_"+user_id).datepicker({
					changeMonth: true,
					changeYear: true,
					dateFormat: 'yy-mm-dd',
					showButtonPanel: true
				});
				
				$("#entrant_time_"+user_id).datepicker({
					changeMonth: true,
					changeYear: true,
					dateFormat: 'yy-mm-dd',
					showButtonPanel: true
				});
				
				var checkElement = $(this).next();
				if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
					return false;
				}
				if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
					$('#menu ul:visible').slideUp('normal');
					checkElement.slideDown('normal');
					return false;
				}
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
			 $("#birthday_add").datepicker({
					changeMonth: true,
					changeYear: true,
					dateFormat: 'yy-mm-dd',
					showButtonPanel: true
				});
			
			$("#entrant_time_add").datepicker({
				changeMonth: true,
				changeYear: true,
				dateFormat: 'yy-mm-dd',
				showButtonPanel: true
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
		function resetQueryForm(){
			$('#query_userName').val('');
		}
	</script>
</head>

<body onload="onload()">
<div style="top: 10px; position: absolute; width: 95%;" id="div_body">
	<div id="title"  style="font-size: 16;font-weight: bold;text-align: center;">人员信息维护</div>
	<div id="userQuery"  align="center">
		<fieldset style="width: 93.7%;margin-bottom: 20px;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">查询</legend>
			<div id="div_queryForm"  align="center">
				<form action="../user/QueryUser.do" method="post" id="queryForm" onsubmit="document.getElementById('pageNum').value=1;">
					<span style="font-size: 13px;">姓名:</span>
					<textarea id="query_userName" name="query_userName"style="width:41%;height:22px;font-family: Trebuchet MS;font-size:14px;">${query_userName}</textarea>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="submit" value="查询">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="重置" onclick="resetQueryForm()">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum">
					<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
				</form>
			</div>
		</fieldset>
	</div>
	
	<div id="resultset" align="center">
		<fieldset style="width: 93.7%;margin-bottom: 10px;text-align: left;">
			<legend style="font-weight: bold; font-size: 13px;">查询结果</legend>
			<div id = "pagingDiv" class="pagination" style="margin-bottom: 35px;">
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
			<ul id="menu">
				<li><a href="#" style="font-weight: bold;font-size: 20px; text-align: center;" id="A_add">新增窗口</a>
					<ul>
						<li>
<!-- initadd -->
				<div id="user_add" style="background: #E6F1D8;">
					<form id="userForm_add" action="../user/saveOrUpdate.do" method="post">
						<input type="hidden" name="type" value="QueryUser"/>
						<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
						<fieldset style="margin-bottom: 20px; border: 1px solid #CCCCCC;">
							<legend  style="font-weight: bold; font-size: 13px;">登录信息</legend>
							<table class="TableList">
								<tr>
									<td>账户名：</td>
									<td><input type="text" name="alias_name" value=""/></td>
								</tr>
								<!-- <tr>
									<td>密码 ：</td>
									<td><input type="password" name="user_pwd" value=""/></td>
								</tr> -->
							</table>
						</fieldset>
						<fieldset style=" border: 1px solid #CCCCCC;">
							<legend  style="font-weight: bold; font-size: 13px;">个人信息</legend>
							<table class="TableList" border="0" style="text-align: right;" >
								<tr>
									<td>姓名：</td>
									<td><input type="text" name="user_name"/></td>
									<td>性别：</td>
									<td>
										<select name="sex" style="width:100%">
											<option value="M" label="男">男</option>
											<option value="F" label="女">女</option>
										</select>
									</td>
									<td>电话号码：</td>
									<td><input type="text" name="phone_number"/></td>
									<td>是否离职：</td>
									<td>
										<select name="is_dimission" style="width:100%">
											<option value="Y" label="是">是</option>
											<option value="N" label="否" selected="selected">否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>邮箱：</td>
									<td><input type="text" name="email"/></td>
									<td>生日：</td>
									<td><input type="text" name="birthday" id="birthday_add"  readonly="readonly"/></td>
									<td>入职时间：</td>
									<td><input type="text" name="entrant_time" id="entrant_time_add"  readonly="readonly"/></td>
									<td>人员类型：</td>
									<td>
										<select name="user_type" style="width:100%">
											<option value="ADMIN">管理员</option>
											<option value="EMPLOYE" selected="selected">职工</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>部门：</td>
									<td colspan="5"><input name="user_dept" id="user_dept" style="width: 100%;"/></td>
								</tr>
								<tr>
									<td>住址：</td>
									<td colspan=5><textarea name="address" style="width:100%"></textarea></td>
									<td colspan="2" rowspan="2">
										<input type="button" value="新增" onclick="process('add','add')"  style="width: 100%; height: 80px;"/>
									</td>
								</tr>
								<tr>
									<td rowspan=2 >备注：</td>
									<td rowspan=2 colspan=5><textarea name="remark" style="width:100%"></textarea></td>
								</tr>
							</table>
							<input type="hidden" id="processType_add" name="processType" value="update">
						</fieldset>
					</form>
				</div>
<!-- add -->
						</li>
					</ul>				
				</li>
				<c:if test="${pager.list!=null}">
					<c:forEach var="user" items="${pager.list}">
						<li><a href="#" style="font-weight: bold;font-size: 16px; text-align: left;" id="A_${user.user_id}">${user.user_name}</a>
							<ul>
								<li>
<!-- 动态 -->
				<div id="user_${user_id}" style="background: #EBEBEB;">
					<form id="userForm_${user.user_id}" action="../user/saveOrUpdate.do" method="post">
						<input type="hidden" name="user_id" value="${user.user_id}"/>
						<input type="hidden" name="create_date" value="${user.create_date}"/>
						<input type="hidden" name="type" value="QueryUser"/>
						<input type="hidden" id="jsessionid" name="jsessionid" value="${jsessionid}">
						<input type="hidden" value="${pager.pageNumber}" name="pageNum" id="pageNum_${user.user_id}">
						<fieldset style="margin-bottom: 20px; border: 1px solid #CCCCCC;">
							<legend  style="font-weight: bold; font-size: 13px;">登录信息</legend>
							<table class="TableList">
								<tr>
									<td>账户名：</td>
									<td><input type="text" name="alias_name" value="${user.alias_name}" readonly="readonly"/></td>
								</tr>
								<%-- <tr>
									<td>密码 ：</td>
									<td><input type="password" name="user_pwd" value="${user.user_pwd}"/></td>
								</tr> --%>
							</table>
						</fieldset>
						<fieldset style=" border: 1px solid #CCCCCC;">
							<legend  style="font-weight: bold; font-size: 13px;">个人信息</legend>
							<table class="TableList" border="0" style="text-align: right;" >
								<tr>
									<td>姓名：</td>
									<td><input type="text" name="user_name" value="${user.user_name}"/></td>
									<td>性别：</td>
									<td>
										<select name="sex" style="width:100%">
											<c:choose>
												<c:when test="${user.sex=='M'}">
													<option value="M" label="男" selected="selected">男</option>
												</c:when>
												<c:otherwise>
													<option value="M" label="男">男</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${user.sex=='F'}">
													<option value="F" label="女" selected="selected">女</option>
												</c:when>
												<c:otherwise>
													<option value="F" label="女">女</option>
												</c:otherwise>
											</c:choose>
										</select>
									</td>
									<td>电话号码：</td>
									<td><input type="text" name="phone_number" value="${user.phone_number}"/></td>
									<td>是否离职：</td>
									<td>
										<select name="is_dimission" style="width:100%">
											<c:choose>
												<c:when test="${user.is_dimission=='Y'}">
													<option value="Y" label="是" selected="selected">是</option>
												</c:when>
												<c:otherwise>
													<option value="Y" label="是" >是</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${user.is_dimission=='N'}">
													<option value="N" label="否" selected="selected">否</option>
												</c:when>
												<c:otherwise>
													<option value="N" label="否">否</option>
												</c:otherwise>
											</c:choose>
										</select>
									</td>
								</tr>
								<tr>
									<td>邮箱：</td>
									<td><input type="text" name="email" value="${user.email}"/></td>
									<td>生日：</td>
									<td><input type="text" name="birthday" id="birthday_${user.user_id}" value="<fmt:formatDate value="${user.birthday}" type="date" pattern="yyyy-MM-dd" />" readonly="readonly"/></td>
									<td>入职时间：</td>
									<td><input type="text" name="entrant_time" id="entrant_time_${user.user_id}" value="<fmt:formatDate value="${user.entrant_time}" type="date" pattern="yyyy-MM-dd"/>" readonly="readonly"/></td>
									<td>人员类型：</td>
									<td>
										<select name="user_type" style="width:100%">
											<c:choose>
												<c:when test="${user.user_type=='ADMIN'}">
													<option value="ADMIN" selected="selected">管理员</option>
												</c:when>
												<c:otherwise>
													<option value="ADMIN">管理员</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${user.user_type=='EMPLOYE'}">
													<option value="EMPLOYE" selected="selected">职工</option>
												</c:when>
												<c:otherwise>
													<option value="EMPLOYE">职工</option>
												</c:otherwise>
											</c:choose>
										</select>
									</td>
								</tr>
								<tr>
									<td>部门：</td>
									<td colspan="5"><input name="user_dept" id="user_dept" style="width: 100%;" value="${user.user_dept}"/></td>
								</tr>
								<tr>
									<td>住址：</td>
									<td colspan=5><textarea name="address" style="width:100%">${user.address}</textarea></td>
									<td colspan="2">
										<input type="button" value="修改" onclick="process('update','${user.user_id}')" style="width: 100%; height: 30px;"/>
									</td>
								</tr>
								<tr>
									<td rowspan=2 >备注：</td>
									<td rowspan=2 colspan=5><textarea name="remark" style="width:100%">${user.remark}</textarea></td>
									<td colspan="2">
										<input type="button" value="删除" onclick="process('delete','${user.user_id}')" style="width: 100%; height: 30px;"/>
									</td>
								</tr>
							</table>
							<input type="hidden" id="processType_${user.user_id}" name="processType" value="update">
						</fieldset>
					</form>
				</div>
<!--  -->
								</li>
							</ul>
						</li>
					</c:forEach>
				</c:if>
				<%-- <li><a href="#">工时</a>
					<ul>
						<li><a href="javascript:forword('../work/initWorkForm.do')">工时登记</a></li>
						<li><a href="javascript:forword('../work/QueryWork.do')">工时查看</a></li>
						<li><a href="javascript:forword('../work/QueryWorkPercentage.do')">工时统计</a></li>
					</ul>
				</li>
				<li><a href="#">人员维护</a>
					<ul>
						<li><a href="javascript:forword('../user/initUserForm.do')">个人信息</a></li>
						<c:if test="${user.user_type=='ADMIN'}">
						<li><a href="javascript:forword('../user/QueryUser.do')">人员维护</a></li>
						</c:if>
					</ul>
				</li> --%>
			</ul>
		</fieldset>
	</div>
	</div>
</body>
</html>
