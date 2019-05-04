package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main running command line agent
 */
public class AgentCommandLine extends AgentApp{

    private Agent agent;
    private LinkedBlockingQueue<NetworkDevice> servers;

    /**
     * Constructs a new Agent controlled through
     * the command line.
     */
    public AgentCommandLine(){
        boolean test = true;

        Scanner inScanner = new Scanner(System.in);
        String input;

        System.out.println("1. Test on own PC?");
        System.out.println("2. Test in CS lab?");


        input = inScanner.nextLine();

        //Initialize the bank and auction proxy
        String bankHost;
        int portNumber;

        try {

            if (input.equals("1")) {

                agent = new Agent("localhost", 42069);

            }

            if (input.equals("2")) {

                System.out.println("enter bank host:");
                bankHost = inScanner.nextLine();


                System.out.println("enter bank port:");
                portNumber = inScanner.nextInt();

                agent = new Agent(bankHost, portNumber);

            }
        }
        catch (NullPointerException e){
            return;
        }

        //main loop goes until "exit"
        while (!input.equals("exit"))
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

                    agent.bid(new Bid(amount, agent.getAccountID(), itemIndex));
                }
                    System.out.println("Sorry, no more items in this auction");
            }
        }

        if(input.equals("exit")){

            System.out.println("You want to exit the Auction House");
            System.out.println("Let's check to see if there are any bids first");

        }
    }

    /**
     * Runs a new Agent on the command line
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        new AgentCommandLine();
    }

}
