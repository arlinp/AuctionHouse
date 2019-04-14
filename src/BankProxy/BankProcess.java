package BankProxy;

import SourcesToOrganize.BankProxy;

/**
 * Bank Functionality offered, which is to be implemented by the Bank and the ClientProxy/ServerProxy
 */
public interface BankProcess {

    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    double getBalance(int AccountID);

    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     */
    void addFunds(int AccountID, double amount);

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     */
    void removeFunds(int AccountID, double amount);

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     * @return Random/Unique Integer of lock, for later retrieval
     */
    int lockFunds(int AccountID, double amount);

    /**
     * Unlocks the lock given by the identifier!
     *
     * @param AccountID Unique Identifier of Account
     * @param lockID Identifier of the lock
     */
    void unlockFunds(int AccountID, int lockID);

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *
     * @param fromID Unique Identifier of Account1
     * @param toID Unique Identifier of Account2
     * @param amount Amount of Money
     */
    void transferFunds(int fromID, int toID, double amount);

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *
     * @param fromID Unique Identifier of Account1
     * @param toID Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    void transferFunds(int fromID, int toID, int lockID);


}
