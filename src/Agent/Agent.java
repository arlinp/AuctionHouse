package Agent;

import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import AuctionProxy.AuctionProcess;
import AuctionProxy.BidInfo;
import BankProxy.BankProxy;
import BankProxy.BankProcess;
import SourcesToOrganize.Bid;

import java.util.ArrayList;

public class Agent implements BankProcess, AuctionProcess {

    BankProxy bankProxy = null;
    AuctionProxy auctionProxy = null;

    public Agent(BankProxy bankProxy, AuctionProxy auctionProxy){

        this.auctionProxy = auctionProxy;
        this.bankProxy = bankProxy;
    }

    public int addAccount(int accountID){

        return bankProxy.addAccount(accountID);
    }

    @Override
    public BidInfo bid(Bid bid) {
        return auctionProxy.bid(bid);
    }

    @Override
    public ItemInfo getItemInfo(int itemID) {
        return auctionProxy.getItemInfo(itemID);
    }

    @Override
    public ArrayList<ItemInfo> getItems() {
        return auctionProxy.getItems();
    }

    @Override
    public String helloInternet(String s) {
        return auctionProxy.helloInternet(s);
    }

    @Override
    public double getBalance(int AccountID) {
        return bankProxy.getBalance(AccountID);
    }

    @Override
    public boolean addFunds(int AccountID, double amount) {
        return bankProxy.addFunds(AccountID, amount);
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

    @Override
    public boolean newServer(String ipAddress, int port) {
        return bankProxy.newServer(ipAddress,port);
    }


}
