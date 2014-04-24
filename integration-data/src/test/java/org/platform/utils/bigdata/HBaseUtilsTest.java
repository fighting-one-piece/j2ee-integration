package org.platform.utils.bigdata;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.junit.Test;
import org.platform.utils.bigdata.hbase.HBaseUtils;

public class HBaseUtilsTest {

	@Test
	public void test() {
		try {
			String tablename = "scores";
			HBaseUtils.deleteTable(tablename);
			String[] familys = { "grade", "course" };
			HBaseUtils.creatTable(tablename, familys);

			// add record zkb
			HBaseUtils.putRecord(tablename, "zkb", "grade", "", "5");
			HBaseUtils.putRecord(tablename, "zkb", "course", "", "90");
			HBaseUtils.putRecord(tablename, "zkb", "course", "math", "97");
			HBaseUtils.putRecord(tablename, "zkb", "course", "art", "87");
			// add record baoniu
			HBaseUtils.putRecord(tablename, "baoniu", "grade", "", "4");
			HBaseUtils.putRecord(tablename, "baoniu", "course", "math", "89");

			System.out.println("===========get one record========");
			Result result = HBaseUtils.getRecord(tablename, "zkb");
			HBaseUtils.printRecord(result);

			System.out.println("===========show all record========");
			HBaseUtils.getRecords(tablename);

			System.out.println("===========del one record========");
			HBaseUtils.deleteRecord(tablename, "baoniu");
			ResultScanner resultScanner = HBaseUtils.getRecords(tablename);
			HBaseUtils.printRecords(resultScanner);

			System.out.println("===========show all record========");
			HBaseUtils.getRecords(tablename);
			HBaseUtils.printRecords(resultScanner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGet() {
		HBaseUtils.printRecord(HBaseUtils.getRecord("user", "user-login-1"));
	}
}
