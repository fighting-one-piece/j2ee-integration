package org.platform.utils;

import org.platform.modules.auth.entity.User;

/**
 * CMS线程变量
 */
public class PlatThreadVariable {
	/**
	 * 当前用户线程变量
	 */
	private static ThreadLocal<User> userVariable = new ThreadLocal<User>();

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public static User getUser() {
		return userVariable.get();
	}

	/**
	 * 设置当前用户
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		userVariable.set(user);
	}

	/**
	 * 移除当前用户
	 */
	public static void removeUser() {
		userVariable.remove();
	}

}
