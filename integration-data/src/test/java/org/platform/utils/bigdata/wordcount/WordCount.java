package org.platform.utils.bigdata.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCount {

	public static void main(String[] args) throws Exception {
//		System.setProperty("hadoop.home.dir", "D:/develop/data/hadoop/hadoop-2.2.0");
//		System.setProperty("HADOOP_MAPRED_HOME", "D:/develop/data/hadoop/hadoop-2.2.0");
		
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.out.println("error");
			System.exit(2);
		}
//		conf.set("textinputformat.record.delimiter", "::");
//		conf.set("mapreduce.output.textoutputformat.separator", "::");
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(WordCount.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setMapperClass(MapperClass.class);
		job.setCombinerClass(ReduceClass.class);
		job.setReducerClass(ReduceClass.class);
//		
//		FileInputFormat.addInputPath(job, new Path(HadoopUtils.HDFS_USER_WAREHOUSE + "/first"));
//		FileOutputFormat.setOutputPath(job, new Path(HadoopUtils.HDFS_USER_WAREHOUSE + "/first/out"));
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.out.println(job.waitForCompletion(true) ? 0 : 1);
	}
}
