/** Class DuplicateAccountException  - a part of hw 4
 *  @version October, 2015
 */
package cscie55.hw4.bank;

/**This exception is thrown when a duplicate account is ordered*/
public class DuplicateAccountException extends Exception
{
    public DuplicateAccountException(int accountId)
    {
        super(String.format("Attempt to add a second account with id %d", accountId));
		//System.out.println(getMessage()) ;
    }
}
