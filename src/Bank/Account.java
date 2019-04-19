package Bank;

import java.util.Random;

import static Bank.Bank.counter;

public class Account {

    private double balance;
    private double uniqueID;
    private double lockedMoney;
    private int lockID;

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
    public synchronized int lockFunds(Double amount) {

        //Generates a random pin/lockID used for retrieval
        //of the funds later

        Random ran = new Random();
        lockID = ran.nextInt(9999);

        //to do: need to return error
        if( (this.balance - amount) <0){ return 0;}

        else{
                this.lockedMoney = amount;
                this.balance -= lockedMoney;
                return lockID;
            }
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     */
    public synchronized boolean unlockFunds(int lockID) {
        return true;
    }
}
