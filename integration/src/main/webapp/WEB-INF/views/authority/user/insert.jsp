<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>add user</title>
</head>
<body>
	<f:form method="post" modelAttribute="entity">
		name:<f:input path="name"/><f:errors path="name"/><br></br>
		password:<f:password path="password"/><f:errors path="password"/><br></br>
		identiy:<f:input path="identity"/><f:errors path="code"/><br></br>
		<input type="submit"/>
	</f:form>
</body>
</html>