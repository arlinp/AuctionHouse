package SynchronizationTest;

import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Bidder implements Runnable{

    private int account;
    private BankProxy bankProxy = null;
    private AuctionProxy auctionProxy = null;

    public Bidder(int accountID, int bankPort, int aucPort) {

        bankProxy = new BankProxy("127.0.0.1", bankPort, null);
        auctionProxy = new AuctionProxy("127.0.0.1", aucPort, null);
        account = bankProxy.addAccount(accountID);
        bankProxy.addFunds(account, 10000000.00);

        System.out.println("Balance: " + bankProxy.getBalance(account));
    }

    @Override
    public void run() {
            //repeatedly try to bid on first item
//        for (int i = 0; i < 1000000; i+=100) {
//            System.out.println(i);

        auctionProxy.bid(new Bid(1000, account, 2));
        System.out.println(auctionProxy.getItems());
        auctionProxy.bid(new Bid(2000+account, account, 2));
        System.out.println(auctionProxy.getItems());
//      if (System.currentTimeMillis() > auctionItems.get(0).getTime()+1000) return;
//        }
    }
}
