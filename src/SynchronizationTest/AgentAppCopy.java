package SynchronizationTest;

import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import SourcesToOrganize.Bid;
import SourcesToOrganize.Bidder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class AgentAppCopy{


    public static void main(String[] args) {




        int accountID = 69;
        int account2ID = 420;

        //Start the bidders with their accounts
        Bidder Bidder1 = new Bidder(accountID, 42069, 42070);
        Bidder Bidder2 = new Bidder(account2ID, 42069, 42070);

        Thread thread1 = new Thread(Bidder1);
        thread1.start();
        Thread thread2 = new Thread(Bidder2);
        thread2.start();


    }

}
