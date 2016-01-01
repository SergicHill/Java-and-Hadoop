/** 
    Problem 2 part2 of hw 8
	@Author: Serguey Khovansky
        @version December, 2015
	
The Problem:
Write a Map/Reduce solution that lists the url's of every record between two dates. 
The date format to support is dd-MM-yyyy. The data reflect records between Novermber 1, 2009, and November 15, 2009. 
The final output should list each URL and the count of its appearances.  Put the values for required dates in the driver's main.
To parse the lines of data use the class Link, which has a static method parse() that takes a String. In Mapper.map you can run Link.parse on each incoming line. This will produce a Link object. Note that Link.parser returns null when it encounters bogus data. Two gotchas: (1)blank lines in a data file, (2) editor backup files, like emacs' ~ recovery files. Either of these will cause problems.
Run your solution, giving start date as November 8, 2009 and end date as November 9, 2009. 

To compile:
javac  cscie55/hw8/hw8problem2b.java 
javac  cscie55/hw8/RegexFilter.java

To run:
hadoop   cscie55.hw8.hw8problem2b  -fs file:/// -jt local  cscie55/hw8/input    cscie55/hw8/output

*/

package cscie55.hw8;


import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

/**The class hw8problem2b uses Map/Reduce technique in order to solve problem 2 (part 2) of hw8*/
public class hw8problem2b  extends Configured implements Tool {
     

    /**This is main method
        @param args[]  input and output paths 
	 */
    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new hw8problem2b(), args);
	System.exit(res);
    }

	 /** This method sets up configuation
	    @param args[] Input and Output paths
	    @return boolean  1 if the job is completed successfully and 0 otherwise
	*/
    public int run(String[] args) throws Exception {
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);

	Configuration conf = getConf();
	/**Set up values for specific variables*/
	conf.set("startDay","08-11-2009");
    conf.set("endDay", "09-11-2009");

	/**Make a job object with given configuration*/
	Job job = new Job(conf, this.getClass().toString());

	/**Input path and filtering certain files, see RegexFilter*/ 
    FileInputFormat.setInputPathFilter(job, RegexFilter.class);
    FileInputFormat.addInputPath(job, inputPath);

	/**Output path*/ 
	FileOutputFormat.setOutputPath(job, outputPath);

	job.setJobName("hw8problem2b");
	job.setJarByClass(hw8problem2b.class);
	
	
	/**Map output key and value types*/
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(IntWritable.class);
	
	
	/**Reducer output key and value types*/
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);

	/**Specifies the map and reduce types*/	
	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);
	
	return job.waitForCompletion(true) ? 0 : 1;
    }



	/**This class does the Map part of Map/Reduce methodology*/
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private final static IntWritable one = new IntWritable(1);
	
    private Text   word = new Text();
	private long   startmilliseconds;
	private long   endmilliseconds;
       
	/** This method reads  the  context and gets  startDay and endDay and computes startmilliseconds and endmilliseconds
        @param context input to the setup function comes automatically from configuration
	*/       
	@Override
	    protected void setup(Context context) throws IOException, InterruptedException
	{
	     /**Read the filename*/
           
	     Configuration conf = context.getConfiguration();
	     String startDay = conf.get("startDay");
	     String endDay = conf.get("endDay");

	     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	     simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
            
        try{
	         Date startDate = simpleDateFormat.parse(startDay);
	         startmilliseconds = startDate.getTime()/1000;

	         Date endDate = simpleDateFormat.parse(endDay);
	         endmilliseconds = endDate.getTime()/1000;
	    }catch( Exception e){ System.out.println(e);}

	   }// end setup
	
	/**This method does the map of Map/Reduce
		@param key   integer offset (can be ignored for this task)
		@param value  text of the file
        @param context  output to the MapReduce framework before being sent to the reduce function 
	*/  
    @Override
	public void map(LongWritable key, Text value,
			Mapper.Context context) throws IOException, InterruptedException
       {
	   	  
	   String line = value.toString();
           /**The line might be empty.  To address the possible exception use try-catch*/
           try{
				/**Parse the line using class Link and its method parse()*/
				Link link_line = Link.parse(line);
				/**Get timestamp*/
				long time_url = link_line.timestamp();
				/**If timestamp satisfies the condition below then place the word into context and count as one*/   
				if( (time_url >= startmilliseconds) && (time_url <= endmilliseconds) )
				{
					/**Record url and place it into MapReduce word()*/
					String url =  link_line.url();
					word.set(url);
					context.write(word, one);
				}

	    }catch(StringIndexOutOfBoundsException es){
                        System.out.println("Exception: Empty line in the file that Link reads, formal explanation here: " +es); }
             catch( Exception e){ System.out.println(e);}

       }// method map
    }
	
	/**This class does Reduce part*/
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

	/**This class does reduce part of Map/Reduce methodology
         @param word  word of the textfile
         @param value_folder  folder of the textfile
         @param context   output to the MapReduce framework before being sent to the reduce function 
	*/
    @Override
	public void reduce(Text word, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
	
                
		/**
			Loop over elements of values and obtains the number of their occurrences
		*/
        int sum = 0;
		for (IntWritable value : values) {
			sum += value.get();
		}
		context.write(word, new IntWritable(sum));
    }
	
  }

}
