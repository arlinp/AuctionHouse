package Bank;

import static Bank.Bank.counter;

public class Account {

    private double balance;
    private double uniqueID;
    private Object lock;
    private double lockedMoney;

    public Account(){
        this.balance = 10;
        this.uniqueID = counter;

    }

    /**
     * Get the balance of the Account number
     *
     * @return Amount of money
     */
    public synchronized double getBalance() {
        return this.balance;
    }

    /**
     * Add the funds to the specified account number
     *
     * @param amount    Amount of Money
     */
    public synchronized void addFunds(double amount) {
        this.balance += amount;
    }

    /**
     * Removes the funds from the given account
     *
     * @param amount    Amount of Money
     */
    public synchronized void removeFunds(double amount) {
        this.balance -= amount;
    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @return Random/Unique Integer of lock, for later retrieval
     */
    public synchronized boolean lockFunds(Double amount) {
        if( (this.balance - amount) <0){ return false;}

        else{
                this.lockedMoney = amount;
                this.balance -= lockedMoney;
                return true;
            }
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     */
    public synchronized boolean unlockFunds(Double amount) {
        if(amount > this.lockedMoney){ return false;}
        else{
            this.balance += amount;
            this.lockedMoney -= amount;
            return true;
        }
    }
}
