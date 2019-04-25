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

    public Bidder(int accountID, int bankPort, int aucPort) {

        bankProxy = new BankProxy("127.0.0.1", bankPort, null);
        auctionProxy = new AuctionProxy("127.0.0.1", aucPort);
        auctionItems = auctionProxy.getItems();

        account = bankProxy.addAccount(accountID);
        bankProxy.addFunds(account, 10000000.00);

        System.out.println("Balance: " + bankProxy.getBalance(account));
    }

    @Override
    public void run() {
            //repeatedly try to bid on first item
        for (int i = 0; i < 1000000; i+=100) {
            System.out.println(account + " Has $" +bankProxy.getBalance(account));
            auctionProxy.bid(new Bid(i, account, auctionItems.get(0).getItemID()));
            if (System.currentTimeMillis() > auctionItems.get(0).getTime()+1000) return;
        }
    }
}
