<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="co.xiaodao.weixin.db.util.BaseDBUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>绑定学号</title>
		<base href="http://<%=BaseDBUtil.WEB_HOSE%>/WeixinXiaodaoit/">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta name="viewport"
			content="initial-scale=1.0; maximum-scale=1.0; user-scalable=no;" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script src="resources/js/jquery-1.4.4.js" type="text/javascript"></script>
		<script src="resources/js/ajax-jquery-plugin.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="resources/css/weixin.css">
		<script type="text/javascript">
	function DoBind() {
		var $stuid = $("#stuid");
		var $pwd = $("#pwd");
		var $authForm = $("#authForm");
		if (CheckBind($stuid, $pwd)) {
			$authForm.submit();
		}
	}

	function CheckBind($stuid, $pwd) {
		var reg = /^20\d{10}$/;
		if (!reg.test($stuid.val())) {
			alert("请输入正确的学号!");
			$stuid.focus();
			return false;
		} else if ($pwd.val().trim().length > 10) {
			alert("请输入正确的密码!");
			$pwd.focus();
			return false;
		} else {
			return true;
		}
	}
</script>
	</head>
	<body>
		<%
			request.setCharacterEncoding("UTF-8");
			String open_id = request.getParameter("oauth").trim();// 得到OpenID
		%>
		<form method="post" name="authForm" id="authForm" action="AuthServlet">

			<div id="mappContainer">
				<div class="inner">
					<div>
					</div>
					<ul class="round">
						<li class="addr">
							<div class="textinput">
								<table class="InputLine">
									<tr>
										<td class="left" align="right">
											学号:
										</td>
										<td class="right" align="left">
											<input type="text" id="stuid" name="stuid" class="input90"
												nullable="no" maxsize="11" />
										</td>
									</tr>
								</table>
							</div>
						</li>
						<li class="tel">
							<div class="textinput">
								<table class="InputLine">
									<tr>
										<td class="left" align="right">
											密码:
										</td>
										<td class="right" align="left">
											<input type="password" id="pwd" name="pwd"
												placeholder="登录教务处网站的密码" class="input90" nullable="no"
												minsize="6" />
										</td>
									</tr>
								</table>
							</div>
						</li>
					</ul>
					<input type="hidden" name="openID" id="openID" value="<%=open_id%>" />
					<div class="submit_div">
						<input name="bind" type="button" id="bind"
							value="绑&nbsp;&nbsp;&nbsp;&nbsp;定" onClick="DoBind()">
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
