/** Building class is a part of hw3
 *
 * @author Serguey Khovansky
 * @version October, 2015
 */
package cscie55.hw3;
import  java.util.HashMap;
import  java.util.Map;


/**
*  The class Building keeps track of one Elevator and multiple floors
*/	
public class Building{
	
	/** Storing the number of floors in the building*/ 
	public static final int FLOORS = 7;

    /** References for  Elevator and Floor classes*/
	private Elevator   elevator;

	/** To keep track of Floors in the building employ Map, which will become HashMap*/
	 private	Map<Integer, Floor>  hmIntFloor; 
	
	/** The Building constructor creates an Elevator and one floor for each floor number. */ 
	public  Building()
	{
        /** Create elevator object*/
		elevator  = new Elevator( this);
		/** Create HashMap object that maps number of floors into a Floor object*/
		hmIntFloor =  new HashMap<Integer, Floor>();
  
	    /** The Building constructor creates one Floor object for each floor number  */ 
		for(Integer i = elevator.getFirstFloor(); i <= Building.FLOORS; ++i)
			{
				hmIntFloor.put( i, new Floor(i));
			}
	} 
 
    /**Get an elevator object
	   @return elevator object*/
	public Elevator elevator()
	{
		return elevator;
	}
		
	/** Get the Floor object for the given floor number
	   @param floorNumber
       @return Floor object  */	
	public Floor floor(int floorNumber)
	{	
		return hmIntFloor.get(floorNumber);
	}
	
	
	/**This method calls Floor.enterGroundFloor(passenger) 
	  for the Floor representing the ground floor  
	  @param passenger  */
	public void enter(Passenger passenger)
	{
		int firstFloor = elevator.getFirstFloor(); 
		/**The destination Floor of a passenger is not defined when it enters*/
		passenger.destinationFloorUndefined();
		/**Gets Floor object corresponding to first Floor, using HashMap hmIntFloor and
           calls the Floor class method enterGroundFloor()	*/
		hmIntFloor.get(firstFloor).enterGroundFloor(passenger);
	}
	
	/**Get HashMap hmIntFloor
	   @return hmIntFloor object*/
	Map<Integer, Floor>  get_hmFloor()
	{
		return hmIntFloor;
	}
	
}