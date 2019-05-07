package Agent;

import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import BankProxy.BankProcess;
import AuctionHouse.Bid;
import Network.NetworkDevice;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements BankProcess, AuctionProcess {

    // Manage bank account and get auction connections
    private BankProxy bankProxy;
    private int accountID = 0;

    // Connected auctions
    private ArrayList<AuctionProxy> connAP = new ArrayList<>();

    // AgentApp
    private AgentApp agentApp;


    /**
     * Connect to the bank to make an account and receive auction info
     *
     * @param bankHost host name
     * @param bankPort port number
     */
    public Agent(String bankHost, int bankPort, AgentApp agentApp) {
        this.agentApp = agentApp;
        bankProxy = new BankProxy(bankHost, bankPort, this);
    }

    /**
     * Request for a new bank account
     *
     * @return account number
     */
    public int addAccount(){
        if (accountID == 0) setAccountID(bankProxy.addAccount());

        return accountID;
    }

    /**
     * Getter for auction proxies
     *
     * @return arraylist of all connected auctions proxies
     */
    private ArrayList<AuctionProxy> getConnAP() {
        return connAP;
    }

    /**
     * Makes an account for auction house or agent
     *
     * @return Account ID
     */
    @Override
    public int addAccount(int ID) {
        return 0;
    }

    /**
     * To place a bid
     *
     * @param bid Bid object that contains elements
     */
    @Override
    public BidInfo bid(Bid bid) {
        return null;
    }

    /**
     * Gets the Item Info from the Item ID.
     *
     * @param itemID Identifier of Item
     * @return ItemInfo
     */
    @Override
    public ItemInfo getItemInfo(int itemID) {
        return null;
    }


    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public synchronized ArrayList<ItemInfo> getItems() {

        // Info from all auctions
        ArrayList<ItemInfo> itemInfos = new ArrayList<>();

        // Get info from each auction
        ArrayList<ItemInfo> auctionsItems;

        // Add all info form all auctions
        for (AuctionProxy auctionProxy : connAP) {

            // Set each info proxy so the agent can get updates for GUI and etc
            auctionsItems = auctionProxy.getItems();

            if (auctionsItems != null) {
                itemInfos.addAll(auctionsItems);
            }
        }

        return itemInfos;
    }

    /**
     * Connect to all the auction proxies that bank has let us know about
     */
    public void connectToAuctions(){
        for (NetworkDevice n : bankProxy.getServers()){
            addAuctionHouse(n);
        }
    }

    /**
     * Check to see if the close is allowed
     *
     * @param accountID Account ID to be used for checking
     * @return True if no active bids, false if active bids
     */
    @Override
    public boolean closeRequest(int accountID) {
        // Loop through the AuctionProxies and check each one
        for (AuctionProxy ap : connAP) {
            if (!ap.closeRequest(accountID)) return false;
        }
        return true;
    }

    /**
     * Check to see if the close is allowed
     *
     * @return True if no active bids, false if active bids
     */
    public boolean closeRequest() {
        return closeRequest(accountID);
    }


    /**
     * Gets balance of specific ID
     *
     * @param AccountID Unique Identifier of Account
     * @return balance of account
     */
    @Override
    public double getBalance(int AccountID) {
        return bankProxy.getBalance(AccountID);
    }


    /**
     * Get balance method used to get balance
     * of an Agent instance
     *
     * @return balance
     */
    public double getBalance() {
        return bankProxy.getBalance(accountID);
    }

    /**
     * Gets the total balance, including the locked amount
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public double getTotalBalance(int AccountID) {
        return bankProxy.getTotalBalance(AccountID);
    }

    /**
     * Calls interfaced function
     *
     * @return double
     */
    public double getTotalBalance() {
        return getTotalBalance(accountID);
    }
    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     */
    @Override
    public boolean addFunds(int AccountID, double amount) {
        return bankProxy.addFunds(AccountID, amount);
    }

    /**
     * Add the funds to the specified account number
     *
     * @param amount Amount to add
     * @return true if added
     */
    public boolean addFunds(double amount) {
        return bankProxy.addFunds(accountID, amount);
    }

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     */
    @Override
    public boolean removeFunds(int AccountID, double amount) {
        return bankProxy.removeFunds(AccountID, amount);
    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @param AccountID Unique Identifier of Account
     * @param amount Amount of Money
     * @return Random/Unique Integer of lock, for later retrieval
     */
    @Override
    public int lockFunds(int AccountID, double amount) {
        return bankProxy.lockFunds(AccountID, amount);
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     * @param AccountID Unique Identifier of Account
     * @param lockID Identifier of the lock
     */
    @Override
    public boolean unlockFunds(int AccountID, int lockID) {
        return bankProxy.unlockFunds(AccountID, lockID);
    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *
     * @param fromID Unique Identifier of Account1
     * @param toID Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public boolean transferFunds(int fromID, int toID, double amount) {
        return bankProxy.transferFunds(fromID,toID, amount);
    }

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *
     * @param fromID Unique Identifier of Account1
     * @param toID Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    @Override
    public boolean transferFunds(int fromID, int toID, int lockID) {
        return bankProxy.transferFunds(fromID, toID,lockID);
    }

    /**
     * Add new AuctionHouse server to Bank logs
     *
     * @param networkDevice Networked device to open for use
     * @return status
     */
    @Override
    public boolean openServer(NetworkDevice networkDevice) {
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
        return false;
    }

    /**
     * Get the servers currently listed within the Bank's systems
     *
     * @return List of servers
     */
    @Override
    public LinkedBlockingQueue<NetworkDevice> getServers() {
        return bankProxy.getServers();
    }

    /**
     * Get account number
     *
     * @return Account number
     */
    public int getAccountID() {
        return accountID;
    }

    /**
     * Set the account number of agent
     *
     * @param accountID Account number
     */
    private void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    /**
     * Add an auction house to the existing others
     *
     * Checks for duplicaties
     *
     * @param auction Network device of auction to add
     */
    public void addAuctionHouse(NetworkDevice auction) {

        // Add an Auction House if not equal
        for (AuctionProxy ap : getConnAP()) {
            InetAddress test = ap.getConnectedAddress();

            if (test.getHostAddress().equalsIgnoreCase(auction.getIpAddress())
                    && ap.getPort() == auction.getPort()) {
                return;
            }
        }
        // If not contained, add new proxy
        getConnAP().add(new AuctionProxy(auction.getIpAddress(), auction.getPort(), agentApp));
    }

    public BidInfo bid(AuctionProxy proxy, Bid bid) {
        return proxy.bid(bid);
    }

}
