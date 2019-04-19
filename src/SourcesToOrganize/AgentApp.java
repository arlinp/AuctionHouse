package SourcesToOrganize;

import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class AgentApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public static void main(String[] args) {

        Scanner inScanner = new Scanner(System.in);
        String input;

        System.out.println("1. Test on own PC?");
        System.out.println("2. Test in CS lab?");


        input = inScanner.nextLine();

        //Initialize the bank and auction proxy
        BankProxy bankProxy = null;
        AuctionProxy auctionProxy = null;

        if (input.equals("1")){

            bankProxy = new BankProxy("localHost", 2000);
            auctionProxy = new AuctionProxy("localhost", 2001);

        }

        if (input.equals("2")){

            System.out.println("enter bank host:");
            input = inScanner.nextLine();
            bankProxy = new BankProxy(input, 2000);

            System.out.println("enter auctionhouse host:");
            input = inScanner.nextLine();
            auctionProxy = new AuctionProxy(input, 2001);

        }

        int accountID = 0;

        //main loop
        while (!input.equals("exit")) {

            //access bank or access auction
            System.out.println("what would you like to do?");
            System.out.println("1. Access bank.");
            System.out.println("2. Access auction house.");
            System.out.println("exit. to end program");

            input = inScanner.nextLine();

            //if bank, get balance
            if (input.equals("1")) {

                System.out.println("enter account ID: ");
                System.out.println(" or nothing...");
                input  = inScanner.nextLine();

                if (input.equals("")) {
                    accountID = 5;
                } else {
                    accountID = Integer.parseInt(input);
                }


                System.out.println("making new Account, ID=" + accountID);
                bankProxy.addAccount(accountID);
                System.out.println("account balance = "
                        + bankProxy.getBalance(accountID));

                while (!input.equals("done")) {

                    System.out.println("1. add to funds.");
                    System.out.println("done. to exit bank");
                    input = inScanner.nextLine();

                    if (input.equals("1")) {

                        System.out.println("Enter amount to deposit:");
                        input = inScanner.nextLine();

                        bankProxy.addFunds(accountID, Integer.parseInt(input));

                        System.out.println("New Balance: " + bankProxy.getBalance(accountID));
                    }

                }

            }

            //if auction, make bid
            if (input.equals("2")) {

                System.out.println("Items for auction: ");
                ArrayList<ItemInfo> auctionItems = auctionProxy.getItems();

                System.out.println("enter number to make bid");

                for (int i = 0; i < auctionItems.size(); i++) {
                    System.out.println("" + i + " : " + auctionItems.get(i));
                }

                input = inScanner.nextLine();

                System.out.println("Bid attempt: " + input);
                int itemIndex = Integer.parseInt(input);
                System.out.println();

                System.out.println("enter amount: ");
                input = inScanner.nextLine();
                double amount = Double.parseDouble(input);

                System.out.println("making bid on: " + auctionItems.get(itemIndex) + " " +
                        "for : " + amount);

                auctionProxy.bid(new Bid(amount,accountID,auctionItems.get(itemIndex).getItemID()));

            }
        }

    }
}
