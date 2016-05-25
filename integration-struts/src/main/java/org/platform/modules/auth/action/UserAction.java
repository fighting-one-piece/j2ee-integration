package org.platform.modules.auth.action;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	public String list() {
		System.out.println("user list");
		return SUCCESS;
	}

}
