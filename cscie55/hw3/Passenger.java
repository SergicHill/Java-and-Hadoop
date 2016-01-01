/** Class Passenger  - a part of hw 3
 * @author Serguey Khovansky
 * @version October, 2015
 */
 
package  cscie55.hw3;

/** The class Passenger records, a passenger's current floor, destination floor and passenger's id */
public class Passenger{

	/** The Passenger's current floor.*/
	private int currentFloor;
	
	/** The Passenger's destination floor*/
	private int destinationFloor;
	
	/** Passenger's id*/
	private final int id;
	
	/**Use this constant to indicate situation when the destination floor is undefined*/
	private static final int UNDEFINED_FLOOR = -1;
	
	
	/**The passenger is originated (via constructor) on the first floor,
    	this situation resembles a case when a passenger enters the building through the first floor */
	public Passenger(int id)
	{
		/**Set up the passenger's id */
		this.id = id;	
		
		/**Set the current Floor at 1*/
		this.currentFloor = 1;
		
		/**Set the destination Floor at 1*/
		this.destinationFloor=1;
	}
	
	
	/** Returns the Passenger's current floor
	    @return current Floor*/
	public int currentFloor()
	{
		return currentFloor;
	}
	
	/** The Passenger's destination floor
	    @return destinationFloor*/
	public int destinationFloor()
	{
		return destinationFloor;
	}	
	
	/** Sets the Passenger's destination floor to newDestinationFloor.
	 	@param newDestinationFloor	*/
	public void waitForElevator(int newDestinationFloor)
	{
		destinationFloor = newDestinationFloor;
	}
	
	/**Sets the Passenger's current floor to be undefined */
	public void boardElevator()
	{
		currentFloor = UNDEFINED_FLOOR;
	}	
	
	 /** The Passenger is on an elevator and arrives at his or her destination.
	     Copy the value of the destination floor to the current floor,
	     and set the destination floor to be undefined.*/
	public void arrive()
	{
		currentFloor = destinationFloor;
		destinationFloor = UNDEFINED_FLOOR;
	}	
	
	/**Make underfined current floor. 
	   This method is applicable to the case when the passenger is riding on an elevator*/
     void currentFloorUndefined()
	{
		currentFloor = UNDEFINED_FLOOR;
	}
	
	/** Make underfined destination floor. 
	    This method is applicable to the case when the passenger is resident*/
	void destinationFloorUndefined()
	{
		destinationFloor = UNDEFINED_FLOOR;
	}
	
	/**Returns the constant that signifies the UNDEFINED FLOOR
	   @return UNDEFINED_FLOOR*/
	int getUndefinedFloor()
	{
		return UNDEFINED_FLOOR;
	}
	
	/**Returns id of the passenger
	   @return id */
	int get_id()
	{
		return id;
	}
	
	public String toString()
	{
		String s;
		s = "Passenger id = " + id + "Current Floor = " + currentFloor + "Destination Floor= " + destinationFloor;
		return s;
	}
	
}