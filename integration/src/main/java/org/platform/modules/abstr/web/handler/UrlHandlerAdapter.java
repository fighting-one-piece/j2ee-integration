package org.platform.modules.abstr.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

public class UrlHandlerAdapter implements HandlerAdapter {

	@Override
	public long getLastModified(HttpServletRequest arg0, Object arg1) {
		return -1;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		System.out.println("request servlet path: " + request.getServletPath());
		System.out.println("request uri: " + request.getRequestURI());
		return null;
	}

	@Override
	public boolean supports(Object arg0) {
		return true;
	}

}
