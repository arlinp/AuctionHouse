package SourcesToOrganize;

import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

import java.util.ArrayList;

public class Bidder implements Runnable{

    private int account;
    private BankProxy bankProxy = null;
    private AuctionProxy auctionProxy = null;
    private ArrayList<ItemInfo> auctionItems;

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
            //repeatedly try to bid on first item
        while(true) {
            auctionProxy.bid(new Bid(10, account, auctionItems.get(0).getItemID()));
        }

    }
}
