package Agent;

import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import BankProxy.BankProcess;
import SourcesToOrganize.AgentApp;
import SourcesToOrganize.Bid;
import SourcesToOrganize.NetworkDevice;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements BankProcess, AuctionProcess {


    //manage bank account and get auction connections
    private BankProxy bankProxy = null;
    int accountID = 0;

    //connected auctions
    private ArrayList<AuctionProxy> connAP = new ArrayList<>();
    private LinkedBlockingQueue<NetworkDevice> connectedAuctionNetworkDevices;
    private AuctionProxy currentAuctionProxy;

    //bids made on items
    private LinkedList<Bid> bids = new LinkedList<>();

    // AgentApp
    private AgentApp agentApp;


    /**
     * connect to the bank to make an account and receive auction info
     * @param bankHost host name
     * @param bankPort port number
     */
    public Agent(String bankHost, int bankPort, AgentApp agentApp) {
        this.agentApp = agentApp;
        bankProxy = new BankProxy(bankHost, bankPort, this);
    }

    /**
     * request for a new bank account
     * @return account number
     */
    public int addAccount(){

        if (accountID == 0) setAccountID(bankProxy.addAccount());

        return accountID;
    }

    /**
     * Getter for auction proxies
     * @return arraylist of all connected auctions proxies
     */
    public ArrayList<AuctionProxy> getConnAP() {
        return connAP;
    }

    /**
     * @return returns list of current bids
     */
    public LinkedList<Bid> getBids() {
        return bids;
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
//        addToBids(bid);
//        BidInfo info = bid.getAuctionProxy().bid(bid);
//        return info;

        //ret
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
        return currentAuctionProxy.getItemInfo(itemID);
    }

    /**
     * Synchronized way of adding a bid to list
     * @param bid the bid to add
     */
    public synchronized void addToBids(Bid bid){
        bids.add(bid);
    }

    /**
     * Synchronized way o fremoving a bid from bid list
     * @param bid the bid to remove
     */
    public synchronized void removeBid(Bid bid){
        bids.remove(bid);
    }

    /**
     * Gets an ArrayList of all of the Items
     *
     * @return ArrayList of Items
     */
    @Override
    public synchronized ArrayList<ItemInfo> getItems() {

        //infos from all auctions
        ArrayList<ItemInfo> itemInfos = new ArrayList<>();

        //get infos from each auction
        ArrayList<ItemInfo> auctionsItems;

        //add all infos form all auctions
        for (AuctionProxy auctionProxy : connAP) {

            //set each infos proxy so the agent can get updates for GUI and etc
            auctionsItems = auctionProxy.getItems();
//            for (ItemInfo info : auctionsItems){
//                info.setProxy(auctionProxy);
//            }
            if (auctionsItems != null) {
                itemInfos.addAll(auctionsItems);
            }
        }

        return itemInfos;

    }

    /**
     * Connect to all the auction proxies that bank has let
     * us know about
     */
    public void connectToAuctions(){

        for (NetworkDevice n : bankProxy.getServers()){
            addAuctionHouse(n);
        }
    }

    /**
     * @param auctionProxy Auction to add
     * @return was it able to be added
     */
    public boolean addAuctionProxy(AuctionProxy auctionProxy){
        connAP.add(auctionProxy);
        return true;
    }

    /**
     * Add a proxy based on hostname and port
     *
     * @param host host name
     * @param port port number
     * @return
     */
    public boolean addAuctionProxy(String host, int port){

        try{
            connAP.add(new AuctionProxy(host, port, agentApp));
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Check to see if the close is allowed
     *
     * @param accountID Account ID to be used for checking
     * @return True if no active bids, false if active bids
     */
    @Override
    public boolean closeRequest(int accountID) {
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
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    /**
     * @return LinkedBlockingQueue of connected auction devices
     */
    public LinkedBlockingQueue<NetworkDevice> getConnectedAuctionNetworkDevices() {
        return connectedAuctionNetworkDevices;
    }

    /**
     * @param auction Network device of auction to add
     */
    public void addAuctionHouse(NetworkDevice auction) {

        System.out.println("Add auction " + auction);
        for (AuctionProxy ap : getConnAP()) {
            InetAddress test = ap.getConnectedAddress();

            if (test.getHostAddress().equalsIgnoreCase(auction.getIpAddress())
                    && ap.getPort() == auction.getPort()) {
                return;
            }
        }
        getConnAP().add(new AuctionProxy(auction.getIpAddress(), auction.getPort(), agentApp));
    }

    public BidInfo bid(AuctionProxy proxy, Bid bid) {
        return proxy.bid(bid);
    }

}
