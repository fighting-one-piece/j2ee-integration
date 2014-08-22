package org.platform.utils.bigdata.mr;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class RemoveDuplicate {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.out.println("error");
			System.exit(2);
		}
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(RemoveDuplicate.class);
		
		job.setMapperClass(RemoveDuplicateMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(RemoveDuplicateReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.out.println(job.waitForCompletion(true) ? 0 : 1);
	}
}


class RemoveDuplicateMapper extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable key, Text value, Context context) 
			throws IOException, InterruptedException  {
		String line = value.toString();
		if (StringUtils.isBlank(line)) return;
		StringTokenizer token = new StringTokenizer(line, ":");
		String id = token.nextToken();
		context.write(new Text(id), value);
	}
}

class RemoveDuplicateReducer extends Reducer<Text, Text, NullWritable, Text> {
	
	protected void reduce(Text key, Iterable<Text> values, Context context) 
			throws IOException, InterruptedException {
		for (Text value : values) {
			context.write(NullWritable.get(), value);
			break;
		}
	};
}
