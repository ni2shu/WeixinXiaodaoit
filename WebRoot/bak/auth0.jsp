<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=no" />
</head>
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String open_id = request.getParameter("oauth").trim();// 得到OpenID
%>
<br/>
<form name="auth" action="AuthServlet" method="post">
学号：
<input type="text" name="stuid" id="stuid" />
<br />
密码：
<input type="password" name="pwd" id="pwd" />
<br />
<input type="hidden" name="openID" id="openID" value="<%=open_id%>" />
<br />
<input name="submit" type="submit" id="submit" value="绑定" />
<br />
<p>
提醒：密码要和你登录教务处网站的密码一样哦~
</p>
</form>
</body>
</html>