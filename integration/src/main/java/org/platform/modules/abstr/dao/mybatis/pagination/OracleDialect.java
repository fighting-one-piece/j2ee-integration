package org.platform.modules.abstr.dao.mybatis.pagination;

public class OracleDialect implements Dialect {

	@Override
	public String obtainPageSql(String sql, int offset, int limit) {
		return null;
	}

}
