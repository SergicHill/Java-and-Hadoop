/** 
    Problem 1 of hw 7
	Author: Serguey Khovansky
    @version December, 2015
	
The Problem:
Create a  program WordHistogram.java so that the final output of Map/Reduce is a histogram of <word-length, frequency>. 
For example output/part-r-00000 might contain these lines 
5 1254 
4 6934
meaning there were 1,254 words of length 5 and 6,934 words of length 4.
*/


package cscie55.hw7;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;


/**The class WordHistogram uses Map/Reduce technique in order to build a histogram "word length" vs " frequency"*/
public class WordHistogram extends Configured implements Tool {

    /**This is main method
       @param args[]  input and output paths 
	*/
    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new WordHistogram(), args);
	System.exit(res);
    }

	/** This method sets up configuation and required jobs
	    @param args[] Input and Output paths
        @return boolean  1 if the job is completed successfully and 0 otherwise
	*/
    public int run(String[] args) throws Exception {
	/**Gets input and output path as input parameters*/	
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);

	/**Gets configuation of hadoop*/	
	Configuration conf = getConf();
	/**Make a job object with given configuration*/	
	Job job = new Job(conf, this.getClass().toString());

	/**Input and output paths*/ 
	FileInputFormat.setInputPaths(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);

	/**Name of the file with the code that solves the problem*/	
	job.setJobName("WordHistogram");
	job.setJarByClass(WordHistogram.class);
	
	/**These commands seems to be excessive*/	
	//job.setInputFormatClass(TextInputFormat.class);
	//job.setOutputFormatClass(TextOutputFormat.class);
	
	/**Map output key and value types*/	
	job.setMapOutputKeyClass(IntWritable.class);
	job.setMapOutputValueClass(IntWritable.class);
	
	/**Reducer output key and value types*/		
	job.setOutputKeyClass(IntWritable.class);
	job.setOutputValueClass(IntWritable.class);

	/**Specifies the map and reduce types*/		
	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);	
	
	/**For this problem we do not need this command*/
	//job.setCombinerClass(Reduce.class);

	return job.waitForCompletion(true) ? 0 : 1;
    }

	/**This class does the Map part of Map/Reduce methodology*/
    public static class Map extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	/**This method does the map of Map/Reduce
		@param key   integer offset (can be ignored in this case)
        @param value  text of the file
        @param context  output to the MapReduce framework before being sent to the reduce function 
     */	
    @Override
	public void map(LongWritable key, Text value,
			Mapper.Context context) throws IOException, InterruptedException {
			
	String line = value.toString();
	/** Convert word to String*/	
	StringTokenizer tokenizer = new StringTokenizer(line);

	String wordStr;
	while (tokenizer.hasMoreTokens()) {
	    word.set(tokenizer.nextToken());
            /** Convert to String in order to use String's methods length*/
	    wordStr = word.toString();
            
	    context.write(new IntWritable(wordStr.length()), one);
	}
    }
    }
	
	/**This class does Reduce part*/
    public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

	/**This class does reduce part of Map/Reduce methodology
        @param word  word of the textfile
        @param value_folder  folder of the textfile
        @param context   output to the MapReduce framework before being sent to the reduce function 
	*/	
    @Override
	public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
	int sum = 0;
	/**Summation to compute the number of occurrences in the histogram*/
	for (IntWritable value : values) {
	    sum += value.get();
	}

	context.write(key, new IntWritable(sum));
    }
    }

}
