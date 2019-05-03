package BankProxy;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import SourcesToOrganize.AgentApp;
import SourcesToOrganize.NetworkDevice;

/**
 * Proxy design for the Bank
 */
public class BankProxy implements BankProcess, Runnable {

    private ConcurrentHashMap<Integer, BankRequest> messages = new ConcurrentHashMap<Integer, BankRequest>();
    private Socket s = null;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private boolean open;
    private AgentApp client;

    /**
     * Proxy design for the BankProxy. Creates a socket from the passed parameters
     *
     * @param hostname Hostname or IP
     * @param port Port number
     */
//    public BankProxy(String hostname, int port, AgentApp client) {
    public BankProxy(String hostname, int port, AgentApp client) {

        open = true;
        this.client = client;

        connectToServer(hostname, port);

        new Thread(this).start();
    }

    public BankProxy(String hostname, int port) {

        open = true;

        connectToServer(hostname, port);

        new Thread(this).start();
    }


    private void connectToServer(String hostname, int port) {
        try {
            while (s == null) {
                System.out.println("jmkj with " + port + " " + hostname);
                s = new Socket(hostname, port);

                System.out.println("Connected: " + s.isConnected());

                os = new ObjectOutputStream(s.getOutputStream());
                is = new ObjectInputStream(s.getInputStream());
            }

        } catch (IOException e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            connectToServer(hostname, port);
//            e.printStackTrace();
        }
    }

    /**
     * Makes an account for auction house or agent
     *
     * @return Account ID
     */
    @Override
    public int addAccount(int ID) {
        BankRequest request = new BankRequest(BankInfo.NEWACCOUNT);
        request.setID(ID);

        try{
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getID();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * Makes an account for auction house or agent
     *
     * @return Account ID
     */
    public int addAccount() {
        BankRequest request = new BankRequest(BankInfo.NEWACCOUNT);

        try{
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getID();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;

    }

    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public double getBalance(int AccountID) {
        BankRequest request = new BankRequest(BankInfo.GETBALANCE);
        request.setID(AccountID);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getAmount();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.ADD);
        request.setID(AccountID);
        request.setAmount(amount);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.REMOVE);
        request.setID(AccountID);
        request.setAmount(amount);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.LOCK);
        request.setID(AccountID);
        request.setAmount(amount);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getLockNumber();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.UNLOCK);
        request.setID(AccountID);
        request.setLockNumber(lockID);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.TRANSFER);
        request.setID(fromID);
        request.setToID(toID);
        request.setAmount(amount);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
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
        BankRequest request = new BankRequest(BankInfo.TRANSFERFROMLOCK);
        request.setID(fromID);
        request.setToID(toID);
        request.setLockNumber(lockID);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Add new AuctionHouse server to Bank logs
     *
     * @param networkDevice Networked device to open for use
     * @return status
     */
    @Override
    public boolean openServer(NetworkDevice networkDevice) {

        BankRequest request = new BankRequest(BankInfo.OPENAUCTION);
        request.addNetworkDevices(networkDevice);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Close an AuctionHouse server to Bank logs
     *
     * @param networkDevice Networked device to close
     * @return status of closure
     */
    @Override
    public boolean closeServer(NetworkDevice networkDevice) {

        BankRequest request = new BankRequest(BankInfo.CLOSEAUCTION);
        request.addNetworkDevices(networkDevice);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getStatus();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get the servers currently listed within the Bank's systems
     *
     * @return List of servers
     */
    @Override
    public LinkedBlockingQueue<NetworkDevice> getServers() {
        BankRequest request = new BankRequest(BankInfo.GETAUCTIONS);

        try {
            os.writeObject(request);
            waitOn(request.getPacketID());

            BankRequest response = messages.get(request.getPacketID());
            messages.remove(response.getPacketID());
            return response.getNetworkDevices();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // TODO Implement notify bank of myself (A New AuctionHouse)

    public void close() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                //open = false;
                s.close();
                System.out.println("shut down!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }


    private boolean isOpen() {
        return open;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (isOpen()) {

            // Attempt to read and parse incoming messages
            BankRequest response = null;
            try {
                response = (BankRequest) is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Process the messages
            if (response.getAck()) {
                messages.put(response.getPacketID(), response);
                synchronized (this) { notify(); }
            } else {
//                System.out.println("Processing");
                processMessage(response);

            }
        }

    }


    // TODO Hey a new auction that we can talk to!
    private void processMessage(BankRequest notification) {
        switch (notification.getType()) {
            case OPENAUCTION:
                // We ignore if we are a proxy
                if (client != null) {
                    client.addAuctionHouse(notification.getNetworkDevice());
                    System.out.println("New server to add " + notification.getNetworkDevice());
                } else {
                    System.out.println("No new server to add because I am already an auction");
                }
                break;
        }
    }


    private void waitOn(int packetID) {
        synchronized (this) {
            while (!messages.containsKey(packetID)) {
                try {
                    System.out.println("Waiting on " + packetID);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
