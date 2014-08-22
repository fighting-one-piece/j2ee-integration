package org.platform.utils.bigdata.hive;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ModOutputFormat extends TextOutputFormat<LongWritable, Text> {

	@Override
	public RecordWriter<LongWritable, Text> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		conf.set("mapreduce.output.textoutputformat.separator", "::");
		return super.getRecordWriter(job);
	}
	
}
class NewRecordWriter extends RecordWriter<LongWritable, Text> {
	
	@Override
	public void close(TaskAttemptContext arg0) throws IOException,
	InterruptedException {
		
	}
	
	@Override
	public void write(LongWritable arg0, Text arg1) throws IOException,
	InterruptedException {
		
	}
	
}

