/** Class BankImpl  - a part of hw 4
 *  @author Serguey Khovansky
 *  @version October, 2015
 *  Answer sheet: C:\Users\SK\Documents\MyClasses\Harvard_cscie55\cscie55\hw4\test\cscie55\hw4
 */

package cscie55.hw4.bank;

import java.util.HashMap;
import java.util.Map;

/**
*The class   BankImpl  implements interface  Bank.  The BankImpl contains accounts, each of which has it speicific id,
 and allows for different operations with them
*/
public class BankImpl implements Bank{

	private Map<Integer, Account> hmAccounts;

		/**Constructor of BankImpl*/
	public BankImpl()
	{
		hmAccounts = new HashMap<Integer, Account>();
	}

		/**Add a new account to the bank
		   @param account  */
	public  void addAccount(Account account) throws DuplicateAccountException
	{
			/**Test that the id of the new account is indeed novel*/
			if( hmAccounts.containsKey(account.id())) throw new DuplicateAccountException(account.id());
			/**Place the new account into the bank*/
			hmAccounts.put((Integer)account.id(), account);
	}
	
	
		/** Withdraw on one account, and deposit on the other, without doing any synchronization. 
			This is completely wrong, but gives some idea of what happens when synchronization is missing
			@param fromId
			@param toId
			@param 	amount  */
	public    void transferWithoutLocking(int fromId, int toId, long amount) throws InsufficientFundsException
	{
			/**Test that the acount fromId has at least amount of money */
			if(hmAccounts.get(fromId).balance() >= amount)
			{
				/**Deduct the amount from fromId account*/
				  hmAccounts.get(fromId).withdraw(amount);
				
				/**Add the amount to the account toId*/
				hmAccounts.get(toId).deposit(amount);
				
			}else throw new InsufficientFundsException(hmAccounts.get(fromId), amount); //(Account account, long withdrawal)
	}
   

		/** Does the transfer while synchronizing on the Bank object.
			This means that only one thread can do a transfer at any given moment.
			While this approach does not provide any concurrency, it should be correct
			@param fromId
			@param toId
			@param amount */
	public  void transferLockingBank(int fromId, int toId, long amount) throws InsufficientFundsException
	{
			
			/**Once a thread obtains the monitor on an object and begins executing a synchronized method, 
			   no other thread can execute any synchronized method on the object because to do so they would 
			   need the monitor, and likewise,*/
			synchronized (this) 
			{			
				/**Test that the acount fromId has at least amount of money */
				if(hmAccounts.get(fromId).balance() >= amount)
				{
					/**Deduct the amount from fromId account*/
					hmAccounts.get(fromId).withdraw(amount);
				
					/**Add the amount to the account toId*/
					hmAccounts.get(toId).deposit(amount);
					
				}else throw new InsufficientFundsException(hmAccounts.get(fromId), amount); //(Account account, long withdrawal)
			}

	}
			/** Does the transfer while synchronizing on the Account objects
			@param fromId
			@param toId
			@param amount */
	public  void  transferLockingAccounts(int fromId, int toId, long amount) throws InsufficientFundsException
	{		

			/**Locking only the fromId account*/ 
			synchronized (hmAccounts.get(fromId)) 
			{	
					if(hmAccounts.get(fromId).balance() >= amount)
					{
						/**Deduct the amount from fromId account*/
						hmAccounts.get(fromId).withdraw(amount);

					}else throw new InsufficientFundsException(hmAccounts.get(fromId), amount); 
			}
		
			/**Locking only the toId account*/ 
			synchronized (hmAccounts.get(toId))
			{			
					hmAccounts.get(toId).deposit(amount);	
			}
	}
    
		/**Compute total balance of the bank
		   @return totalBalance*/
	public	long totalBalances()
	{
		long total = 0;
		for(Map.Entry<Integer, Account>  entry : hmAccounts.entrySet())
		{					
				/**Iterate over Accounts and sum up the balance*/		
				total += entry.getValue().balance();
		}
		return total;
	}
	
		/**Computes the number of accounts
			@returns the number of accounts */
	public   int numberOfAccounts()
	{
		return hmAccounts.size();
	}
}
