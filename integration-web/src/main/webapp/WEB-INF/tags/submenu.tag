<%@tag pageEncoding="UTF-8" description="构建子菜单" %>
<%@ attribute name="menu" type="org.platform.modules.auth.entity.Resource" required="true" description="当前菜单" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="es" tagdir="/WEB-INF/tags" %>
<c:choose>
    <c:when test="${!menu.hasChildren}">
        <li><a href="<%=handle(request, menu.getUrl())%>">${menu.name}</a></li>
<%--         <li><a href="${contextURL}${menu.url}">${menu.name}</a></li> --%>
    </c:when>
    <c:otherwise>
        <li>
            <a href="">${menu.name}</a>
            <ul>
                <c:forEach items="${menu.children}" var="subMenu">
                    <es:submenu menu="${subMenu}"/>
                </c:forEach>
            </ul>
        </li>
    </c:otherwise>
</c:choose>

<%!
    private static String handle(HttpServletRequest request, String url) {
        if(url.startsWith("http")) {
            return url;
        }
        String ctx = request.getContextPath();
        if(url.startsWith(ctx) || url.startsWith("/" + ctx  )) {
            return url;
        }
        if(!url.startsWith("/")) {
            url = url + "/";
        }
        return ctx + url;
    }
%>

