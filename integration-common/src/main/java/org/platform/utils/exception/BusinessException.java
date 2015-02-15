package org.platform.utils.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Long code = null;

	private String message = null;

	public BusinessException(Long code) {
		this.code = code;
	}

	public BusinessException(String message) {
		this.message = message;
	}

	public BusinessException(Long code, String message) {
		this.code = code;
		this.message = message;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "exception code:" + code + ", message: " + message;
	}


}
