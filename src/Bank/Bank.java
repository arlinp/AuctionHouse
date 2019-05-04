package Bank;

import BankProxy.BankProcess;
import SourcesToOrganize.NetworkDevice;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import static SourcesToOrganize.AgentApp.bankPort;

/**
 * Runs a Bank where accounts can be interacted with through a socket connection
 */
public class Bank implements BankProcess {

    // Debugging flags
    public static final boolean BANKCOMMDEBUG = true;
    public static final boolean BANKDEBUG = false;

    // Used data structures
    public  Random ran = new Random();
    private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();
    private HashMap<Integer, Double> lockedMoney = new HashMap<Integer, Double>();
    private LinkedBlockingQueue<NetworkDevice> auctionNetworkDevices = new LinkedBlockingQueue<>();
    // TODO replace with Thread pool
    private LinkedBlockingQueue<BankCommunicator> bankCommunicators = new LinkedBlockingQueue<>();


    private int accountCount = 0;

    /**
     * Constructor for Bank
     *
     * @param port Creates a server on the given port
     */
    public Bank(int port) {

        // Attempt to create a new server socket
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            if (BANKDEBUG) System.out.println("Started a server on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (BANKDEBUG) System.out.println("Accepting Connections...");

        // Accept connections while true
        while (isAlive()) {
            try {
                System.out.println("Accepting Connections");
                Socket s = ss.accept();
                BankCommunicator ac = new BankCommunicator(s,this);
                bankCommunicators.put(ac);

                if (BANKDEBUG) System.out.println("Started new BankCommunicator for: " + s.getRemoteSocketAddress());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    // TODO implement
    /**
     * Checks whether the Bank is alive
     *
     * @return boolean for aliveness
     */
    public boolean isAlive() {
        return true;
    }

    /**
     * Makes an account for auction house or agent
     *
     * @return Account ID
     */
    @Override
    public synchronized int addAccount(int ID) {
        // Iterate counter

        // Add a new account to the existing accounts. New account
        // created with a random number.
        //int generatedID = generateAccountID();
        Account newAccount = new Account(ID);

        //Save the Account ID and Account to HashTable
        accounts.put(newAccount.getUniqueID(), newAccount);

        // Return new Unique ID
        return newAccount.getUniqueID();
    }

    /**
     * Makes an account for auction house or agent
     *
     * @return Account ID
     */
    public synchronized int addAccount() {
        // Iterate counter

        // Add a new account to the existing accounts. New account
        // created with a random number.
        //int generatedID = generateAccountID();
        Account newAccount = new Account(accountCount);
        accountCount++;

        //Save the Account ID and Account to HashTable
        accounts.put(newAccount.getUniqueID(), newAccount);

        // Return new Unique ID
        return newAccount.getUniqueID();
    }

    public int generateAccountID() {
        // Generate a random ID
        int number = ran.nextInt(1000);

        //TODO Check Hashmap TEST THIS TO MAKE SURE IT WORKS

        if(accounts.containsKey(number)){
            generateAccountID();
        }
        return number;

    }

    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public synchronized double getBalance(int AccountID) {
        // Run check that would cause crash
//        for (Integer accountNum : accounts.keySet()){
//            System.out.println(accountNum);
//        }

        if (!accounts.containsKey(AccountID)) {
            System.out.println("CAN'T FIND THAT ACCOUNT");
            return -1.0;
        }

        // Get the balance of an account!
        Account account = accounts.get(AccountID);
        return account.getBalance();
    }

    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public synchronized boolean addFunds(int AccountID, double amount) {
        // Run check that would cause crash
        if (!accounts.containsKey(AccountID)) return false;

        // Get the account
        Account account = accounts.get(AccountID);

        // Add funds to an account
        account.addFunds(amount);
        return true;
    }

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public boolean removeFunds(int AccountID, double amount) {
        // Run check that would cause crash
        if (!accounts.containsKey(AccountID)) return false;

        Account account = accounts.get(AccountID);

        // Run logical check on balance
        if (account.getBalance() < amount) return false;

        // Remove a given amount of funds
        account.removeFunds(amount);
        return true;
    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     * @return Random/Unique Integer of lock, for later retrieval
     */
    @Override
    public synchronized int lockFunds(int AccountID, double amount) {
        // Run check that would cause crash
        if (!accounts.containsKey(AccountID)) return -1;

        Account account = accounts.get(AccountID);

        // Return the lockID
        return account.lockFunds(amount);
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     * @param AccountID Unique Identifier of Account
     * @param lockID    Identifier of the lock
     */
    @Override
    public synchronized boolean unlockFunds(int AccountID, int lockID) {
        // Run check that would cause crash
        if (!accounts.containsKey(AccountID)) return false;

        Account account = accounts.get(AccountID);

        // Return status of unlock
        return account.unlockFunds(lockID);
    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public synchronized boolean transferFunds(int fromID, int toID, double amount) {
        // Run check that would cause crash
        if (!accounts.containsKey(fromID) && !accounts.containsKey(toID)) {
            return false;
        }

        // Get the accounts
        Account account1 = accounts.get(fromID);
        Account account2 = accounts.get(toID);

        // Check for balance
        if (account1.getBalance() < amount) return false;

        // Exchange funds!
        account1.removeFunds(amount);
        account2.addFunds(amount);
        return true;
    }

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    @Override
    public synchronized boolean transferFunds(int fromID, int toID, int lockID) {
        // Run check that would cause crash
        if (!accounts.containsKey(fromID) && !accounts.containsKey(toID)) return false;

        // Get the accounts
        Account account1 = accounts.get(fromID);
        Account account2 = accounts.get(toID);

        System.out.println(account1 + " " + account2);
        double amount = account1.getLockedFunds(lockID);

        // Synchronize on the first account so that the code is ran
        // consecutively consistently
        synchronized (account1) {
            if (account1.unlockFunds(lockID)) {
                account1.removeFunds(amount);
                account2.addFunds(amount);
                return true;
            }
        }

        // Return false if it couldn't transfer the funds
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
        auctionNetworkDevices.add(networkDevice);
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
        auctionNetworkDevices.remove(networkDevice);
        return false;
    }

    /**
     * Get the servers currently listed within the Bank's systems
     *
     * @return List of servers
     */
    @Override
    public LinkedBlockingQueue<NetworkDevice> getServers() {
        return auctionNetworkDevices;
    }

    /**
     *
     * @param networkDevice
     */
    public void notifyAuction(NetworkDevice networkDevice) {
        for (BankCommunicator bc : bankCommunicators) {
            bc.notifyNewAuction(networkDevice);
        }
    }


    public static void main(String[] args) {
        if (args.length == 1) {
            int operatingPort;
            try {
                operatingPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Input not correct:\n Correct usage: Bank" +
                        " <Operating Port>");
                return;
            }

            Bank bank = new Bank(operatingPort);
        } else {
            Bank bank = new Bank(bankPort);
        }



    }

}

