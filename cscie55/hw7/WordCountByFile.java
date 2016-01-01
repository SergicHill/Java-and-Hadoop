/** 
    Problem 2 of hw 7
	Author: Serguey Khovansky
    @version December, 2015
	
The Problem:
  Design the new Mapper and Reducer classes to report word counts by file name.
  Suppose the word "foo" appears twice in the file /Users/java/input/f1.txt and once in the file /Users/java/input/f2.txt. Then the output (output/part-r-00000) should contain this line:
  foo file:/Users/java/input/f1.txt: 2 file:/Users/java/input/f2.txt: 1
  Arrange to run it on an input file containing the three "fleas" files.
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

/**The class WordCountByFile uses Map/Reduce technique in order to record word counts by file name*/
public class WordCountByFile  extends Configured implements Tool {

    /**This is main method
       @param args[]  input and output paths 
	*/
    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new WordCountByFile(), args);
	System.exit(res);
    }

	 /** This method sets up configuation and required jobs
	    @param args[] Input and Output paths
        @return boolean  1 if the job is completed successfully and 0 otherwise
	*/
    public int run(String[] args) throws Exception {
	Path inputPath = new Path(args[0]);
	Path outputPath = new Path(args[1]);

	Configuration conf = getConf();
	/**Make a job object with given configuration*/
	Job job = new Job(conf, this.getClass().toString());

	/**Input and output paths*/ 
	FileInputFormat.setInputPaths(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);

	/**Name of the file with the code that solves the problem*/
	job.setJobName("WordCountByFile");
	job.setJarByClass(WordCountByFile.class);
	

	/**Map output key and value types*/
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(Text.class);
	
	
	/**Reducer output key and value types*/
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(Text.class);

	/**Specifies the map and reduce types*/	
	job.setMapperClass(Map.class);
	job.setReducerClass(Reduce.class);
	
	/**To get the required format of the output this Combiner is not needed*/
	//job.setCombinerClass(Reduce.class); 

	return job.waitForCompletion(true) ? 0 : 1;
    }
	/**This class does the Map part of Map/Reduce methodology*/
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
	
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	private String folderFileName;
    
	/** This method reads the file path and name from the relevant context
          @param context automatic input to the setup function, comes from configuration
	*/
    @Override
	protected void setup(Context context) throws IOException, InterruptedException
	{
	    /**Read the file name and file path*/
	    FileSplit fsFileSplit = (FileSplit) context.getInputSplit(); 
	    folderFileName = fsFileSplit.getPath().toString();
	 }
	
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
	   StringTokenizer tokenizer = new StringTokenizer(line);
           /**Go over the words of line, one by one, and place each of them into the MP word object*/
	   while (tokenizer.hasMoreTokens())
	   {
		   word.set(tokenizer.nextToken());
		   context.write(word, new Text(folderFileName));
	   }
        }
    }
	
	/**This class does Reduce part*/
    public static class Reduce extends Reducer<Text, Text, Text, Text> {

	/**This class does reduce part of Map/Reduce methodology
         @param word  word of the textfile
         @param value_folder  folder of the textfile
         @param context   output to the MapReduce framework before being sent to the reduce function 
	*/
    @Override
	public void reduce(Text word, Iterable<Text> value_folder, Context context) throws IOException, InterruptedException{
	/**Use Hashmap to setup correspondence between file folder and the number of times the folder appears in the output of map
	   The key of HashMap is the folder, the value is the number of times it appears.
	*/
	 HashMap<String,Integer> hm = new HashMap<String,Integer>(); 
	
	 String sfolder="";
	
	 /**The output of map methos is similar to  word:[folder1, folder2, folder1], 
	 i.e. word appear in folder1, foder2 and once more in folder1, the following loop goes over the [...] 
	 folder part and counts the number of times each folder pops up */
	for (Text folder : value_folder) 
	{
	    /**Convert a foldername, which is Text,  to a string*/
	    sfolder = folder.toString();

	    if( hm.containsKey(sfolder) )
	    {
		/**If the sfolder is already in the keys of maphash then increase the corresponding value of hm by 1*/
		hm.put(sfolder, hm.get(sfolder) + 1);
	    }else{
		/**If the hm does not contains sfolder, then insert it with corresponding value 1*/
	        hm.put(sfolder,1);
	    }
	}
		
	String str = "\t";
	/**Loop over the keys, i.e. folders, of hm and place them into a string, str, along with the corresponding values, which are the number of times these folders appear in the original Map's class method map output
	 */
        for (String hmkey : hm.keySet())
		{
			str += hmkey + ": " +hm.get(hmkey) + " ";
		}
	 context.write(word, new Text(str));
    }
    }

}
