package org.platform.utils.bigdata;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.platform.utils.bigdata.sqoop.SqoopUtils;

public class SqoopUtilsTest {

	@Test
	public void testDB2HDFS() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(SqoopUtils.CONNECT, "jdbc:mysql://192.168.10.10:3306/test");
		parameters.put(SqoopUtils.USERNAME, "root");
		parameters.put(SqoopUtils.PASSWORD, "123456");
		parameters.put(SqoopUtils.TABLE, "movies");
		parameters.put(SqoopUtils.TARGET_DIR, "/tmp/movies");
		SqoopUtils.importDB(parameters);
	}
	
	// bin/sqoop import --connect jdbc:mysql://localhost:3306/test --username root --password 123456 --table movies --hive-import --create-hive-table --hive-table moviessss^C
}
