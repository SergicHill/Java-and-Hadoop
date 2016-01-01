/** Interface Bank  - a part of hw 4
 *  @version October, 2015
 */
package cscie55.hw4.bank;

/**This is an interface of a Bank*/
public interface Bank
{
	/**Adds a new account to the bank*/
    void addAccount(Account account) throws DuplicateAccountException;
	/**Transfer money from fromId account to toId account using multiple threads without locking*/
    void transferWithoutLocking(int fromId, int toId, long amount) throws InsufficientFundsException;
	/**Transfer money from fromId account to toId account using multiple threads with locking this bank's method*/
    void transferLockingBank(int fromId, int toId, long amount) throws InsufficientFundsException;
	/**Transfer money from fromId account to toId account using multiple threads with locking  bank's accounts*/
    void transferLockingAccounts(int fromId, int toId, long amount) throws InsufficientFundsException;
	/**Computes total balance across all accounts*/
    long totalBalances();
	/**Returns the total number of accounts in the bank*/
    int numberOfAccounts();

}
