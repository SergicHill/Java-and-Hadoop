/** Interface Account  - a part of hw 4
 *  @version October, 2015
 */
package cscie55.hw4.bank;

/**This is an interface for the account in a bank*/
public interface Account
{
	/**This method returns the id of the Account*/
    int id();
	/**This method returns the total balance on the Account*/
    long balance();
	/**This method makes a deposit to the Account in the given amount*/
    void deposit(long amount);
	/**This method withdraws from the Account the given amount*/
    void withdraw(long amount) throws InsufficientFundsException;
}
