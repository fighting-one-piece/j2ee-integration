package org.platform.modules.front.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.platform.utils.web.request.LogUtils;

/**
 * 记录访问日志
 */
public class AccessLogFilter extends GenericFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LogUtils.logAccess(request);
//        String rowkey = String.valueOf(System.currentTimeMillis());
//        HBaseUtils.putRecord("user", rowkey, "logininfo", "ip", IpUtils.getIpAddr(request));
//        HBaseUtils.putRecord("user", rowkey, "logininfo", "url", request.getRequestURI());
        chain.doFilter(request, response);
    }


}
