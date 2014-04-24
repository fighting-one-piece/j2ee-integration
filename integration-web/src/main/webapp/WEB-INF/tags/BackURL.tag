<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ tag import="org.platform.modules.front.Constants" %>
<%@ tag import="org.apache.commons.lang.StringUtils" %>
<%@ attribute name="hiddenInput" type="java.lang.Boolean" required="false" description="是否是隐藏展示" %><%
    if(!StringUtils.isEmpty(request.getParameter(Constants.IGNORE_BACK_URL))) {
        return;
    }
    String backURL = (String) request.getAttribute(Constants.BACK_URL);
    if(hiddenInput != null && hiddenInput.equals(Boolean.TRUE)) {
        out.write("<input type=\"hidden\" name=\"" + Constants.BACK_URL + "\" value=\"" + backURL + "\">");
    } else {
        out.write(backURL);
    }
    return;
%>