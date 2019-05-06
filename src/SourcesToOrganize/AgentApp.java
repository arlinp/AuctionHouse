package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

public class AgentApp extends Application{

    private static final double APP_WIDTH = 500;
    private static final double APP_HEIGHT = 500;
    public static int bankPort = 42070;
    public static int auctionPort = 42069;
    Stage window;
    public Agent agent;

    Text selectedItemText = new Text();
    Text notification = new Text();
    LinkedList<Bid> bids = new LinkedList<>();
    private VBox bidVBox = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception {


        window = primaryStage;
        window.setTitle("Distributed Auctions - Agent");
        window.setMinWidth(700);

        primaryStage.setScene( new Scene(introRoot(), APP_WIDTH, APP_HEIGHT));
        // default value = n
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

        //connect to bank host on port 42069
        Button labTestButton = new Button("Lab Test");
        labTestButton.setOnAction(e -> {

            agent = new Agent(bankHostInput.getText(), 42069, this);
        });

        //test on this computer
        Button localTestButton = new Button("localhost Test");
        localTestButton.setOnAction(e -> {

            agent = new Agent("localhost", bankPort, this);

            window.setScene( new Scene(bankIntroRoot(), APP_WIDTH, APP_HEIGHT));
        });


        //add to intro root
        root.setAlignment(Pos.CENTER);

        root.add(title,1,1);

        root.add(new Label("Bank Host"), 0, 4);
        root.add(bankHostInput, 1,4);

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

        Button bankLoginButton = new Button("make new Account");
        bankLoginButton.setOnAction(e -> {

            agent.addAccount();

        });


        //add funds input
        TextField addFundsInput = new TextField("1000");

        //add funds to account
        Button addFundsButton = new Button("addFunds");
        addFundsButton.setOnAction(e -> {

            double fundsToAdd = Double.parseDouble(addFundsInput.getText());
            agent.addFunds(fundsToAdd);

            notification.setText("added " + fundsToAdd + " dollars");
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


        BorderPane root = new BorderPane();

        //select auction houses
        root.setLeft(bankAccountRoot());

        //select items
        root.setCenter(itemSelectionRoot());

        //manage bids

        ScrollPane bidScrollPane = new ScrollPane();

        bidVBox = new VBox();

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


        ArrayList<ItemInfo> itemInfos = agent.getItems();

        //table view
        ScrollPane scrollTable = new ScrollPane();

        TableView<ItemInfo> tableView = new TableView<>();
        scrollTable.setContent(tableView);

        //add columns to table
        //name column
        TableColumn<ItemInfo, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().add(itemCol);

        //price columns
        TableColumn<ItemInfo, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.getColumns().add(priceCol);

//        //description column
//        TableColumn<ItemInfo, String> descCol = new TableColumn<>("Description");
//        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
//        tableView.getColumns().add(descCol);

        for (ItemInfo info : itemInfos){
            if(!tableView.getItems().contains(info)){
                tableView.getItems().add(info);
            }
        }

        
        //select Item
        tableView.setOnMouseClicked(e -> {
            ItemInfo selectedItem = tableView.getSelectionModel().getSelectedItem();
            selectedItemText.setText(selectedItem.getName());

        });


        //enter the amount to bid with
        Label amountLabel = new Label("Bid Amount");
        TextField amountInput = new TextField();

        //bid on Item
        Button bidButton = new Button("Bid");
        bidButton.setOnAction(e -> {
            //get selected item
            ItemInfo selectedItemInfo = tableView.getSelectionModel().getSelectedItem();

            //get amount to start bid
            Double amount = Double.parseDouble(amountInput.getText());

            //create bid to send to house
            Bid bid = new Bid(amount, agent.getAccountID(), selectedItemInfo.getItemID());

            //display new bid to be updated with the reference to that Infos AHP
            Group bidGroup = displayNewBid(bid, selectedItemInfo.getProxy());
//            bidVBox.getChildren().add(bidGroup);

            agent.bid(selectedItemInfo.getProxy(), bid);

        });

        System.out.println("Where");
        auctionItemRoot.add(scrollTable, 1, 1);
        auctionItemRoot.add(selectedItemText, 1,3);

        auctionItemRoot.add(amountLabel, 0, 4);
        auctionItemRoot.add(amountInput, 1, 4);
        auctionItemRoot.add(bidButton,   1, 5);

        return auctionItemRoot;
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

        GridPane root = new GridPane();

        /*
        acc# : ###
        bal  : $$$
         */


        Label accountLabel = new Label("Account# : ");
        Text accountNum = new Text(String.valueOf(agent.getAccountID()));


        Label balanceLabel = new Label("Balance : ");
        Text accountBal = new Text(String.valueOf(agent.getBalance()));


        //add funds input
        TextField addFundsInput = new TextField("1000");

        Button addFundsButton = new Button("addFunds");
        addFundsButton.setOnAction(e -> {

            double fundsToAdd = Double.parseDouble(addFundsInput.getText());
            agent.addFunds(fundsToAdd);

            notification.setText("added " + fundsToAdd + " dollars");

            accountBal.setText(String.valueOf(agent.getBalance()));

        });

        root.add(accountLabel, 0,0);
        root.add(accountNum, 1, 0);

        root.add(balanceLabel, 0, 1);
        root.add(accountBal, 1,1);

        root.add(addFundsInput, 1, 3);
        root.add(addFundsButton, 2, 3);

        return root;

    }



    public static void main(String[] args) {

        launch(args);

    }

    public void addAuctionHouse(NetworkDevice networkDevice) {

        System.out.println("I am theoretically going to add an auctionhouse for " + networkDevice);
    }

    public void newPopUp(String s) {
        Platform.runLater(() -> {
            PopupControl popup = new PopupControl();
            Text t = new Text(s);
            VBox v = new VBox(t);
            popup.getScene().setRoot(v);
            popup.show(v.getScene().getWindow());
        });

    }
}
