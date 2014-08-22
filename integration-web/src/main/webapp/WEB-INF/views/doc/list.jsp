<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Doc List</title>
</head>
<body>
	Doc List Page <br/>
	<c:forEach items="${entities}" var="entity">
		----${entity.autoid}----${entity.channel}-----${entity.docno}-----${entity.title}----${entity.url}----${entity.ptime}----${entity.createtime}
		<a href="user/${entity.id}/update">update entity</a>
		<a href="user/${entity.id}/delete">delete entity</a>
		<br/>
	</c:forEach>
	<a href="?currentPageNum=${currentPageNum - 1} && rowNumPerPage = ${rowNumPerPage}">prePage</a>
	--currentPage:${currentPageNum}/totalRowNum:${totalRowNum}--
	<a href="?currentPageNum=${currentPageNum + 1} && rowNumPerPage = ${rowNumPerPage}">nextPage</a>
</body>
</html>