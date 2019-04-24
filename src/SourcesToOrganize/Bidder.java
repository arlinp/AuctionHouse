package SourcesToOrganize;

import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

import java.util.ArrayList;

public class Bidder implements Runnable{

    private int account;
    BankProxy bankProxy = null;
    AuctionProxy auctionProxy = null;
    ArrayList<ItemInfo> auctionItems = auctionProxy.getItems();

    public Bidder(int accountID){
        account = accountID;
    }

    public Bidder(int accountID, AuctionProxy auctionProxy, BankProxy bankProxy) {
        account = accountID;
        this.auctionProxy = auctionProxy;
        this.bankProxy = bankProxy;
    }

    @Override
    public void run() {


    }
}
