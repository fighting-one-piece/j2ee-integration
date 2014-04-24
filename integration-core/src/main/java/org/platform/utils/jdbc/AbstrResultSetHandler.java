package org.platform.utils.jdbc;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

public abstract class AbstrResultSetHandler implements ResultSetHandler{
	
	protected Logger logger = Logger.getLogger(getClass());
	
	protected Class<?> clazz = null;
	
	protected void preHandle() {
		if (null == clazz) {
			logger.error("clazz is null");
			throw new RuntimeException("clazz is null");
		}
	}
	
	protected void postHandle() {
		
	}
	
	protected abstract Object doHandle(ResultSet resultSet);

	@Override
	public Object handle(ResultSet resultSet) {
		preHandle();
		return doHandle(resultSet);
	}

}
