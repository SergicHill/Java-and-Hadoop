/** Supplement to  hw 8
	@Author: Serguey Khovansky
    @version December, 2015

This class allows to filter out files of specific form. In this case, we skip
the files that end with ~.

To compile:
javac  cscie55/hw8/RegexFilter.java

*/	
package cscie55.hw8;


import java.io.IOException;
import java.util.*;
/**This package is not needed in this specific case, but can be needed in general applications*/
//import java.text.SimpleDateFormat;


import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

/** This class filters filename and filepaths*/
public class RegexFilter implements PathFilter
{
   private final String regex;
    /** Constructor, sets a simple pattern for filtering*/
   public RegexFilter() 
          {
	      this.regex = "~";
          }

    /** A file is accepted to further analysis if its name does not end with ~ or it is not null*/
    public boolean accept(Path path)
          {
	      return !(path.toString().endsWith(regex) || path.toString() == null);
  }
}
