package Agent;

import AuctionHouse.Bid;
import AuctionHouse.ItemInfo;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main running command line agent
 */
public class AgentCommandLine extends AgentApp {

    private Agent agent;

    /**
     * Constructs a new Agent controlled through
     * the command line.
     */
    public AgentCommandLine(){

        Scanner inScanner = new Scanner(System.in);
        String input;

        System.out.println("1. Test on own PC?");
        System.out.println("2. Test in CS lab?");


        input = inScanner.nextLine();

        // Initialize the bank and auction proxy
        String bankHost;
        int portNumber;

        try {
            if (input.equals("1")) {
                agent = new Agent("localhost", 42069, null);
            } else if (input.equals("2")) {

                System.out.println("Enter bank host:");
                bankHost = inScanner.nextLine();

                System.out.println("Enter bank port:");
                portNumber = inScanner.nextInt();

                agent = new Agent(bankHost, portNumber, null);

            }
        }
        catch (NullPointerException e){
            return;
        }

        // Main loop goes until "exit"
        while (!input.equals("Exit")) {

            // Access bank or access action
            System.out.println("What would you like to do?");
            System.out.println("1. Access bank.");
            System.out.println("2. Access auction house.");
            System.out.println("Exit. to end program");

            input = inScanner.nextLine();

            // if bank, get balance
            if (input.equals("1")) {

                // Create the account
                if (agent.getAccountID() == 0) {

                    agent.addAccount();

                    System.out.println("Account ID=" + agent.getAccountID());
                    System.out.println("Account balance = "
                            + agent.getBalance());
                }

                // Access the bank
                while (!input.equals("0")) {

                    System.out.println("1. Add to funds.");
                    System.out.println("0. Exit bank");
                    input = inScanner.nextLine();

                    if (input.equals("1")) {

                        System.out.println("Enter amount to deposit:");
                        input = inScanner.nextLine();

                        agent.addFunds(Double.parseDouble(input));

                        System.out.println("New Balance: " + agent.getBalance());
                    }

                }

            }

            // If auction, make bid and view the available items
            if (input.equals("2")) {

                // Get the items
                ArrayList<ItemInfo> auctionItems = agent.getItems();

                //
                while (!auctionItems.isEmpty()) {
                    System.out.println("Items for auction: ");
                    for (ItemInfo info : auctionItems) {
                        System.out.println(info);
                    }

                    System.out.println("Please enter a number to make bid\n" +
                            "Or type \"Menu\" to exit to the menu");


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
