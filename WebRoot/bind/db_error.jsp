<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="co.xiaodao.weixin.db.util.BaseDBUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>服务器错误</title>
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
	</head>
	<body>
		<div id="mappContainer">
			<div class="inner">
				<div>
				</div>
				<ul class="round">
					<li class="tel">
						<div class="textinput">
							<table class="InputLine">
								<tr>
									<td class="left" align="left">
										服务器罢工了，请联系开发者微信号：cdztop
									</td>
								</tr>
							</table>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</body>
</html>
