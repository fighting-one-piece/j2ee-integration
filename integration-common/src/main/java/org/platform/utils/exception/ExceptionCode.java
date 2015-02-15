package org.platform.utils.exception;

public class ExceptionCode {

	// 操作成功
	public static final long OPER_SUCCESS = 1;
	// 操作失败
	public static final long OPER_FAILURE = 2;

	// 输入参数为空
	public static final long PARAM_NULL = -101;
	// 输入参数错误
	public static final long PARAM_ERROR = -102;

	// 数据库操作失败
	public static final long DATABASE_ERROR = -203;

	public static final String USER_UNAUTHORIZED = "1001";
}
