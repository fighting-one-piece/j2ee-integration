package org.platform.utils.bigdata.wordcount;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReduceClass extends Reducer<Text, IntWritable, Text, IntWritable> {

	protected void reduce(Text text, Iterable<IntWritable> iterator, Context context) {
		System.out.println("text: " + text.toString());
		int sum = 0;
		Iterator<IntWritable> iter = iterator.iterator();
		while (iter.hasNext()) {
			int i = iter.next().get();
			sum += i;
		}
		try {
			context.write(text, new IntWritable(sum));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};
}
