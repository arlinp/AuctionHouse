package SourcesToOrganize;

import Agent.Agent;
import AuctionHouse.ItemInfo;
import AuctionProxy.AuctionProxy;
import BankProxy.BankProxy;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AgentApp extends Application{


    Stage window;
    public Agent agent;


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        primaryStage.setScene(introScene());

        primaryStage.show();
    }

    /**
     * User enters the host of the bank they want to connect to
     * @return Scene for GUI
     */
    private Scene introScene() {

        GridPane root = new GridPane();


        Text title = new Text("Agent");
        title.setFont(Font.font(50));


        //test on this computer
        Button localTest = new Button("localhost Test");
        localTest.setOnAction(e -> {

            localTest();

            window.setScene(bankAccountScene());
        });


        //new account taking input text
        TextField bankHostInput = new TextField();
        TextField auctionHostInput = new TextField();

        Button labTest = new Button("Lab Test");
        labTest.setOnAction(e -> {

            agent = new Agent(
                    new BankProxy(bankHostInput.getText(), 42069,this),
                    new AuctionProxy(auctionHostInput.getText(), 42070));
        });


        root.setAlignment(Pos.CENTER);

        root.add(title,1,1);

        root.add(new Label("Bank Host"), 0, 4);
        root.add(bankHostInput, 1,4);

        root.add(new Label("AuctionHost"), 0, 5);
        root.add(auctionHostInput, 1, 5);
        root.add(labTest, 1, 6);

        root.add(localTest, 1,9);

        return new Scene(root, 500, 500);

    }

    /**
     * Asks User to set up bank account, enter bank account number
     * @return
     */
    private Scene bankAccountScene() {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        Text notification = new Text();

        //new Bank Account
        TextField bankAccountInput = new TextField("5");

        Button bankLogin = new Button("make new Account");
        bankLogin.setOnAction(e -> {

            agent.addAccount(Integer.parseInt(bankAccountInput.getText()));

            notification.setText("account " + bankAccountInput.getText() + " made");

        });

        //add funds input
        TextField addFundsInput = new TextField("1000");

        Button addFundsButton = new Button("addFunds");
        addFundsButton.setOnAction(e -> {
            double fundsToAdd = Double.parseDouble(addFundsInput.getText());
            agent.addFunds(fundsToAdd);

            notification.setText("added " + fundsToAdd + " dollars");
        });

        Button auctionButton = new Button("Start Auction");
        auctionButton.setOnAction(e-> {
            window.setScene(auctionScene());
        });


        root.add(bankAccountInput,1,1);
        root.add(bankLogin, 2,1);

        root.add(addFundsInput, 1, 3);
        root.add(addFundsButton, 2, 3);

        root.add(auctionButton, 1, 7);

        root.add(notification,1,9);

        return new Scene(root, 500, 500);

    }

    /**
     * shows user the items up for auction, allows user to bid on items
     * @return
     */
    private Scene auctionScene() {

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);

        GridPane auctionItemRoot = new GridPane();


        splitPane.getItems().add(auctionItemRoot);

        splitPane.getItems().add(bankAccountRoot());


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

        //description column
        TableColumn<ItemInfo, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        tableView.getColumns().add(descCol);

        for (ItemInfo info : itemInfos){
            tableView.getItems().add(info);
        }

        Text selectedItemText = new Text();

        tableView.setOnMouseClicked(e -> {
//            ItemInfo selecteditem = tableView.getSelectionModel().getSelectedItem();

            selectedItemText.setText(tableView
                    .getSelectionModel()
                    .getSelectedItem()
                    .getName());
        });

        Button bidButton = new Button("Bid");

        Label amountLabel = new Label("Bid Amount");
        TextField amountInput = new TextField();

        bidButton.setOnAction(e -> {
            ItemInfo selectedItem = tableView.getSelectionModel().getSelectedItem();

            Double amount = Double.parseDouble(amountInput.getText());

            Bid bid = new Bid(amount, agent.getAccountID(), selectedItem.getItemID());

            agent.bid(bid);
        });


        /*
                table

                text

        label, input
               button

         */
        auctionItemRoot.add(scrollTable, 1, 1);
        auctionItemRoot.add(selectedItemText, 1,3);

        auctionItemRoot.add(amountLabel, 0, 4);
        auctionItemRoot.add(amountInput, 1, 4);
        auctionItemRoot.add(bidButton,   1, 5);


        return new Scene(splitPane,500, 500);
    }


    private Node bankAccountRoot(){

        GridPane gridPane = new GridPane();

        /*
        acc# : ###
        bal  : $$$
         */


        Label accountLabel = new Label("Account# : ");
        Text accountNum = new Text(String.valueOf(agent.getAccountID()));


        Label balanceLabel = new Label("Balance : ");
        Text accountBal = new Text(String.valueOf(agent.getBalance()));

        gridPane.add(accountLabel, 0,0);
        gridPane.add(accountNum, 1, 0);

        gridPane.add(balanceLabel, 0, 1);
        gridPane.add(accountBal, 1,1);

        return gridPane;

    }

    private boolean localTest() {

        BankProxy bankProxy = new BankProxy("localHost", 42069, this);

        AuctionProxy auctionProxy = new AuctionProxy("localhost", 42070);

        agent = new Agent(bankProxy, auctionProxy);

        return true;

    }




    public static void main(String[] args) {

        launch(args);

    }

    public void addAuctionHouse(NetworkDevice networkDevice) {

        System.out.println("I am theoretically going to add an auctionhouse for " + networkDevice);
    }
}
