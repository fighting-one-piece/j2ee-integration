package org.platform.utils.bigdata.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<LongWritable, Text, Text, IntWritable> {

	protected void map(LongWritable key, Text value, Context context)  {
		System.out.println("value: " + value.toString());
		StringTokenizer token = new StringTokenizer(value.toString());
		while (token.hasMoreTokens()) {
			try {
				context.write(new Text(token.nextToken()), new IntWritable(1));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
