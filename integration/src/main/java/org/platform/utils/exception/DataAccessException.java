package org.platform.utils.exception;

/** 数据访问异常*/
public class DataAccessException extends GenericException {

	private static final long serialVersionUID = 1L;

	public DataAccessException(String module, String code, Object[] args, String defaultMessage) {
		super(module, code, args, defaultMessage);
    }
	
	public DataAccessException(String module, String code, Object[] args) {
        super(module, code, args, null);
    }

    public DataAccessException(String module, String defaultMessage) {
    	super(module, null, null, defaultMessage);
    }

    public DataAccessException(String code, Object[] args) {
    	super(null, code, args, null);
    }

    public DataAccessException(String defaultMessage) {
    	super(null, null, null, defaultMessage);
    }

}
