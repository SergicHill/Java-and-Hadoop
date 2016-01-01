/** PassengerCompare class is a part of hw3
 *  Use in order to compare passengers in TreeSet that keeps passengers
 *  The comparison is implemented via passengers' id
 *
 * @author Serguey Khovansky
 * @version October, 2015
 */

package  cscie55.hw3;
import java.util.Comparator;

class PassengerCompare implements Comparator<Passenger>
{	
	@Override
	public int compare(Passenger p1, Passenger p2)
	{	
		return (p1.get_id() - p2.get_id());
	}
}