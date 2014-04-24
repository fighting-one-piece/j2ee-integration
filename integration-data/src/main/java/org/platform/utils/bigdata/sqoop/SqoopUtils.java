package org.platform.utils.bigdata.sqoop;

import java.util.ArrayList;
import java.util.Map;

import org.apache.sqoop.Sqoop;
import org.apache.sqoop.tool.ExportTool;
import org.apache.sqoop.tool.ImportTool;
import org.platform.utils.bigdata.AbstrUtils;

public class SqoopUtils extends AbstrUtils {
	
	public static final String M = "m";
	public static final String Z = "z";
	public static final String APPEND = "append";
	public static final String CONNECT = "connect";
	public static final String DRIVER_CLASS_NAME = "driver_class_name";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String TABLE = "table";
	public static final String COLUMNS = "columns";
	public static final String QUERY = "query";
	public static final String WHERE = "where";
	public static final String TARGET_DIR = "target_dir";
	public static final String SPLIT_BY = "split_by";
	public static final String FIELDS_TERMINATED_BY = "fields-terminated-by";
	public static final String COMPRESSION_CODEC = "compression-codec";
	public static final String HBASE_CREATE_TABLE = "hbase-create-table";
	public static final String HBASE_TABLE = "hbase_table";
	public static final String COLUMN_FAMILY = "column_family";
	public static final String HBASE_ROW_KEY = "hbase_row_key";
	public static final String HIVE_IMPORT = "hive_import";
	public static final String HIVE_TABLE = "hive_table";
	public static final String HIVE_PARTITION_KEY = "hive_partition_key";
	public static final String HIVE_PARTITION_VALUE = "hive_partition_value";
	
	public static int importDB(Map<String, String> parameters) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("--connect");
		list.add(parameters.get(CONNECT));
		list.add("--username");
		list.add(parameters.get(USERNAME)); // 数据库的用户名
		list.add("--password");
		list.add(parameters.get(PASSWORD)); // 数据库的密码
		list.add("--table");
		list.add(parameters.get(TABLE)); // 数据库中的表。
		if (null != parameters.get(APPEND)) {
			list.add("--append");
		}
		if (null != parameters.get(COLUMNS)) {
			/** 将导入指定字段*/
			list.add("--columns");
			list.add(parameters.get(COLUMNS));
		}
		if (null != parameters.get(QUERY)) {
			list.add("--query");
			list.add(parameters.get(QUERY));
		}
		if (null != parameters.get(WHERE)) {
			list.add("--where");
			list.add(parameters.get(WHERE));
		}
		if (null != parameters.get(TARGET_DIR)) {
			list.add("--target-dir");
			list.add(parameters.get(TARGET_DIR));
		}
		if (null != parameters.get(HIVE_IMPORT)) {
			list.add("--hive-import");
		}
		if (null != parameters.get(HIVE_TABLE)) {
			list.add("--hive-table");
			list.add(parameters.get(HIVE_TABLE));
		}
		if (null != parameters.get(HIVE_PARTITION_KEY)) {
			list.add("--hive-partition-key");
			list.add(parameters.get(HIVE_PARTITION_KEY));
		}
		if (null != parameters.get(HIVE_PARTITION_VALUE)) {
			list.add("--hive-partition-value");
			list.add(parameters.get(HIVE_PARTITION_VALUE));
		}
		if (null != parameters.get(HBASE_CREATE_TABLE)) {
			list.add("--hbase-create-table");
			list.add(parameters.get(HBASE_CREATE_TABLE));
		}
		if (null != parameters.get(HBASE_TABLE)) {
			list.add("--hbase-table");
			list.add(parameters.get(HBASE_TABLE));
		}
		if (null != parameters.get(COLUMN_FAMILY)) {
			list.add("--column-family");
			list.add(parameters.get(COLUMN_FAMILY));
		}
		if (null != parameters.get(HBASE_ROW_KEY)) {
			list.add("--hbase-row-key");
			list.add(parameters.get(HBASE_ROW_KEY));
		}
		if (null != parameters.get(SPLIT_BY)) {
			list.add("--split-by");
			list.add(parameters.get(SPLIT_BY));
		}
		if (null != parameters.get(FIELDS_TERMINATED_BY)) {
			list.add("--fields-terminated-by");
			list.add(parameters.get(FIELDS_TERMINATED_BY));
		}
		if (null != parameters.get(M)) {
			list.add("-m");
			list.add(parameters.get(M));
		}
		if (null != parameters.get(COMPRESSION_CODEC)) {
			/** lzo 表示用lzo方式压缩*/
			list.add("--compression-codec");
			list.add(parameters.get(COMPRESSION_CODEC));
		}
		if (null != parameters.get(Z)) {
			/** 采用压缩方式*/
			list.add("-z");
		}
		Sqoop sqoop = new Sqoop(new ImportTool());
		sqoop.setConf(configuration);
		return Sqoop.runSqoop(sqoop, list.toArray(new String[0]));
	}
	
	public static int exportDB(Map<String, String> parameters) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("--connect");
		list.add(parameters.get(CONNECT));
		list.add("--username");
		list.add(parameters.get(USERNAME)); // 数据库的用户名
		list.add("--password");
		list.add(parameters.get(PASSWORD)); // 数据库的密码
		list.add("--table");
		list.add(parameters.get(TABLE)); // 数据库中的表。
		list.add("--columns");
		list.add(parameters.get(COLUMNS)); // 将导入指定字段
		list.add("--hbase-table");
		list.add(parameters.get(HBASE_TABLE));// 导入hbase指定表
		list.add("--column-family");
		list.add(parameters.get(COLUMN_FAMILY));
		list.add("--hbase-row-key");
		list.add(parameters.get(HBASE_ROW_KEY));// 记录边界
		list.add("--split-by");
		list.add(parameters.get(SPLIT_BY));
		list.add("-m");
		list.add(parameters.get(M));// 定义mapreduce的数量。
		Sqoop sqoop = new Sqoop(new ExportTool());
		sqoop.setConf(configuration);
		return Sqoop.runSqoop(sqoop, list.toArray(new String[0]));
	}
	
//	public void export() {
//		Configuration conf  = new Configuration(); 
//		SqoopOptions sqoopOptions = new SqoopOptions();
//		sqoopOptions.setConf(conf);
//		sqoopOptions.setConnectString(parameters.get(URL));
//		sqoopOptions.setDriverClassName(parameters.get(DRIVER_CLASS_NAME));
//		sqoopOptions.setUsername(parameters.get(USERNAME));
//		sqoopOptions.setPassword(parameters.get(PASSWORD));
//		sqoopOptions.setFieldsTerminatedBy('\01');
//		sqoopOptions.setLinesTerminatedBy('\n');
//		sqoopOptions.setClearStagingTable(true);
//		//sqoopOptions.setHadoopHome(parameters.get(HADOOP_HOME));
//		sqoopOptions.setJobName("HDFS to Mysql");
//		sqoopOptions.setJobName("HDFS Map reduce Job");
//		sqoopOptions.setJarOutputDir("");
//		sqoopOptions.setNumMappers(3);
//		sqoopOptions.setCodeOutputDir("");
//		sqoopOptions.setTableName(parameters.get(TABLE));
//		sqoopOptions.setExportDir("");
//		sqoopOptions.setClassName("");
//		ExportTool exportTool = new ExportTool();
//		exportTool.run(sqoopOptions);
//	}
	
}
