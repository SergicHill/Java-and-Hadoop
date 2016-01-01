/** Elevator class is a part of hw3
 *
 * @author Serguey Khovansky
 * @version October, 2015
 */
 
package  cscie55.hw3;
import   java.util.Set;
import   java.util.TreeSet;
import   java.util.HashMap;
import   java.util.Iterator;
import   java.util.Map;


/**
*The class  Elevator describes an elevator. The elevator moves and down, stops at floors to disembark passengers and to pick up new passengers. 
*The elevator has limited capacity, so it picks up those passengers that could not be accommodated at its next visit to the floor. 
*/
public class Elevator{

	/** Direction of the move of the elvevator */
	private DIRTRAVEL direction;

	/**Reference to a builing where the elevator is located*/
	private Building building;

	/**current floor*/	
	private int currentFloor; 		

	
	/** Keeps track of passengers on Elevator - a Map, which will become HashMap, that maps Floor number with 
	    the set of passengers destined on that floor. Integer stands for floor number, Set<Passenger>, 
		which will become TreeSet stands for set of passengers destined for the floor */
	private	Map<Integer, Set<Passenger>>  hmFloorPassenger; 
	
	/**Iterator  over the TreeSet<Passenger>, which is value of HashMap hmFloorPassenger*/
	private Iterator<Passenger> it;
	
	/** The capacity of the elevator*/
	public static final int CAPACITY = 10;
	
	/** The first floor from which the elevator starts operating
		Assume, that the elevator moves up to the FLOORS    */
	private int firstFloor;

		
	/**
	* Constructor
	  @param building where the elevator is located */
	public Elevator(Building building)
	{
       this.building = building;
       firstFloor = 1;
	   
	   currentFloor = firstFloor;
	   direction = DIRTRAVEL.UP;
	   
	   /**Keeps track of passengers on elevator*/
	   hmFloorPassenger = new HashMap<Integer, Set<Passenger>>();
	}
	
	/** Return true if the elevator is going up, false otherwise
	 	@return boolean	*/
	public boolean goingUp()
	{
		if (direction==DIRTRAVEL.UP) {return true;
		}else{ return false;}
	}
	
	/** Return true if the elevator is going down, false otherwise
		@return boolean */
	public boolean goingDown()
	{
		if (direction==DIRTRAVEL.DOWN) {return true;
		}else{ return false;}	
	}
	
	
	 /** Move the currentFloor in the direction given by DIRTRAVEL, automatically modifies the direction of travel, 
	     if the ground floor or top floor have been reached, load and unload passengers */	
    public void move()
	{	
		/** Modifies the direction of travel, if the ground floor or top floor has been reached. */	
		if(currentFloor <= firstFloor) 
		{
			direction = DIRTRAVEL.UP;	
		} else if (currentFloor >= Building.FLOORS)
		{	
			direction = DIRTRAVEL.DOWN;
		}
	
		/** Changes the currentFloor by one floor*/
		if(direction == DIRTRAVEL.UP) {
			currentFloor++;
		} else{ 
			currentFloor--;
		}

		if(currentFloor==firstFloor) direction = DIRTRAVEL.UP;
		if(currentFloor==Building.FLOORS) direction = DIRTRAVEL.DOWN;
		
 
	/**
	First, discharge passengers from elevator who are destined to the current floor
	*/
	 
		/**The hs_load keeps track of passengers on elevator aiming to current floor*/
		Set<Passenger> hs_load  =   hmFloorPassenger.get(currentFloor); 
		
		/** The set hs_load is not empty if some of passengers previosly boarded to get to this floor*/
		if( hs_load != null )
		{ 
			for (Passenger it_pass : hs_load) 
			{
				/**Passenger arrives to the destination floor*/
				it_pass.arrive();	 			
				/**discharge passengers to the current floor*/		
				building.get_hmFloor().get(currentFloor).becomeResident(it_pass);
			}
		
			/**Clear passengers from the elevator who aims to the current floor  */
			hmFloorPassenger.remove(currentFloor);		
		}
		/**If hs_load == null, then do nothing, that means no passengers want to load from the elevator to the current Floor*/
	

	/**
	Second. board passengers from current floor to the elevator and spread them across up or down TreeSets
	consider two cases: when the elevator goes up and when it goes down
	*/

		/** Computes total number of passengers on the elevator*/
		int nPassengersInElevator = passengers().size();
		/**if capacity allows then try to board passengers, otherwise do nothing and move forward if any*/
		if(nPassengersInElevator < CAPACITY)
		{
	  
			if(direction == DIRTRAVEL.UP){
				/**Get the TreeSet of the floor passengers who aims to higher floors*/
				Set<Passenger> hs_board = new TreeSet<Passenger>( new PassengerCompare()) ; 
									
				/**Can load only limited number of passengers <= CAPACITY,  use iterator for this*/
				it = building.get_hmFloor().get(currentFloor).getHSup().iterator();
		
				while(it.hasNext() && nPassengersInElevator < CAPACITY)
				{
					nPassengersInElevator++;
					hs_board.add( it.next() );		
				}
		
		
				if(hs_board !=null)
				{
					/**Board these passengers on the elevator*/
					try{
						boardPassenger( hs_board);
						}catch(ElevatorFullException e ){System.out.println("ERROR: Elevator at up: Line 193");}
		
					/**Clear elements from  the hs_up that moved to the elevator */
					it = hs_board.iterator();
					while(it.hasNext())
					{
						building.get_hmFloor().get(currentFloor).getHSup().remove( it.next() );		
					}
		
				}
			/**Now consider the passengers who aim to the lower Floors*/			
			} else
				{
					/**Get the TreeSet of the floor passengers who aims to lower floors*/
					Set<Passenger> hs_board = building.get_hmFloor().get(currentFloor).getHSdown();
		
					it = building.get_hmFloor().get(currentFloor).getHSdown().iterator();
					while(it.hasNext() && nPassengersInElevator <= CAPACITY)
					{
						nPassengersInElevator++;
						hs_board.add( it.next() );		
					}
		
		
					if(hs_board != null)
					{
						try{
							/**Board these passengers on the elevator*/
							boardPassenger( hs_board);
							}catch(ElevatorFullException e){
										System.out.println("ERROR: Elevator in down-move");}	 
						/**Clear the hs_up that was get by getHSdown()*/	
						it=hs_board.iterator();
						while(it.hasNext())
						{
							building.get_hmFloor().get(currentFloor).getHSdown().remove( it.next() );		
						}
				
					}
			}
		}
	
	}

	
	
