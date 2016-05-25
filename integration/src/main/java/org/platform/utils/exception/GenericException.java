package org.platform.utils.exception;

import org.apache.commons.lang.StringUtils;
import org.platform.utils.message.MessageUtils;

/**
 * 基础异常
 */
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 所属模块
	 */
    private String module = null;

    /**
     * 错误码
     */
    private String code = null;

    /**
     * 错误码对应的参数
     */
    private Object[] args = null;

    /**
     * 错误消息
     */
    private String defaultMessage = null;

    public GenericException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public GenericException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public GenericException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public GenericException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public GenericException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.getMessage(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return this.getClass() + "{" +
                "module='" + module + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
