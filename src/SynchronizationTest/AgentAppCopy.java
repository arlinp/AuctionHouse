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

        String input = "1";

        //Initialize the bank and auction proxy
        BankProxy bankProxy = null;
        AuctionProxy auctionProxy = null;

        bankProxy = new BankProxy("localHost", 42069, null);
        auctionProxy = new AuctionProxy("localhost", 42070);


        int accountID = 69;
        int account2ID = 420;


            accountID = bankProxy.addAccount(accountID);
            account2ID = bankProxy.addAccount(account2ID);

            bankProxy.addFunds(accountID, 600);
            bankProxy.addFunds(account2ID, 400);

            System.out.println("New Balance Account " + accountID + ": " + bankProxy.getBalance(accountID));
            System.out.println("New Balance Account " + account2ID + ": " + bankProxy.getBalance(account2ID));

            //Start the bidders with their accounts
            Bidder Bidder1 = new Bidder(accountID, auctionProxy, bankProxy);
            Bidder Bidder2 = new Bidder(account2ID, auctionProxy, bankProxy);

            Thread thread1 = new Thread(Bidder1);
            thread1.start();
            Thread thread2 = new Thread(Bidder2);
            thread2.start();


    }

}
