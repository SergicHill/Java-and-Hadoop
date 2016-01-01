/** Problem 3 of hw 7
	Author: Serguey Khovansky
    @version December, 2015
The Problem:
Write a new MR program DocWordIndex.java, which writes a report of indexed word usages
such that for each word it cites its location (by word #) in the file, for each file.

Here is sample output :
fleas /Users/charliesawyer/input/f1.txt: 2 9 /Users/charliesawyer/input/f2.txt: 4 

The meaning is that the word "fleas" appears in file f1.txt as the 2nd and 9th word in that file and
in f2.txt as the 4th word in that file.

To compile:
/Harvard_cscie55/cscie55/hw7$ javac DocWordIndex.java


To Run (note the change of folder):
/Harvard_cscie55$ hadoop   cscie55.hw7.DocWordIndex -fs file:/// -jt local  cscie55/hw7/input    cscie55/hw7/outputHW3_1
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

/**The class DocWordIndex uses Map/Reduce technique in order to record word counts (by words) in a file text*/
public class DocWordIndex  extends Configured implements Tool {

    /**This is main method
       @param args[]  input and output paths 
	*/
    public static void main(String args[]) throws Exception {
	int res = ToolRunner.run(new DocWordIndex(), args);
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
	job.setJobName("DocWordIndex");
	job.setJarByClass(DocWordIndex.class);
	
	/**These commands seems to be excessive*/
	//job.setInputFormatClass(TextInputFormat.class);
	//job.setOutputFormatClass(TextOutputFormat.class);
	
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
	private String folderFileName;
    
	/** This method reads the file path and name from the relevant context
          @param context automatic input to the setup function, comes from configuration
	*/	
    @Override
	protected void setup(Context context) throws IOException, InterruptedException
	{
	    /**Read the file name and file path*/
	    FileSplit fsFileSplit = (FileSplit) context.getInputSplit(); 
	    folderFileName = fsFileSplit.getPath().toString().substring(5) + ":";
	}
	
	/**This method does the map of Map/Reduce
		@param key   integer offset (can be ignored in this case)
        @param value  text of the file
        @param context  output to the MapReduce framework before being sent to the reduce function 
     */ 
    @Override
	public void map(LongWritable key, Text value,
			Mapper.Context context) throws IOException, InterruptedException
    {
	   String line = value.toString();
	   String[] line_split = line.split("\\s+");	   

	   /**Cound the location of a given word in a line string*/
	   int ind;	  
	   /** Temporal string to keep folderFileName*/
	   String tmp_folderName = folderFileName;

	   /**Set to avoid duplicates in word counting*/
	   Set<String> myset = new HashSet<String>();
	   
	      for (String stmp:  line_split)
	      {		   
			/**If the word was already counted we skip it*/
			if(myset.contains(stmp)) continue;
			/**Otherwise place the word into myset*/
			myset.add(stmp);
			/**Place the stmp into a MP Text object word*/
			word.set(stmp);

			ind = 0; /**Indicats location of a given word in a string line*/
                 
                   /**Count location of the word "stmp" in the text line_split in terms of words*/
			for (String str:  line_split)
			{
		       ind += 1;
		       /**Record location of str into the folderFileName if str==stmp*/
		       if( str.equals( stmp)) folderFileName += " " + ind;
			}
			context.write(word, new Text(folderFileName));
			/**For each new word stmp  arrange original folderFileName and proceed the loop*/
			folderFileName = tmp_folderName ;
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
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
	
	/**The variable svalue is used twice: first  in order to add elements into a set and second in order to 
	   construct a string to keep elements of the set*/
	String svalue;
	/**To avoid duplicate entries in values create a set and add values to this set*/
   	Set<String> myset = new HashSet<String>();

	for(Text value : values) 
	{
	    svalue = value.toString();
	    myset.add(svalue);
	}
	 
	 svalue = "\t";
	 /**Iterate over the elements of the set and place them into a string*/
	 Iterator iterator = myset.iterator();
	 while (iterator.hasNext())
	{
	     svalue += iterator.next() + " ";  
	}
	
	 context.write(key, new Text( svalue));
    }
    
   }
}
