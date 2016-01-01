/** Exception class for hw3
 *
 * @author Serguey Khovansky
 * @version October, 2015
 */


package  cscie55.hw3;
import java.lang.Exception;


/**
*The class ElevatorFullException which throws ElevatorFullException when the elevator is already at capacity.
*/
public class ElevatorFullException extends Exception
   {
       private String msg;
	   private int currentFloor;
	   
	   /** Constructor: The exception can provide a message */
       public ElevatorFullException(String s)
       {
          msg = s;
       }  
	   
	   /** Constructor: The exception can provide no any messages */
       public ElevatorFullException()
       {
          msg = null;
       }
	   
	   /** Constructor: The exception can keep information about the current floor in order to report, if neccessary */
	   public  ElevatorFullException(int currentFloor)
	   {
	    this.currentFloor = currentFloor; 
	   }
	  

	   public String toString()
	   {
	   return "ElevatorFullException ( " + msg +" )";
	   
	   }
	   
   }