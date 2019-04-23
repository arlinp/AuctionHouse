package Bank;

import java.util.HashMap;
import java.util.Random;

/**
 * Account class for use within the bank
 */
public class Account {

    private double balance = 0;
    private int uniqueID;
    private HashMap<Integer, Double> lockedMoney = new HashMap<>();

    /**
     * Initialize a new account with a balance of $10 and unique counter
     */
    public Account(int ID){
        this.uniqueID = ID;
    }

    /**
     * Get the balance of the Account number
     *
     * @return Amount of money
     */
    public synchronized double getBalance() {

        return balance;
    }

    /**
     * Add the funds to the specified account number
     *
     * @param amount    Amount of Money
     */
    public synchronized void addFunds(double amount) {
        balance += amount;
    }

    /**
     * Removes the funds from the given account
     *
     * @param amount    Amount of Money
     */
    public synchronized void removeFunds(double amount) {
        balance -= amount;
    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @return Random/Unique Integer of lock, for later retrieval
     */
    public synchronized int lockFunds(Double amount) {
        // Generates a random pin/lockID used for retrieval
        // of the funds later
        Random ran = new Random();
        int lockID = ran.nextInt(9999);

        // Run checks
        if (lockedMoney.containsKey(lockID)) {
            return lockFunds(amount);
        } else if (balance - amount < 0) {
            return 0;
        } else {
            balance -= amount;
            lockedMoney.put(lockID, amount);
            return lockID;
        }
    }

    /**
     * Unlocks the funds with the lockID
     *
     * @param lockID ID of the lock
     * @return True if unlocked
     */
    public synchronized boolean unlockFunds(int lockID) {
        // Check that locked ID is contained
        if (!lockedMoney.containsKey(lockID)) return false;

        // Add locked money back into the account
        addFunds(lockedMoney.get(lockID));
        lockedMoney.remove(lockID);

        return true;
    }

    /**
     * Returns the amount of money within the specified lock
     *
     * @param lockID ID of lock
     * @return Value of getting it from a HashMap
     */
    public synchronized double getLockedFunds(int lockID) {
        return lockedMoney.get(lockID);
    }


    /**
     * Returns the unique ID of the Account
     *
     * @return Unique ID
     */
    public synchronized int getUniqueID() {
        return uniqueID;
    }
    public synchronized void setUniqueID(int ID) { uniqueID = ID; }

}
