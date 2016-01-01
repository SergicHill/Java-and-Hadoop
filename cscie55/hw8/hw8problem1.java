/** 
    Problem 1 of hw 8
	@Author: Serguey Khovansky
    @version December, 2015
	
The Problem:
Write a Map/Reduce solution that lists the url's with the (comma-separated) union of the tags listed in all the records for each url. 
For example, suppose the following two records were seen in the stream:
    {"url": "http://freegasmoney.org/", "timestamp": 1257034785, "tags": ["free", "gas"]}
    {"url": "http://freegasmoney.org/", "timestamp": 1257034877, "tags": ["free", "gas", "vacation"]} 
The final output from your solution would have one line:
     http://freegasmoney.org/ free,gas,vacation
*/

package cscie55.hw8;


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

/**The class hw8problem1 uses Map/Reduce technique in order to solve problem 1 of hw8*/
public class hw8problem1  extends Configured implements Tool {

    /**This is main method
		@param args[]  input and output paths 
	 */
    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new hw8problem1(), args);
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
	/**Make a job object with given configuration*/
	Job job = new Job(conf, this.getClass().toString());

    /**Filter out certain files, see RegexFilter*/ 
    FileInputFormat.setInputPathFilter(job, RegexFilter.class);
      
	/**Input and output paths*/ 
	FileInputFormat.setInputPaths(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);

	job.setJobName("hw8problem1");
	job.setJarByClass(hw8problem1.class);
	
	
	/**Map output key and value types*/
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(Text.class);
	
	
	/**Reducer output key and value types*/
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(Text.class);

	/**Specifies the map and reduce types*/	
	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);
	
	return job.waitForCompletion(true) ? 0 : 1;
    }




	/**This class does the Map part of Map/Reduce methodology*/
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

	private Text word = new Text();
		
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
		ArrayList<String> ArrStr = new  ArrayList<String>();
		String stag;
		
           try{
				Link link_line = Link.parse(line);
				String url =  link_line.url();
				word.set(url);
	    
				/**Keep the tags in this arraylist*/
				ArrStr.addAll( link_line.tags());
     
				/**Record the tags using iterator*/
				Iterator<String> it = ArrStr.iterator();
	     
				while (it.hasNext())
				{
					stag = it.next();
					context.write(word, new Text(stag));
				}
           }catch(StringIndexOutOfBoundsException es){
                System.out.println("Exception: Empty line in the file that Link reads, formal explanation here: " +es); }
			catch( Exception e){ System.out.println(e);}
        }
    }
	
	/**This class does Reduce part*/
    public static class Reduce extends Reducer<Text, Text, Text, Text> {

	/**This class does reduce part of Map/Reduce methodology
	     @param word  word of the textfile
         @param values  folder of the textfile
         @param context   output to the MapReduce framework before being sent to the reduce function 
	*/
    @Override
	public void reduce(Text word, Iterable<Text> values, Context context) throws IOException, InterruptedException{
	
        Set<String> myset = new HashSet<String>();		
		String str = "";

		/**
		Loop over elements of values to keep unique elements we first place them into a set
		*/
        
		for(Text value : values) 
		{
			str = value.toString();
			myset.add(str);
		}
	 
		str = "";
		/**Iterate over the elements of the set and place them into a string*/
		Iterator iterator = myset.iterator();
		boolean btmp = false;
		if(iterator.hasNext()) btmp = true;
		/** Use this complicated loop to place comma after all but the last tag*/
		while (btmp)
		{
			str += iterator.next(); 
			if(iterator.hasNext()) 
			{
				btmp = true;
				str += ",";  
			}else{
			btmp=false;
			} 

		}
	
		context.write(word, new Text(str));
    }
 }

}