	/** Returns the current floor
	    @return currentFloor*/	
	public int currentFloor()
	{
        return currentFloor ;
	}	
	
	
	/**  Returns the set of all passengers on the elevator
	     @return  Set<Passenger> */	
	public Set<Passenger> passengers(){	
			/**Set of passengers who are on the elevator, to be filled in this method*/
			Set<Passenger>  setPassengerOut = new TreeSet<Passenger>( new PassengerCompare());
			
			/**Iterate over all destination Floors*/	
			for(Map.Entry<Integer, Set<Passenger>>  entry : hmFloorPassenger.entrySet() )
				{
					/**Iterator over Passengers who are destined for a particular destination Floor */
					Iterator<Passenger> iteratorPass =entry.getValue().iterator();
					
					/**Iterate over Passengers who are destined to a particular Floor*/
					while(iteratorPass.hasNext())
					{
						/**Each passenger is added to TreeSet, if it is not there yet*/
						setPassengerOut.add( iteratorPass.next() );
					}	
				}
		return setPassengerOut;
	}	
	

	/** Gets the first floor (aka ground floor)
	    @return firstFloor*/	
	int getFirstFloor(){
		return firstFloor;
	}
		
	
	/** Boards  set of passenger from the given TreeSet with destination to the given destination floor
	    @param Set<Passenger> hs  which is set of passengers*/
	private void boardPassenger( Set<Passenger> hs ) throws ElevatorFullException
	{ 	
		
		Integer destinationFloor;	
		it = hs.iterator();
		Passenger passengerTmp;
		Set<Passenger>  hs_tmp;
		
		/**Spread the passengers across their destination Floors*/
		while(it.hasNext())
		{
				/**This tmp reference points to Passenger object, keep the casting (Passenger) to help remember that we deal with Passengers*/
				 passengerTmp = it.next();
				
				/**Gets the destination Floor of the Passenger*/
				destinationFloor = passengerTmp.destinationFloor();
				
				/**It is possible, that there is no other passengers on the Elevator who aim to destination Floor
				   in this case a new pair of (destinationFloor, passenger) is created and put into HashMap hmFloorPassenger*/	
				if(hmFloorPassenger.get(destinationFloor) == null)
				{	
					hs_tmp =  new TreeSet<Passenger>(new PassengerCompare());
					hs_tmp.add(passengerTmp);	
					hmFloorPassenger.put(destinationFloor, hs_tmp);	
					/**Make underfined the current floor of the passenger under consideration
					   This change is important because the current floor is undefined when the passenger is on the elevator*/
					passengerTmp.currentFloorUndefined();
						
				}else{	
					/**This is the case, when there is at least one passengers on the Elevator who aim to destination Floor
						in this case one more passenger passengerTmp is added to hmFloorPassenger*/
					hmFloorPassenger.get(destinationFloor).add(passengerTmp);
					/**Make underfined the current floor of the passenger under consideration
					   This change is important because the current floor is undefined when the passenger is on the elevator*/
					passengerTmp.currentFloorUndefined();
				}
		}		
	}

}
