<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.Exception" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error Page</title>
</head>
<body>
	<h1>错误信息</h1>
	<%
		Object object = request.getAttribute("exception");
		if (null != object) {
			Exception exception = (Exception) object;
			exception.printStackTrace();
		}
	%>
	<%-- 	
	<H2>异常:<%= exception.getMessage()%></H2>  
	--%>
	<P/> 
</body>
</html>