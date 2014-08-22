package org.platform.utils.bigdata.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.platform.utils.bigdata.AbstrUtils;

@SuppressWarnings("deprecation")
public class HBaseUtils extends AbstrUtils {
	
	private static HBaseAdmin admin = null;
	
	private static HTablePool tablePool = null;
	
	static {
		try {
			admin = new HBaseAdmin(configuration);
			tablePool = new HTablePool(configuration, 10);
			tablePool.close();
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	/** 创建一张表*/
	public static void creatTable(String tableName, String[] familys) {
		try {
			if (admin.tableExists(tableName)) {
				logger.info("table "+ tableName + " already exists!");
			} else {
				HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
				for (int i = 0; i < familys.length; i++) {
					tableDesc.addFamily(new HColumnDescriptor(familys[i]));
				}
				admin.createTable(tableDesc);
				logger.info("create table " + tableName + " success.");
			}
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		} 
	}
	
	/** 表注册Coprocessor*/
	public static void addTableCoprocessor(String tableName, String coprocessorClassName) {
		try {
			admin.disableTable(tableName);
			HTableDescriptor htd = admin.getTableDescriptor(Bytes.toBytes(tableName));
			htd.addCoprocessor(coprocessorClassName);
			admin.modifyTable(Bytes.toBytes(tableName), htd);
			admin.enableTable(tableName);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	/** 统计表行数*/
	public static long rowCount(String tableName, String family) {
		long rowCount = 0;
//		AggregationClient ac = new AggregationClient(configuration);  
//		Scan scan = new Scan();
//		scan.addFamily(Bytes.toBytes(family));
//		try {
//			rowCount = ac.rowCount(Bytes.toBytes(tableName), new LongColumnInterpreter(), scan);
//		} catch (Throwable e) {
//			logger.info(e.getMessage(), e);
//		}  
		return rowCount;
	}
	
	/** 统计表行数*/
	@SuppressWarnings("resource")
	public static long rowCount(String tableName) {
		long rowCount = 0;
		try {
			HTable table = new HTable(configuration, tableName);
			Scan scan = new Scan();
//			scan.setFilter(new KeyOnlyFilter());
			scan.setFilter(new FirstKeyOnlyFilter());
			ResultScanner resultScanner = table.getScanner(scan);
			for (Result result : resultScanner) {
				rowCount += result.size();
			}
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return rowCount;
	}

	/** 删除表*/
	public static void deleteTable(String tableName) {
		try {
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			logger.info("delete table " + tableName + " success.");
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
		} 
	}

	/** 插入一行记录*/
	@SuppressWarnings("resource")
	public static void putRecord(String tableName, String rowKey, String family, String qualifier, String value) {
		try {
			HTable table = new HTable(configuration, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
			table.put(put);
			logger.info("insert recored " + rowKey + " to table " + tableName + " success.");
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	/** 批量插入记录*/
	public static void putRecords(String tableName, List<Put> puts) {
		HTable table = null;
		try {
			table = new HTable(configuration, tableName);
			table.put(puts);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
			try {
				table.flushCommits();
			} catch (Exception e1) {
				logger.info(e1.getMessage(), e1);
			}
		}
	}
	
	/** 删除一行记录*/
	@SuppressWarnings("resource")
	public static void deleteRecord(String tableName, String... rowKeys) {
		try {
			HTable table = new HTable(configuration, tableName);
			List<Delete> list = new ArrayList<Delete>();
			Delete delete = null;
			for (String rowKey : rowKeys) {
				delete = new Delete(rowKey.getBytes());
				list.add(delete);
			}
			if (list.size() > 0) {
				table.delete(list);
			}
			logger.info("delete recoreds " + rowKeys + " success.");
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
	}

	/** 查找一行记录*/
	@SuppressWarnings({ "resource"})
	public static Result getRecord(String tableName, String rowKey) {
		try {
			HTable table = new HTable(configuration, tableName);
			Get get = new Get(rowKey.getBytes());
			get.setMaxVersions();
			return table.get(get);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return null;
	}

	/** 查找所有记录*/
	@SuppressWarnings({"resource" })
	public static ResultScanner getRecords(String tableName) {
		try {
			HTable table = new HTable(configuration, tableName);
			return table.getScanner(new Scan());
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return null;
	}
	
	/** 查找所有记录*/
	@SuppressWarnings({"resource" })
	public static ResultScanner getRecords(String tableName, Scan scan) {
		try {
			HTable table = new HTable(configuration, tableName);
			return table.getScanner(scan);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return null;
	}
	
	/** 查找所有记录*/
	public static ResultScanner getRecords(String tableName, byte[] startRow, byte[] stopRow) {
		return getRecords(tableName, startRow, stopRow, null);
	}
	
	/** 查找所有记录*/
	@SuppressWarnings({"resource" })
	public static ResultScanner getRecords(String tableName, byte[] startRow,
			byte[] stopRow, Filter filter) {
		try {
			HTable table = new HTable(configuration, tableName);
			Scan scan = new Scan();
			if (null != startRow) {
				scan.setStartRow(startRow);
			}
			if (null != stopRow) {
				scan.setStopRow(stopRow);
			}
			if (null != filter) {
				scan.setFilter(filter);
			}
			return table.getScanner(scan);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return null;
	}
	
	public static void printRecord(Result result) {
		for (Cell cell : result.rawCells()) {
			logger.info("cell row: " + new String(cell.getRowArray()));
//			logger.info("cell family: " + new String(cell.getFamilyArray()));
//			logger.info("cell qualifier: " + new String(cell.getQualifierArray()));
//			logger.info("cell value: " + new String(cell.getValueArray()));
//			logger.info("cell timestamp: " + cell.getTimestamp());
		}
		/** 之前版本*/
		/** 
		for (KeyValue kv : rs.raw()) {
			System.out.print(new String(kv.getRow()) + " ");
			System.out.print(new String(kv.getFamily()) + ":");
			System.out.print(new String(kv.getQualifier()) + " ");
			System.out.print(kv.getTimestamp() + " ");
			System.out.println(new String(kv.getValue()));
		}
		*/
	}
	
	public static void printRecords(ResultScanner resultScanner) {
		for (Result result : resultScanner) {
			printRecord(result);
		}
	}

}
