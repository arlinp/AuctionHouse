package BankProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Proxy design for the Bank
 */
public class BankProxy implements BankProcess {


    private Socket s = null;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    /**
     * Proxy design for the BankProxy. Creates a socket from the passed parameters
     *
     * @param hostname Hostname or IP
     * @param port Port number
     */
    public BankProxy(String hostname, int port) {

        try {
            s = new Socket(hostname, port);

            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

//        while(!s.isClosed()) {
//
//            processMessage();
//
//        }
    }

//    // TODO Throw in separate thread, if there is even a use for this
//    private void processMessage() {
//
//    }

    /**
     * Sends new AccountID to Bank
     * @param AccountID
     * @return Success
     */
    @Override
    public boolean addAccount(int AccountID) {

        BankRequest ar = new BankRequest(BankInfo.NEWACCOUNT);
        ar.setID(AccountID);

        try{
            os.writeObject(ar);
            BankRequest newAr = (BankRequest) is.readObject();
            return newAr.isSuccess();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        BankRequest ar = new BankRequest(BankInfo.GETBALANCE);
        ar.setID(AccountID);

        try {
            os.writeObject(ar);
            BankRequest newAr = (BankRequest) is.readObject();
            return newAr.getAmount();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Add the funds to the specified account number
     *  @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public boolean addFunds(int AccountID, double amount) {
        BankRequest ar = new BankRequest(BankInfo.ADD);
        ar.setID(AccountID);
        ar.setAmount(amount);

        try {
            os.writeObject(ar);
            BankRequest newAr = (BankRequest) is.readObject();
            return newAr.getStatus();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Removes the funds from the given account
     *  @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public boolean removeFunds(int AccountID, double amount) {
        BankRequest ar = new BankRequest(BankInfo.REMOVE);
        ar.setID(AccountID);
        ar.setAmount(amount);

        try {
            os.writeObject(ar);
            BankRequest newAr = (BankRequest) is.readObject();
            return newAr.getStatus();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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
        BankRequest br = new BankRequest(BankInfo.LOCK);
        br.setID(AccountID);
        br.setAmount(amount);

        try {
            os.writeObject(br);
            BankRequest response = (BankRequest) is.readObject();
            return response.getLockNumber();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Unlocks the lock given by the identifier!
     *  @param AccountID Unique Identifier of Account
     * @param lockID    Identifier of the lock
     */
    @Override
    public boolean unlockFunds(int AccountID, int lockID) {
        BankRequest br = new BankRequest(BankInfo.UNLOCK);
        br.setID(AccountID);
        br.setLockNumber(lockID);

        try {
            os.writeObject(br);
            BankRequest response = (BankRequest) is.readObject();
            return response.getStatus();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public boolean transferFunds(int fromID, int toID, double amount) {
        BankRequest br = new BankRequest(BankInfo.TRANSFER);
        br.setID(fromID);
        br.setToID(toID);
        br.setAmount(amount);

        try {
            os.writeObject(br);
            BankRequest response = (BankRequest) is.readObject();
            return response.getStatus();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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
        BankRequest br = new BankRequest(BankInfo.TRANSFER);
        br.setID(fromID);
        br.setToID(toID);
        br.setLockNumber(lockID);

        try {
            os.writeObject(br);
            BankRequest response = (BankRequest) is.readObject();
            return response.getStatus();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void close() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                s.close();
                System.out.println("shut down!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
