/** Floor class is a part of hw2
 *
 * @author Serguey Khovansky
 * @version October, 2015
 */
package  cscie55.hw3;
import java.util.TreeSet;
import java.util.Set;


/** The class Floor describes one of the floors that the elevator can visit and where the passsengers are located */
public class Floor{

	/**the number of this floor*/
	private final int floorNumber;
 
	
	private Set<Passenger>  hs_resident;
	private Set<Passenger>  hs_up;
	private Set<Passenger>  hs_down;


	/** Constructor 
	  @param floorNumber is the number of the current floor */	
	public Floor(int floorNumber)
	{
		this.floorNumber = floorNumber;
		/**Keeps track of the passengers who are residents on the Floor*/
		hs_resident = new TreeSet<Passenger>(new PassengerCompare());
		/**Keeps track of the passengers who still on the Floor but wish to move up*/
		hs_up = new TreeSet<Passenger>(new PassengerCompare());
		/**Keeps track of the passengers who still on the Floor but wish to move down*/
		hs_down = new TreeSet<Passenger>(new PassengerCompare());
	}
	

  
 	/** 
	  This function is called when a passenger on the floor  waits for the elevator. 
	  Calling this should cause the elevator to stop the next time it moves to the floor.
	  This method allows the Floor to know which Passenger is waiting for the Elevator.
	  by comparing destinationFloor to the floor number, the Floor class knows whether the Passenger is waiting to go up or down.*/
	public void waitForElevator(Passenger passenger, int destinationFloor){
		/**Set the destination floor for passenger*/
		passenger.waitForElevator(destinationFloor);
		
		/**Adjust the TreeSets accordingly
		   Consider a situation when the passenger  was resident initially, but then changed to down or up*/
		if(hs_resident.contains(passenger))
		{	
			/**Actually moves down*/
			if(destinationFloor < floorNumber)
			{			
				hs_resident.remove(passenger);
				hs_down.add(passenger);
			/**Actually moves up*/
			} else if(destinationFloor > floorNumber)
			{
			
				hs_resident.remove(passenger);
				hs_up.add(passenger);
			}
		return;	
		}	
				
		/**Consider a situation when the passenger initially was up, but then changed to down or become resident*/
		if(hs_up.contains(passenger))
		{
			/**Actually moves down*/
			if(destinationFloor < floorNumber)
			{
				hs_up.remove(passenger);
				hs_down.add(passenger);
				
			/**Actually becomes resident*/	
			} else if(destinationFloor == floorNumber)
			{
				hs_up.remove(passenger);	
				hs_resident.add(passenger);
			}			   
		return;
		}
			
		/**Consider a situation when the passenger initially was down, but then changed to up or become resident*/
		if(hs_down.contains(passenger))
		{
			/**Actually moves up*/
			if(destinationFloor > floorNumber)
			{
				hs_down.remove(passenger);
				hs_up.add(passenger);
				
			/**Actually becomes resident*/	
			}else if(destinationFloor == floorNumber)
			{
				hs_down.remove(passenger);
				hs_resident.add(passenger);		
			}
		return;		
		}
	}
 
	/**This method returns true if the passenger is resident on the Floor, 
	  (i.e., not waiting to go up and not waiting to go down), false otherwise.
	  @param passenger 
	  @return boolean */
	public boolean isResident(Passenger passenger)
	{
		if (passenger.destinationFloor() == passenger.getUndefinedFloor()){return true;
		}else{return false;}
	}
	 
	/**This method adds a passenger to the Floor's residents
	   so its name enterGroundFloor is slightly misleading
	   @param passenger */
	void enterGroundFloor(Passenger passenger)
	{
		if(isResident(passenger)){hs_resident.add(passenger);} 
	}
	
	
	/** Gets Set of floor residents
	    @return hs_resident */
	Set<Passenger> getHSresident()
	{
		return hs_resident;
	}
	
	/** Gets Set of floor passengers with up destination
	    @return hs_up */
	Set<Passenger> getHSup()
	{
		return hs_up;
	}
	
	/** Gets Set of floor passengers with down destination
	    @return hs_down */	
	Set<Passenger> getHSdown()
	{
		return hs_down;
	}
	
	/**This method makes the passenger a resident 
	   @param passenger*/
	void becomeResident(Passenger passenger)
	{
		/**Resident passenger has undefined destination floor*/
		passenger.destinationFloorUndefined();
		/**Place this passenger into the  resident treeSet */
		hs_resident.add(passenger);	
	}
	
	
		public String toString()
	{
		String s;
		s = " hs_resident= " + hs_resident + " hs_up  " + hs_up + " hs_down  " + hs_down;
		return s;
	}
	
	
	
}