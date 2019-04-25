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
    ArrayList<ItemInfo> auctionItems;

    public Bidder(int accountID){
        account = accountID;
    }

    public Bidder(int accountID, AuctionProxy auctionProxy, BankProxy bankProxy) {
        account = accountID;
        this.auctionProxy = auctionProxy;
        this.bankProxy = bankProxy;
        auctionItems = auctionProxy.getItems();
    }

    @Override
    public void run() {

        auctionProxy.bid(new Bid(100, account, 2));

    }
}
