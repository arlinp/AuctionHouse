package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentCommandLine extends AgentApp{


    private Agent agent;

    AgentCommandLine(){
        boolean test = false;

        Scanner inScanner = new Scanner(System.in);
        String input;

        System.out.println("1. Test on own PC?");
        System.out.println("2. Test in CS lab?");


        input = inScanner.nextLine();

        //Initialize the bank and auction proxy
        BankProxy bankProxy = null;
        AuctionProxy auctionProxy = null;

        try {


            if (input.equals("1")) {

//                bankProxy = new BankProxy("localHost", 42069, this);
//                auctionProxy = new AuctionProxy("localhost", 42070);
                localTest();

            }

            if (input.equals("2")) {

                System.out.println("enter bank host:");
                bankProxy = new BankProxy(inScanner.nextLine(), 42069, this);

                System.out.println("enter auctionhouse host:");
                auctionProxy = new AuctionProxy(inScanner.nextLine(), 42070);

                agent = new Agent(bankProxy, auctionProxy);

            }

            if (bankProxy == null) throw new ConnectException();

            if (auctionProxy == null) throw new ConnectException();

        } catch (ConnectException e) {


        }

        //int accountID = bankProxy.addAccount();
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

                if (agent.getAccountID() == 0) {

                    System.out.println("Your Account ID is: ");
                    System.out.println(" or nothing...");
                    input = inScanner.nextLine();

                    if (input.equals("")) {
                        agent.setAccountID(5);
                    } else {
                        agent.setAccountID(Integer.parseInt(input));
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

                System.out.println("Items for auction: ");
                ArrayList<ItemInfo> auctionItems = agent.getItems();

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

//                System.out.println("making bid on: " + auctionItems.get(itemIndex) + " " +
//                        "for : " + amount);
//
//                auctionProxy.bid(new Bid(amount,accountID,auctionItems.get(itemIndex).getItemID()));

                agent.bid(new Bid(amount, agent.getAccountID(), itemIndex));
            }
        }
    }

    private void localTest() {


        agent = new Agent(
                new BankProxy("localHost", 42069, this),
                new AuctionProxy("localhost", 42070));
    }

    public static void main(String[] args) {
        new AgentCommandLine();
    }
}
