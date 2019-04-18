package SourcesToOrganize;

import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import javafx.application.Application;
import javafx.stage.Stage;

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
        AuctionProxy auctionProxy;

        if (input.equals("1")){

            bankProxy = new BankProxy("localHost", 2000);
//            auctionProxy = new AuctionProxy("localhost", 2001);

        }

        if (input.equals("2")){

            System.out.println("enter bank host:");
            input = inScanner.nextLine();
            bankProxy = new BankProxy(input, 2000);

            System.out.println("enter auctionhouse host:");
            input = inScanner.nextLine();
            auctionProxy = new AuctionProxy(input, 2001);

        }

        //access bank or access auction
        System.out.println("what would you like to do?");
        System.out.println("1. Access bank.");
        System.out.println("2. Access auction house.");

        input = inScanner.nextLine();

        //if bank, get balance
        if (input.equals("1")){

            System.out.println("making new Account, ID=5");
            bankProxy.addAccount(5);
            double balance = bankProxy.getBalance(5);
            System.out.println("account balance = " + balance);

        }

        //if auction, make bid
        if (input.equals("2")){


        }


    }
}
