package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Agent app for the user to connect to the servers
 */
public class AgentApp extends Application{

    private static final double APP_WIDTH = 800;
    private static final double APP_HEIGHT = 650;
    public static int bankPort = 42070;
    public static int auctionPort = 42069;
    Stage window;
    public Agent agent;

    Text selectedItemText = new Text();
    Text notification = new Text();
    ItemInfo selectedItem;
    TableView<ItemInfo> tableView;
    Text accountBal;
    Text totalBal;
    private boolean atAuction = false;

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("Distributed Auctions - Agent");
        window.setMinWidth(700);

        primaryStage.setScene( new Scene(introRoot(), APP_WIDTH, APP_HEIGHT));
        primaryStage.setOnCloseRequest(t -> {
            t.consume();
            if (!atAuction || agent.closeRequest()) {
                primaryStage.close();
                Platform.exit();
                System.exit(0);
            } else {
                newPopUp("You have an item being bid upon! NO LEAVING!");
            }

        });

        primaryStage.show();
    }

    /**
     * Asks users to connect to the bank.
     * Then transfers them to bank account scene
     * @return Scene for GUI
     */
    private Parent introRoot() {

        GridPane root = new GridPane();

        //title
        Text title = new Text("Agent");
        title.setFont(Font.font(50));

        //new account taking input text
        TextField bankHostInput = new TextField();

        //new account taking input text
        TextField bankPortInput = new TextField();

        //connect to bank host on port 42069
        Button labTestButton = new Button("Lab Test");
        labTestButton.setOnAction(e -> {
            int port;
            try {
                port = Integer.parseInt(bankPortInput.getText());
            } catch (NumberFormatException error) {
                return;
            }
            agent = new Agent(bankHostInput.getText(), port, this);
        });

        //test on this computer
        Button localTestButton = new Button("LocalHost Test");
        localTestButton.setOnAction(e -> {
            agent = new Agent("localhost", bankPort, this);
            window.setScene( new Scene(bankIntroRoot(), APP_WIDTH, APP_HEIGHT));
        });


        //add to intro root
        root.setAlignment(Pos.CENTER);

        root.add(title,1,1);

        root.add(new Label("Bank Host  "), 0, 4);
        root.add(bankHostInput, 1,4);
        root.add(new Label("Bank Port  "), 0, 5);
        root.add(bankPortInput, 1,5);

        root.add(labTestButton, 1, 6);

        root.add(localTestButton, 1,9);

        return root;

    }

    /**
     * Asks User to set up bank account
     * enter bank account number
     * add funds
     *
     * enter auction
     * @return
     */
    private Parent bankIntroRoot() {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        Button bankLoginButton = new Button("Make new Account");
        bankLoginButton.setOnAction(e -> {
            agent.addAccount();
            notification.setText("Created an account: ID# " + agent.getAccountID());
        });


        //add funds input
        TextField addFundsInput = new TextField("10000");

        //add funds to account
        Button addFundsButton = new Button("Deposit Funds");
        addFundsButton.setOnAction(e -> {

            double fundsToAdd = Double.parseDouble(addFundsInput.getText());
            agent.addFunds(fundsToAdd);

            notification.setText("Added " + fundsToAdd + " dollars");
        });

        //gets auction from bank to start bidding
        Button auctionButton = new Button("Start Auction");
        auctionButton.setOnAction(e-> {

            agent.connectToAuctions();

            window.setScene( new Scene(auctionRoot(), APP_WIDTH, APP_HEIGHT));
        });


        //add components to gui root
        root.add(bankLoginButton, 2,1);

        root.add(addFundsInput, 1, 3);
        root.add(addFundsButton, 2, 3);

        root.add(auctionButton, 1, 7);

        root.add(notification,1,9);

        return root;
    }

    /**
     * shows user the items up for auction, allows user to bid on items
     * @return
     */
    private Parent auctionRoot() {
        atAuction = true;

        BorderPane root = new BorderPane();

        //select auction houses
        root.setLeft(bankAccountRoot());

        //select items
        root.setCenter(itemSelectionRoot());

        //manage bids

        ScrollPane bidScrollPane = new ScrollPane();

        bidScrollPane.setContent(bidScrollPane);


        return root;
    }

    /**
     * displays all items from all connected auction and allows
     * user to bid on an item
     * @return
     */
    private GridPane itemSelectionRoot() {

        GridPane auctionItemRoot = new GridPane();
        auctionItemRoot.setMaxWidth(400);
        ArrayList<ItemInfo> itemInfos = agent.getItems();

        //table view
        ScrollPane scrollTable = new ScrollPane();

        tableView = new TableView<>();
        scrollTable.setContent(tableView);
        scrollTable.setMaxWidth(250);
        scrollTable.setMinWidth(250);

        //add columns to table
        //name column
        TableColumn<ItemInfo, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemCol.setMinWidth(120);
        tableView.getColumns().add(itemCol);

        //price columns
        TableColumn<ItemInfo, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(60);
        tableView.getColumns().add(priceCol);


        for (ItemInfo info : itemInfos){
            if(!tableView.getItems().contains(info)){
                tableView.getItems().add(info);
            }
        }

        
        //select Item
        tableView.setOnMouseClicked(e -> {
            selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItemText != null && selectedItem != null) {
                selectedItemText.setText(selectedItem.getName());
            }
        });


        //enter the amount to bid with
        Label amountLabel = new Label("Bid Amount");
        TextField amountInput = new TextField();

        //bid on Item
        Button bidButton = new Button("Bid");
        bidButton.setOnAction(e -> {
            //get selected item
            ItemInfo selectedItemInfo = tableView.getSelectionModel().getSelectedItem();
            Double amount = 0.0;
            //get amount to start bid
            try {
                amount = Double.parseDouble(amountInput.getText());
            } catch (NumberFormatException error) {
                tableView.getSelectionModel().select(-1);
                selectedItem = null;
                return;
            }
            Bid bid;
            //create bid to send to house
            if (selectedItemInfo != null) {
                bid = new Bid(amount, agent.getAccountID(), selectedItemInfo.getItemID());
                //display new bid to be updated with the reference to that Infos AHP

                agent.bid(selectedItemInfo.getProxy(), bid);
                selectedItem = null;
                refreshTableView();
                refreshBalance();
            }

        });
        Button resetButton = new Button("Reset Selection");
        resetButton.setOnAction(e -> {
            tableView.getSelectionModel().select(-1);
            selectedItem = null;
            return;
        });


        HBox buttons = new HBox(bidButton, resetButton);
        System.out.println("Where");
        Text title = new Text("Items");
        title.setFont(Font.font(50));
        auctionItemRoot.add(title, 1, 0);
        auctionItemRoot.add(scrollTable, 1, 1);
        auctionItemRoot.add(selectedItemText, 1,3);
        auctionItemRoot.add(amountLabel, 0, 4);
        auctionItemRoot.add(amountInput, 1, 4);
        auctionItemRoot.add(buttons,   1, 5);
        auctionItemRoot.setHgap(5);
        auctionItemRoot.setVgap(5);

        Thread one = new Thread(() -> {
            while (true) {
                try{
                    Thread.sleep(2000);
                    refreshTableView();
                } catch(InterruptedException v) {
                    System.out.println(v);
                }
            }
        });
        one.start();


        return auctionItemRoot;
    }

    private void refreshTableView() {

        if (selectedItem == null) {
            System.out.println("Refreshing");
            ArrayList<ItemInfo> itemInfos = agent.getItems();

            System.out.println(itemInfos);

            tableView.getItems().clear();
            System.out.println(itemInfos);
            for (ItemInfo info : itemInfos){
                System.out.println(info);
                if (!tableView.getItems().contains(info)){
                    tableView.getItems().add(info);
                }
            }
        }
    }

    private Group displayNewBid(Bid bid, AuctionProxy proxy) {

        Thread groupUdater = new Thread();

        return null;
    }

    private Group makeBidGroup(Bid bid, ItemInfo info) {

        Group bidGroup = new Group();

        bidGroup.getChildren().add(new Text(info.getName()));

        bidGroup.getChildren().add(new Text("$" + info.getPrice()));

        return bidGroup;
    }


    private Node bankAccountRoot(){
        StackPane pane = new StackPane();
        GridPane root = new GridPane();

        /*
        acc# : ###
        bal  : $$$
         */


        Label accountLabel = new Label("Account# : ");
        Text accountNum = new Text(String.valueOf(agent.getAccountID()));


        Label balanceLabel = new Label("Available Balance : ");
        accountBal = new Text(String.valueOf(agent.getBalance()));

        Label totalLabel = new Label("Available Balance : ");
        totalBal = new Text(String.valueOf(agent.getTotalBalance()));


        //add funds input
        TextField addFundsInput = new TextField("100");

        Button addFundsButton = new Button("Add Funds");
        addFundsButton.setOnAction(e -> {
            double fundsToAdd;
            try {
                fundsToAdd = Double.parseDouble(addFundsInput.getText());
            } catch (NumberFormatException error) {
                notification.setText("Enter a number");
                return;
            }
            agent.addFunds(fundsToAdd);

            notification.setText("Added $" + fundsToAdd + " to Account# " + agent.getAccountID());

            refreshBalance();
        });

        Text title = new Text("Bank");
        title.setFont(Font.font(50));
        root.add(title,0,0);

        root.add(accountLabel, 0,1);
        root.add(accountNum, 1, 1);

        root.add(balanceLabel, 0, 2);
        root.add(accountBal, 1,2);

        root.add(totalLabel, 0, 3);
        root.add(totalBal, 1,3);

        root.add(addFundsInput, 1, 4);
        root.add(addFundsButton, 2, 4);
        root.setAlignment(Pos.CENTER);
        root.setVgap(10.0);
        root.setHgap(5.0);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(root);
        pane.setPadding(new Insets(50));
        return pane;

    }

    private void refreshBalance() {
        if (accountBal != null && totalBal != null) {
            accountBal.setText(String.valueOf(agent.getBalance()));
            totalBal.setText(String.valueOf(agent.getTotalBalance()));
        }
    }


    public static void main(String[] args) {

        launch(args);

    }

    public void addAuctionHouse(NetworkDevice networkDevice) {

        System.out.println("I am theoretically going to add an auctionhouse for " + networkDevice);
    }

    public void newPopUp(String s) {
        Platform.runLater(() -> {
            refreshBalance();
            Stage newStage = new Stage();
            StackPane comp = new StackPane();
            Text str = new Text(s);
            comp.getChildren().add(str);

            Scene stageScene = new Scene(comp);
            newStage.setScene(stageScene);
            newStage.show();
        });

    }
}
