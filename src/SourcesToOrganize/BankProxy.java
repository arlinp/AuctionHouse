package SourcesToOrganize;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Proxy design for the Bank
 */
public class BankProxy implements BankProcess {

    private ObjectInputStream is;
    private ObjectOutputStream os;

    /**
     * Proxy design for the BankProxy. Creates a socket from the passed parameters
     *
     * @param hostname Hostname or IP
     * @param port Port number
     */
    BankProxy(String hostname, int port) {
        Socket s = null;
        try {
            s = new Socket(hostname, port);

            is = new ObjectInputStream(s.getInputStream());
            os = new ObjectOutputStream(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!s.isClosed()) {

            processMessage();

        }
    }

    // TODO Throw in separate thread, if there is even a use for this
    private void processMessage() {

    }


    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public double getBalance(int AccountID) {
        return 0;
    }

    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public void addFunds(int AccountID, double amount) {

    }

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public void removeFunds(int AccountID, double amount) {

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
        BankRequest br = new BankRequest();

        try {
            os.writeObject(br);
            BankRequest response = (BankRequest) is.readObject();
            return response.getLock();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     * @param AccountID Unique Identifier of Account
     * @param lockID    Identifier of the lock
     */
    @Override
    public void unlockFunds(int AccountID, int lockID) {
        BankRequest br = new BankRequest();

        try {
            os.writeObject(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public void transferFunds(int fromID, int toID, double amount) {
        BankRequest br = new BankRequest();

        try {
            os.writeObject(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    @Override
    public void transferFunds(int fromID, int toID, int lockID) {
        BankRequest br = new BankRequest();

        try {
            os.writeObject(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
