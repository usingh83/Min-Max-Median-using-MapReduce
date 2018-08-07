package lab.ques4;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import org.apache.hadoop.util.*;


public class MinMaxMedian {
	public static class Map extends Mapper<LongWritable, Text, PairWritable, IntWritable>{
		int i=0;
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
				context.write( new PairWritable(1L,Long.parseLong(value.toString())), new IntWritable(Integer.parseInt(value.toString())));
		}
	}
	public static class Partition extends Partitioner<PairWritable, IntWritable>{
		@Override
		public int getPartition(PairWritable key, IntWritable value, int noReducers) {
			return 0;
		}
	}
	
	public static class Reduce extends Reducer<PairWritable,IntWritable,Text,Text> {
		int min =0;
		int max =0;
		double median=0;
		ArrayList<Integer> l=new ArrayList();
		public void reduce(PairWritable key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
			 // initialize the sum for each keyword
			for(IntWritable itemCount:values){
				l.add(itemCount.get());
				if(min>itemCount.get()) min=itemCount.get();
				if(max<itemCount.get()) max=itemCount.get();
			}
			int size=l.size();
			if(size%2==0) median=(l.get((size-1)/2)+(l.get(size/2)))/2.0;
			else median=l.get(size/2);
			String val="min = "+min+"  max = "+max+"  median = "+median;
			Text result=new Text();
			result.set(val);
			context.write(new Text("Result : "),result);// create a pair <keyword, number of occurences>
		}
	}
  	// Driver program
  	public static void main(String[] args) throws Exception {
  	Configuration conf = new Configuration();
  	String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
  	// get all args
  	if (otherArgs.length != 2) {
  	System.err.println("Usage: WordCount <in> <out>");
  	System.exit(2);
  	}
  	Job job = new Job(conf, "WordCount");
  	job.setJarByClass(MinMaxMedian.class);
  	job.setMapperClass(Map.class);
  	job.setPartitionerClass(Partition.class);
  	job.setReducerClass(Reduce.class);
  	// uncomment the following line to add the Combiner job.setCombinerClass(Reduce.class);
  	// set output key type
  	//job.setSortComparatorClass(KeySortComparator.class);
  	job.setGroupingComparatorClass(GroupingComparator.class);
  	job.setOutputKeyClass(PairWritable.class);
  	// set output value type
  	job.setOutputValueClass(IntWritable.class);
  	job.setNumReduceTasks(1);
  	//set the HDFS path of the input data
  	FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
  	// set the HDFS path for the output
  	FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
  	//Wait till job completion
  	System.exit(job.waitForCompletion(true) ? 0 : 1);
  	}
  }
