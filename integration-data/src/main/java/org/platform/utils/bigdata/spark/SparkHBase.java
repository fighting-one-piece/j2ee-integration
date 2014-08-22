package org.platform.utils.bigdata.spark;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkHBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "resource", "deprecation" })
	public void operationHBase() throws IOException {
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("spark://centos.master:7077");
		sparkConf.setAppName("Spark HBase");
		sparkConf.setSparkHome("/home/hadoop/software/spark-1.0.2-bin-hadoop2");
		sparkConf
				.setJars(new String[] {
						"/home/hadoop/software/hbase-0.96.0/lib/hbase-common-0.96.0-hadoop2.jar",
						"/home/hadoop/software/hbase-0.96.0/lib/hbase-server-0.96.0-hadoop2.jar",
						"/home/hadoop/software/hbase-0.96.0/lib/hbase-client-0.96.0-hadoop2.jar",
						"/home/hadoop/software/hbase-0.96.0/lib/hbase-protocol-0.96.0-hadoop2.jar", });
		JavaSparkContext context = new JavaSparkContext(sparkConf);

		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "centos.master");
		conf.set("hbase.master", "centos.master:60000");
		conf.set("hbase.mapreduce.inputtable", "user");
		conf.addResource("/home/hadoop/software/hbase-0.96.0/conf/hbase-site.xml");

		// String tableName = "user";
		// conf.set(TableInputFormat.INPUT_TABLE, tableName);

		// Scan scan = new Scan();
		// scan.setStartRow(Bytes.toBytes("0120140722"));
		// scan.setStopRow(Bytes.toBytes("1620140728"));
		// scan.addFamily(Bytes.toBytes("basic"));
		// scan.addColumn(Bytes.toBytes("basic"), Bytes.toBytes("name"));

		// ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
		// String ScanToString = Base64.encodeBytes(proto.toByteArray());
		// conf.set(TableInputFormat.SCAN, ScanToString);

		HTable table = new HTable(conf, "user");
		Put put = new Put(Bytes.toBytes("row6"));
		put.add(Bytes.toBytes("basic"), Bytes.toBytes("name"),
				Bytes.toBytes("value6"));
		table.put(put);
		table.flushCommits();

		JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = context
				.newAPIHadoopRDD(conf, TableInputFormat.class,
						ImmutableBytesWritable.class, Result.class);

		Long count = hBaseRDD.count();
		System.out.println("count: " + count);

		List<Tuple2<ImmutableBytesWritable, Result>> tuples = hBaseRDD
				.take(count.intValue());
		for (int i = 0, len = count.intValue(); i < len; i++) {
			Result result = tuples.get(i)._2();
			KeyValue[] kvs = result.raw();
			for (KeyValue kv : kvs) {
				System.out.println("rowkey:" + new String(kv.getRowArray()) + " cf:"
						+ new String(kv.getFamilyArray()) + " column:"
						+ new String(kv.getQualifierArray()) + " value:"
						+ new String(kv.getValueArray()));
			}
			Cell[] cells = result.rawCells();
			for (Cell cell : cells) {
				System.out.println("rowkey:" + new String(cell.getRowArray()) + " cf:"
						+ new String(cell.getFamilyArray()) + " column:"
						+ new String(cell.getQualifierArray()) + " value:"
						+ new String(cell.getValueArray()));
			}
		}
	}

	public static void main(String[] args) {
		try {
			new SparkHBase().operationHBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
