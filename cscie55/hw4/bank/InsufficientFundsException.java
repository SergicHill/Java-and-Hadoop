/** Class InsufficientFundsException  - a part of hw 4
 *  @version October, 2015
 */

package cscie55.hw4.bank;

/**This exception is thrown when the amount to withdraw from an account is less than the amount on that account. */
public class InsufficientFundsException extends Exception
{
    public InsufficientFundsException(Account account, long withdrawal)
    {
        super(String.format("Insufficient Funds: Attempt to withdraw %d from %s", withdrawal, account ));
	   // System.out.println(getMessage()) ;
    }
}
