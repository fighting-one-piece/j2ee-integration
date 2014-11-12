package org.platform.utils.bigdata;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
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
			HBaseUtils.insertRecord(tablename, "zkb", "grade", "", "5");
			HBaseUtils.insertRecord(tablename, "zkb", "course", "", "90");
			HBaseUtils.insertRecord(tablename, "zkb", "course", "math", "97");
			HBaseUtils.insertRecord(tablename, "zkb", "course", "art", "87");
			// add record baoniu
			HBaseUtils.insertRecord(tablename, "baoniu", "grade", "", "4");
			HBaseUtils.insertRecord(tablename, "baoniu", "course", "math", "89");

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
	public void testCreateTable() {
		HBaseUtils.creatTable("user", new String[]{"basic","detail"});
	}
	
	@Test
	public void testInsert() {
		HBaseUtils.insertRecord("user", "0120140722a", "basic", "name", "user01");
		HBaseUtils.insertRecord("user", "0220140722b", "basic", "name", "user02");
		HBaseUtils.insertRecord("user", "0320140723c", "basic", "name", "user03");
		HBaseUtils.insertRecord("user", "0420140724d", "basic", "name", "user04");
		HBaseUtils.insertRecord("user", "0520140725e", "basic", "name", "user05");
		HBaseUtils.insertRecord("user", "0620140726f", "basic", "name", "user06");
		HBaseUtils.insertRecord("user", "0720140727g", "basic", "name", "user07");
		HBaseUtils.insertRecord("user", "0820140728h", "basic", "name", "user08");
		HBaseUtils.insertRecord("user", "0920140729i", "basic", "name", "user09");
		HBaseUtils.insertRecord("user", "1020140722j", "basic", "name", "user10");
		HBaseUtils.insertRecord("user", "1120140723k", "basic", "name", "user11");
		HBaseUtils.insertRecord("user", "1220140724l", "basic", "name", "user12");
		HBaseUtils.insertRecord("user", "1320140721m", "basic", "name", "user13");
		HBaseUtils.insertRecord("user", "1420140721n", "basic", "name", "user14");
		HBaseUtils.insertRecord("user", "1520140722o", "basic", "name", "user15");
		HBaseUtils.insertRecord("user", "1620140728p", "basic", "name", "user16");
		HBaseUtils.insertRecord("user", "1720140725q", "basic", "name", "user17");
		HBaseUtils.insertRecord("user", "0120140722a", "basic", "age", "17");
		HBaseUtils.insertRecord("user", "0220140722b", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "0320140723c", "basic", "age", "19");
		HBaseUtils.insertRecord("user", "0420140724d", "basic", "age", "17");
		HBaseUtils.insertRecord("user", "0520140725e", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "0620140726f", "basic", "age", "17");
		HBaseUtils.insertRecord("user", "0720140727g", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "0820140728h", "basic", "age", "19");
		HBaseUtils.insertRecord("user", "0920140729i", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1020140722j", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1120140723k", "basic", "age", "19");
		HBaseUtils.insertRecord("user", "1220140724l", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1320140721m", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1420140721n", "basic", "age", "17");
		HBaseUtils.insertRecord("user", "1520140722o", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1620140728p", "basic", "age", "18");
		HBaseUtils.insertRecord("user", "1720140725q", "basic", "age", "19");
	}
	
	@Test
	public void testGetWithStartAndStop() {
		Scan scan = new Scan();
		scan.setStartRow("20140722".getBytes());
		scan.setStopRow("20140725".getBytes());
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithStartAndStopAndFilter() {
		Scan scan = new Scan();
		scan.setStartRow("0120140722".getBytes());
		scan.setStopRow("1720140725".getBytes());
		scan.setFilter(new PrefixFilter("0".getBytes()));
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithFilter() {
		Scan scan = new Scan();
		scan.setFilter(new KeyOnlyFilter());
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithFilter1() {
		Scan scan = new Scan();
		scan.setFilter(new FirstKeyOnlyFilter());
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithFilter2() {
		Scan scan = new Scan();
		scan.setFilter(new RowFilter(CompareOp.EQUAL, new RegexStringComparator("")));
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithFilter3() {
		Scan scan = new Scan();
		scan.setFilter(new RowFilter(CompareOp.EQUAL, 
				new RegexStringComparator("\\w{2}2014072[45]+")));
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGetWithFilter4() {
		Scan scan = new Scan();
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		filterList.addFilter(new RowFilter(CompareOp.GREATER_OR_EQUAL, 
				new RegexStringComparator("\\w{2}20140724\\w")));
		filterList.addFilter(new RowFilter(CompareOp.LESS_OR_EQUAL, 
				new RegexStringComparator("\\w{2}20140727\\w")));
		scan.setFilter(filterList);
		ResultScanner resultScanner = HBaseUtils.getRecords("user", scan);
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testTableRowCountFilter() {
		long rowCount = HBaseUtils.rowCount("user");
		System.out.println("rowCount: " + rowCount);
	}
	
	@Test
	public void testTableRowCount() {
		String coprocessorClassName = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
		HBaseUtils.addTableCoprocessor("user", coprocessorClassName);
		long rowCount = HBaseUtils.rowCount("user", "basic");
		System.out.println("rowCount: " + rowCount);
	}
	
	@Test
	public void testScan() {
		ResultScanner resultScanner = HBaseUtils.getRecords("scores");
		HBaseUtils.printRecords(resultScanner);
	}
	
	@Test
	public void testGet() {
		HBaseUtils.printRecord(HBaseUtils.getRecord("user", "user-login-1"));
	}
}
