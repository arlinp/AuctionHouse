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



    int accountID = 0;
    BankProxy bankProxy = null;
    private ArrayList<AuctionProxy> connectedAuctionProxys = new ArrayList<AuctionProxy>();
    private AuctionProxy currentAuctionProxy;

    LinkedList<Bid> bids = new LinkedList<>();

    public Agent(BankProxy bankProxy, AuctionProxy auctionProxy) {


        connectedAuctionProxys.add(auctionProxy);
        currentAuctionProxy = auctionProxy;

        this.bankProxy = bankProxy;
    }

    public int addAccount(int accountID) {
        setAccountID(bankProxy.addAccount(accountID));
        return accountID;
    }

    public ArrayList<AuctionProxy> getConnectedAuctionProxys() {
        return connectedAuctionProxys;
    }

    public LinkedList<Bid> getBids() {
        return bids;
    }

    @Override
    public BidInfo bid(Bid bid) {

        bids.add(bid);
        return currentAuctionProxy.bid(bid);
    }

    @Override
    public ItemInfo getItemInfo(int itemID) {
        return currentAuctionProxy.getItemInfo(itemID);
    }

    @Override
    public synchronized ArrayList<ItemInfo> getItems() {
        return currentAuctionProxy.getItems();
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

//    @Override
//    public boolean openServer(String ipAddress, int port) {
//        return bankProxy.openServer(ipAddress,port);
//    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

}
