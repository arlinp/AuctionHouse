package Agent;

import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import BankProxy.BankProcess;
import SourcesToOrganize.Bid;
import SourcesToOrganize.NetworkDevice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements BankProcess, AuctionProcess {


    //manage bank account and get auction connections
    private BankProxy bankProxy = null;
    int accountID = 0;

    //connected auctions
    private ArrayList<AuctionProxy> connectedAuctionProxys = new ArrayList<>();
    private LinkedBlockingQueue<NetworkDevice> connectedAuctionNetworkDevices;
    private AuctionProxy currentAuctionProxy;

    //bids made on items
    private LinkedList<Bid> bids = new LinkedList<>();


    /**
     * connect to the bank to make an account and receive auction info
     * @param bankHost
     * @param bankPort
     */
    public Agent(String bankHost, int bankPort){

        bankProxy = new BankProxy(bankHost, bankPort);
    }

    /**
     * request for a new bank account
     * @return account number
     */
    public int addAccount(){

        if (accountID == 0) setAccountID(bankProxy.addAccount());

        return accountID;
    }

    public ArrayList<AuctionProxy> getConnectedAuctionProxys() {
        return connectedAuctionProxys;
    }

    public LinkedList<Bid> getBids() {
        return bids;
    }

    @Override
    public int addAccount(int ID) {
        return 0;
    }

    @Override
    public BidInfo bid(Bid bid) {
        addToBids(bid);
        BidInfo info = currentAuctionProxy.bid(bid);
        return info;
    }

    @Override
    public ItemInfo getItemInfo(int itemID) {
        return currentAuctionProxy.getItemInfo(itemID);
    }

    public synchronized void addToBids(Bid bid){
        bids.add(bid);
    }

    public synchronized void removeBid(Bid bid){
        bids.remove(bid);
    }

    @Override
    public synchronized ArrayList<ItemInfo> getItems() {
        ArrayList<ItemInfo> itemInfos = new ArrayList<>();

        for (AuctionProxy auctionProxy : connectedAuctionProxys){
            itemInfos.addAll(auctionProxy.getItems());
        }

        return itemInfos;

    }

    public void connectToAuctions(){

        for ( NetworkDevice n : bankProxy.getServers()){

            connectedAuctionProxys.add(new AuctionProxy(n.getIpAddress(), n.getPort()));
        }
    }

    public boolean addAuctionProxy(AuctionProxy auctionProxy){
        connectedAuctionProxys.add(auctionProxy);
        return true;
    }

    public boolean addAuctionProxy(String host, int port){

        try{
            connectedAuctionProxys.add(new AuctionProxy(host, port));
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

        return currentAuctionProxy.closeRequest(accountID);
    }


    @Override
    public double getBalance(int AccountID) {
        return bankProxy.getBalance(AccountID);
    }

    public double getBalance() {
        return bankProxy.getBalance(accountID);
    }


    @Override
    public boolean addFunds(int AccountID, double amount) {
        return bankProxy.addFunds(AccountID, amount);
    }
    public boolean addFunds(double amount) {
        return bankProxy.addFunds(accountID, amount);
    }



    @Override
    public boolean removeFunds(int AccountID, double amount) {
        return bankProxy.removeFunds(AccountID, amount);
    }

    @Override
    public int lockFunds(int AccountID, double amount) {
        return bankProxy.lockFunds(AccountID, amount);
    }

    @Override
    public boolean unlockFunds(int AccountID, int lockID) {
        return bankProxy.unlockFunds(AccountID, lockID);
    }

    @Override
    public boolean transferFunds(int fromID, int toID, double amount) {
        return bankProxy.transferFunds(fromID,toID, amount);
    }

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


    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }


    public LinkedBlockingQueue<NetworkDevice> getConnectedAuctionNetworkDevices() {
        return connectedAuctionNetworkDevices;
    }

}
