package Bank;

import BankProxy.BankProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank implements BankProcess {

    public Bank(int port) {

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket s = ss.accept();
                BankCommunicator ac = new BankCommunicator(s,this);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // TODO implement
    public boolean isAlive() {
        return true;
    }

    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public double getBalance(int AccountID) {
        System.out.println("Getting the balance for Account#: " + AccountID);
        return 0;
    }

    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public boolean addFunds(int AccountID, double amount) {
        System.out.println("Added $" + amount + " to the Account #: " + AccountID);
        return false;
    }

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public boolean removeFunds(int AccountID, double amount) {
        System.out.println("Removed $" + amount + " to the Account #: " + AccountID);
        return false;
    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     * @return Random/Unique Integer of lock, for later retrieval
     */
    @Override
    public int lockFunds(int AccountID, double amount) {
        System.out.println("Locked $" + amount + " from the Account #: " + AccountID);
        return 0;
    }

    /**
     * Unlocks the lock given by the identifier!
     *  @param AccountID Unique Identifier of Account
     * @param lockID    Identifier of the lock
     */
    @Override
    public boolean unlockFunds(int AccountID, int lockID) {
        System.out.println("Unlocked LockID# " + lockID + " to the Account #: " + AccountID);
        return false;
    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *  @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public boolean transferFunds(int fromID, int toID, double amount) {
        System.out.println("Transfered $" + amount + " from Account#: " + fromID + " to Account#: " + toID);
        return false;
    }

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *  @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    @Override
    public boolean transferFunds(int fromID, int toID, int lockID) {
        System.out.println("Transferred the lockID# " + lockID + " from Account# " + fromID + " to Account# " + toID);
        return false;
    }
}
