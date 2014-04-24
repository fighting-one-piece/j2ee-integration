package org.platform.utils.utils;

import org.junit.Test;
import org.platform.utils.jdbc.JDBCUtils;

public class JDBCUtilsTest {

	@Test
	public void testJDBCInsert() {
		String sql = "insert into user(account,password) values(?, ?)";
		Object[] params = new Object[]{"zhangsan", "zhangsan"};
		JDBCUtils.execute(sql, params);
	}
}
