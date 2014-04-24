<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	User List Page 
	<a href="user/insert">add user</a>------
	<a href="user/login">Login</a><br/>
	Current Login User : ${loginUser.code}<br/>
	<c:forEach items="${entities}" var="user">
		${user.name}-----${user.password}-----${user.identity}
		<a href="user/${user.id}/update">update user</a>
		<a href="user/${user.id}/delete">delete user</a>
		<br/>
	</c:forEach>
	<a href="?currentPageNum=${currentPageNum - 1} && rowNumPerPage = ${rowNumPerPage}">prePage</a>
	--currentPage:${currentPageNum}/totalRowNum:${totalRowNum}--
	<a href="?currentPageNum=${currentPageNum + 1} && rowNumPerPage = ${rowNumPerPage}">nextPage</a>
</body>
</html>