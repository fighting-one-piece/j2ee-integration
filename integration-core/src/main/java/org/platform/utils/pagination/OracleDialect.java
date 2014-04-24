package org.platform.utils.pagination;

public class OracleDialect implements Dialect {

	@Override
	public String obtainPageSql(String sql, int offset, int limit) {
		return null;
	}

}
