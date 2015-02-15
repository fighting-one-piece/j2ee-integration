package org.platform.utils.exception;

/** 数据访问异常*/
public class BusinessAccessException extends GenericException {

	private static final long serialVersionUID = 1L;

	public BusinessAccessException(String module, String code, Object[] args, String defaultMessage) {
		super(module, code, args, defaultMessage);
    }
	
	public BusinessAccessException(String module, String code, Object[] args) {
        super(module, code, args, null);
    }

    public BusinessAccessException(String module, String defaultMessage) {
    	super(module, null, null, defaultMessage);
    }

    public BusinessAccessException(String code, Object[] args) {
    	super(null, code, args, null);
    }

    public BusinessAccessException(String defaultMessage) {
    	super(null, null, null, defaultMessage);
    }

}
