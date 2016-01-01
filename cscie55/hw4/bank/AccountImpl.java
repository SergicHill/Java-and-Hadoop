/** Class AccountImpl  - a part of hw 4
 *  @author Serguey Khovansky
 *  @version October, 2015
 */

package cscie55.hw4.bank;


/**
*The class  AccountImpl  implements interface  Account.  It is an account in a bank with two features: an id, i.e. number of the account, and
* the balance of the account. The balance can be modified by deposting and withdrawing.
*/
public class AccountImpl implements Account{
		private final int id;
		private long balance;
		
		/**Constructor for AccountImpl*/		
	public	AccountImpl( int id)
		{
			this.id = id;
			this.balance = 0;
		}
		
	
		/**The id of the account
		@return id */
	public  int id()
		{
			return id;
		}
    
		/**The balance of the account
	       @return balance */
	public long balance()
		{
			return balance;
		}
	
		/**Deposits the amount
	       @param amount*/
	public     void deposit(long amount) throws IllegalArgumentException
		{
			if(amount > 0) 
			{
				balance += amount;  	
			} else if (amount < 0){
				throw  new IllegalArgumentException("Error:  AccountImpl:deposit negative deposit"); 
			}else{ 
				throw  new IllegalArgumentException("Error:  AccountImpl:deposit zero deposit");
			}	
	}
	
		/**Withdraws from the account the given amount
			@param amount */
	public    void withdraw(long amount) throws InsufficientFundsException
		{
			if(amount > 0) 
			{
				if(balance < amount) throw  new InsufficientFundsException( this, amount);
				balance -= amount;  	
			} else if (amount < 0){
				throw  new IllegalArgumentException("Error: AccountImpl:withdraw negative withdraw"); 
			}else{ 
				throw  new IllegalArgumentException("Error: AccountImpl:withdraw zero withdraw");
			}
		}
	
	public String toString()
	{
		String s = "" + this.id;
		return s;
	}


}
