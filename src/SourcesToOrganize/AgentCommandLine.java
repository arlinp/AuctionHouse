package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class AgentCommandLine extends AgentApp{


    private Agent agent;
    LinkedBlockingQueue<NetworkDevice> servers;

    AgentCommandLine(){
        boolean test = true;

        Scanner inScanner = new Scanner(System.in);
        String input;

        System.out.println("1. Test on own PC?");
        System.out.println("2. Test in CS lab?");


        input = inScanner.nextLine();

        //Initialize the bank and auction proxy
        BankProxy bankProxy = null;
        AuctionProxy auctionProxy = null;

//        try {


            if (input.equals("1")) {

//                bankProxy = new BankProxy("localHost", 42069, this);
//                auctionProxy = new AuctionProxy("localhost", 42070);
//                localTest();
                agent = new Agent("localhost", 42069);

            }

//            if (input.equals("2")) {
//
//                System.out.println("enter bank host:");
//                bankProxy = new BankProxy(inScanner.nextLine(), 42071, this);
//
//
//                System.out.println("enter auctionhouse host:");
//                auctionProxy = new AuctionProxy(inScanner.nextLine(), 42070);
//
//                agent = new Agent(bankProxy, auctionProxy);
//
//            }
//
//            System.out.println(bankProxy);
            //if (bankProxy == null) throw new ConnectException();

            //if (auctionProxy == null) throw new ConnectException();

//        } catch (ConnectException e) {
//            System.out.println("A connect exception happened");
//            e.printStackTrace();
//
//        }

        //int accountID = bankProxy.addAccount();
        int accountID = 0;

        //main loop
        while (!input.equals("exit")
                //|| input.equalsIgnoreCase("menu")
        )
        {

            //access bank or access auction
            System.out.println("what would you like to do?");
            System.out.println("1. Access bank.");
            System.out.println("2. Access auction house.");
            System.out.println("exit. to end program");

            input = inScanner.nextLine();

            //if bank, get balance
            if (input.equals("1")) {

                if (agent.getAccountID() == 0) {

                    System.out.println("Your Account ID is: ");
                    System.out.println(" or nothing...");
                    input = inScanner.nextLine();

                    if (input.equals("")) {
                        agent.addAccount(5);
                    } else {
                        agent.addAccount(Integer.parseInt(input));
                    }




                    System.out.println("Account ID=" + agent.getAccountID());
                    System.out.println("account balance = "
                            + agent.getBalance());
                }

                while (!input.equals("0")) {

                    System.out.println("1. add to funds.");
                    System.out.println("0. to exit bank");
                    input = inScanner.nextLine();

                    if (input.equals("1")) {

                        System.out.println("Enter amount to deposit:");
                        input = inScanner.nextLine();

                        agent.addFunds(Double.parseDouble(input));

                        System.out.println("New Balance: " + agent.getBalance());
                    }

                }

            }

            //if auction, make bid
            if (input.equals("2")) {


                ArrayList<ItemInfo> auctionItems = agent.getItems();

                while(!auctionItems.isEmpty()) {
                    System.out.println("Items for auction: ");
                    for (ItemInfo info : auctionItems) {
                        System.out.println(info);
                    }

                    System.out.println("Please enter a number to make bid\nOr type \"Menu\" to exit to the menu");


                    input = inScanner.nextLine();

                    if (input.equalsIgnoreCase("menu")) break;

                    System.out.println("Bid attempt: " + input);
                    int itemIndex = Integer.parseInt(input);
                    System.out.println();

                    System.out.println("Enter amount: ");
                    input = inScanner.nextLine();
                    double amount = Double.parseDouble(input);

//                System.out.println("making bid on: " + auctionItems.get(itemIndex) + " " +
//                        "for : " + amount);
//
//                auctionProxy.bid(new Bid(amount,accountID,auctionItems.get(itemIndex).getItemID()));

                    agent.bid(new Bid(amount, agent.getAccountID(), itemIndex));
                }
                    System.out.println("Sorry, no more items in this auction");
            }
        }

        if(input.equals("exit")){
            //check if any bids
            //tryToClose();
            System.out.println("You quit the program");
            System.out.println("Let's check to see if there are any bids first");
            //while(auctionProxy.)
        }
    }



    @Override
    public void addAuctionHouse(NetworkDevice networkDevice) {

    }

    public static void main(String[] args) {
        new AgentCommandLine();
    }
}
