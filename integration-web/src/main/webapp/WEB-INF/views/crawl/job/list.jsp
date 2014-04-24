<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script> 
function cutStr(len){ 
    var obj=document.getElementById('table1').getElementsByTagName('td'); 
    for (var i=0;i<obj.length;i++){ 
        obj[i].innerHTML=obj[i].innerHTML.substring(0,len)+'…'; 
    } 
} 
</script> 
</head>

<body>
	<h1 align="center">职位列表</h1> <br/>
	<h3 align="left">
		<a href="${contextURL}/crawl/job/startCrawlData">开始抓取数据</a>
		<a href="${contextURL}/crawl/job/stopCrawlData">停止抓取数据</a>
	</h3> <br/>
	<form action="${contextURL}/crawl/job/search" method="post">
		<input type="hidden" name="index" value="11">
		<table>
			<tr>
				<td><a href="${contextURL}/crawl/job/index?type=11">内存索引</a></td>
				<td><input name="keyword" type="text"/></td>
				<td><input type="submit" value="Search"/></td>
			</tr>
		</table>
	</form>
	<form action="${contextURL}/crawl/job/search" method="post">
		<input type="hidden" name="index" value="21">
		<table>
			<tr>
				<td><a href="index?type=21">文件索引</a></td>
				<td><input name="keyword" type="text"/></td>
				<td><input type="submit" value="Search"/></td>
			</tr>
		</table>
	</form>
	
	<table id="table1" width="100%">
	<c:forEach items="${entities}" var="entity">
			<tr>
				<td width="50%"><a href="${entity.link}">${entity.career}</a></td>
				<td width="50%"><div style="width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${entity.company}</div></td>
			</tr>
			<tr>
				<td width="100%" colspan=2>${entity.require}</td>
			</tr>
			<tr>
				<td width="100%" colspan=2>${entity.summary}</td>
			</tr>
	</c:forEach>
	</table>
	<div align="center">
		<table>
			<tr>
				<td>
					<form action="${contextURL}/crawl/job/search" method="post">
						<input type="hidden" name="index" value="${index}">
						<input type="hidden" name="keyword" value="${keyword}">
						<input type="hidden" name="currentPageNum" value="${currentPageNum - 1}">
						<input type="hidden" name="rowNumPerPage" value="${rowNumPerPage}">
						<table>
							<tr>
								<td><input type="submit" value="上一页"/></td>
							</tr>
						</table>
					</form>
				</td>
				<td>
					--currentPage:${currentPageNum}/totalRowNum:${totalRowNum}--
				</td>
				<td>
					<form action="${contextURL}/crawl/job/search" method="post">
						<input type="hidden" name="index" value="${index}">
						<input type="hidden" name="keyword" value="${keyword}">
						<input type="hidden" name="currentPageNum" value="${currentPageNum + 1}">
						<input type="hidden" name="rowNumPerPage" value="${rowNumPerPage}">
						<table>
							<tr>
								<td><input type="submit" value="下一页"/></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>