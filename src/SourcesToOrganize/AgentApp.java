package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.Item;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentApp extends Application {


    Stage window;
    public Agent agent;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        primaryStage.setScene(introScene());

        primaryStage.show();
    }

    private Scene introScene() {

//        BankProxy bankProxy = null;
//        AuctionProxy auctionProxy = null;

        GridPane root = new GridPane();


        Button localTest = new Button("localhost Test");
        localTest.setOnAction(e -> {

            localTest();

            window.setScene(agentScene());
        });


        TextField bankHostInput = new TextField("Bank Host");
        TextField auctionHostInput = new TextField("AuctionHost");

        Button labTest = new Button("Lab Test");
        labTest.setOnAction(e -> {

            agent = new Agent(
                    new BankProxy(bankHostInput.getText(), 42069),
                    new AuctionProxy(auctionHostInput.getText(), 42070));
        });



        root.setAlignment(Pos.CENTER);

        root.add(new Text("Agent"),1,1);
        root.add(localTest, 1,2);
        root.add(bankHostInput, 1,4);
        root.add(auctionHostInput, 1, 5);
        root.add(labTest, 1, 6);

        return new Scene(root, 500, 500);

    }

    private Scene agentScene() {

        GridPane root = new GridPane();

        Text notification = new Text();

        TextField bankAccountInput = new TextField();

        Button bankLogin = new Button("make new Account");
        bankLogin.setOnAction(e -> {

            agent.addAccount(Integer.parseInt(bankAccountInput.getText()));

            notification.setText("account " + bankAccountInput.getText() + " made");
        });


        root.add(bankAccountInput,1,1);
        root.add(bankLogin, 1,2);
        root.add(notification,1,3);

        return new Scene(root, 500, 500);

    }

    private void localTest() {


        agent = new Agent(
                new BankProxy("localHost", 42069),
                new AuctionProxy("localhost", 42070));
    }




    public static void main(String[] args) {

        launch(args);

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

                bankProxy = new BankProxy("localHost", 42069);
                auctionProxy = new AuctionProxy("localhost", 42070);

            }

            if (input.equals("2")) {

                System.out.println("enter bank host:");
                input = inScanner.nextLine();
                bankProxy = new BankProxy(input, 42069);

                System.out.println("enter auctionhouse host:");
                input = inScanner.nextLine();
                auctionProxy = new AuctionProxy(input, 42070);

            }

            if (bankProxy == null) throw new ConnectException();

            if (auctionProxy == null) throw new ConnectException();

        } catch (ConnectException e) {


        }

        //int accountID = bankProxy.addAccount();
        int accountID = 0;

//        this test is not going to work
//        if(test){
//
//            int account2ID = bankProxy.addAccount();
//
//            //Add money to accounts
//            bankProxy.addFunds(accountID, 500);
//            bankProxy.addFunds(account2ID, 500);
//
//            System.out.println("New Balance Account 1: " + bankProxy.getBalance(accountID));
//            System.out.println("New Balance Account 2: " + bankProxy.getBalance(account2ID));
//
//
//
//        }

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

                System.out.println("Your Account ID is: ");
                System.out.println(" or nothing...");
                input = inScanner.nextLine();

                if (input.equals("")) {
                    accountID = 5;
                } else {
                    accountID = Integer.parseInt(input);
                }


                System.out.println("Account ID=" + accountID);
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

//                System.out.println("making bid on: " + auctionItems.get(itemIndex) + " " +
//                        "for : " + amount);
//
//                auctionProxy.bid(new Bid(amount,accountID,auctionItems.get(itemIndex).getItemID()));

                auctionProxy.bid(new Bid(amount, accountID, itemIndex));
            }
        }

    }
}
