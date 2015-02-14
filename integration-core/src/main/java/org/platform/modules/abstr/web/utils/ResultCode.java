package org.platform.modules.abstr.web.utils;

public enum ResultCode {

	SUCCESS(1, "操作成功"),
	FAILURE(2, "操作失败"),
	PARA_NULL(-101,"参数为空"),
	POSTDATA_ERROR(-102,"请求数据错误或非法请求"),
	URL_ERROR(-103,"页面不存在"),
	ASYNCHRONOUS_MEMORY(-104,"异步存储到个人页错误"),
	CLOSED(-105,"此模块已关闭"),
	CONSTRUCTIONING(-106,"此模块正在建设中"),
	REVIEWING(-107,"内容正在审核中"),
	DELETED(-108,"内容已被删除"),
	VERIFY_ERROR(-109,"参数验证失败"),
	
	DATABASE_CONNECTION_FAIL(-201,"数据库连接失败"),
	DATABASE_READ_FAIL(-202,"数据库读取失败"),
	DATABASE_OPERATION_FAIL(-203,"数据库操作失败"),
	
	SESSION_EXPIRED(-301,"会话已过期"),
	NO_AUTHORIZATION(-302,"用户没有权限"),
	LOGIN_FAIL(-303,"用户登录失败"),
	NO_EXIST(-304,"用户不存在"),
	NO_LOGGED(-305,"用户未登录"),
	VERIFICATION_CODE_ERROR(-306,"验证码错误"),
	VERIFICATION_CODE_FAIL(-307,"获取验证码失败"),
	REGISTER_FAIL(-308,"用户注册失败"),
	NAME_EXIST(-309,"用户昵称已存在"),
	
	OAUTH_LOGIN_FAIL(-400,"第三方登录失败"),
	OAUTH_BIND_FAIL(-401,"绑定失败"),
	OAUTH_PASSWORD_INCORRECT(-402,"密码错误"),
	OAUTH_BIND_CANNOT(-403,"不能绑定"),
	OAUTH_BIND_ALREADY(-404,"已绑定同类型账户"),
	OAUTH_BIND_ALREADY_PASSPORT(-405,"已绑定过网易通行证"),
	OAUTH_BIND_ALREADY_TYPE(-406,"已绑定过此类第三方账户"),
	OAUTH_BIND_ALREADY_OAUTH(-407,"该第三方账户已绑定过"),
	OAUTH_BIND_ONLY_NEW_PASSPORT(-408,"只能绑定未使用过网易热的网易通行证"),
	OAUTH_BIND_NO_PASSPORT(-409,"只有绑定过网易通行证才能解绑或绑定其他第三方"),
	OAUTH_BIND_NO_EXIST(-410,"不存在此种绑定关系"),
	OAUTH_REMOVE_BIND_FAIL(-411,"解绑失败"),
	OAUTH_FOR_APP(-412,"不是APP客户端调用"),
	
	VERIFICATION_SUCCESS(200,"验证通过成功返回"),
	VERIFICATION_FAIL(400,"验证失败，cookie或token值无效"),
	VERIFICATION_URL_ERROR(401,"URL语法错误或参数值非法，或access_token无效"),
	VERIFICATION_NO_IP_PERIMISSION(410,"服务器没有开通接口的IP权限"),
	VERIFICATION_NO_EXIST(420,"用户不存在"),
	VERIFICATION_ID_INVALID(427,"id参数无效，需要重新初始化id"),
	VERIFICATION_SERVER_ERROR(500,"服务器端错误"),
	VERIFICATION_SERVER_UNDER_MAINTENANCE(503,"服务器正在维护");
	
	/** 代码值*/
	private int code = 0;
	/** 代码值描述*/
	private String desc = null;
	
	private ResultCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
