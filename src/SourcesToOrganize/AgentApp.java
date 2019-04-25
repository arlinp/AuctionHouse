package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentApp extends Application{


    Stage window;
    public Agent agent;

    public AgentApp() {

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

            window.setScene(bankAccountScene());
        });


        //new account taking input text
        TextField bankHostInput = new TextField("Bank Host");
        TextField auctionHostInput = new TextField("AuctionHost");

        Button labTest = new Button("Lab Test");
        labTest.setOnAction(e -> {

            agent = new Agent(
                    new BankProxy(bankHostInput.getText(), 42069,this),
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

    private Scene bankAccountScene() {

        GridPane root = new GridPane();

        Text notification = new Text();

        TextField bankAccountInput = new TextField();

        Button bankLogin = new Button("make new Account");
        bankLogin.setOnAction(e -> {

            agent.addAccount(Integer.parseInt(bankAccountInput.getText()));

            notification.setText("account " + bankAccountInput.getText() + " made");

            window.setScene(auctionScene());
        });


        root.add(bankAccountInput,1,1);
        root.add(bankLogin, 1,2);
        root.add(notification,1,3);

        return new Scene(root, 500, 500);

    }

    private Scene auctionScene() {

        GridPane root = new GridPane();

        ScrollPane scrollPane = new ScrollPane();

        TableView<String> tableView = new TableView<>();
        scrollPane.setContent(tableView);

        //add column to table
        TableColumn<String, ItemInfo> itemCol = new TableColumn<>("Item");
        tableView.getColumns().add(itemCol);


        ArrayList<ItemInfo> itemInfos = agent.getItems();

        for (ItemInfo info : itemInfos){

            tableView.getItems().add(info.getName());

        }

        root.add(scrollPane, 1,1);

        return new Scene(root,500, 500);
    }

    private void localTest() {


        agent = new Agent(
                new BankProxy("localHost", 42069, this),
                new AuctionProxy("localhost", 42070));
    }




    public static void main(String[] args) {

        launch(args);
//        AgentApp app = new AgentApp();

    }

    public void addAuctionHouse(String ipAddress, int port) {

        System.out.println("I am theoretically going to add an auctionhouse for " + ipAddress + ":" + port);
    }
}
