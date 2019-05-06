package Agent;

import AuctionHouse.ItemInfo;
import AuctionProxy.BidInfo;
import AuctionHouse.Bid;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Agent app for the user to connect to the servers
 */
public class AgentApp extends Application{

    // Static variables for default settings
    private static final double APP_WIDTH = 800;
    private static final double APP_HEIGHT = 650;
    public static int bankPort = 42070;
    public static int auctionPort = 42069;

    // Global GUI elements for selection
    private Stage window;
    public Agent agent;
    private Text selectedItemText = new Text();
    private Text notification = new Text();
    private ItemInfo selectedItem;
    private TableView<ItemInfo> tableView;
    private Text accountBal;
    private Text totalBal;

    // Global variable for main use
    private boolean atAuction = false;


    /**
     * Starts the GUI process
     *
     * @param primaryStage Stage of GUI
     */
    @Override
    public void start(Stage primaryStage) {

        // Set the stage
        window = primaryStage;
        window.setTitle("Distributed Auctions - Agent");
        window.setMinWidth(700);

        // Set the behavior of the close button to check with AuctionProxies
        primaryStage.setScene( new Scene(introRoot(), APP_WIDTH, APP_HEIGHT));
        primaryStage.setOnCloseRequest(t -> {
            t.consume();
            // If the GUI is not in an auction or cleared by closeRequest,
            // then the user can't leave
            if (!atAuction || agent.closeRequest()) {
                primaryStage.close();
                Platform.exit();
                System.exit(0);
            } else {
                // Notify of not being able to leave
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

        // Title
        Text title = new Text("Agent");
        title.setFont(Font.font(50));

        // New account taking input text
        TextField bankHostInput = new TextField();

        // New account taking input text
        TextField bankPortInput = new TextField();

        // Connect to bank host on port 42069
        Button labTestButton = new Button("Lab Test");
        labTestButton.setOnAction(e -> {
            int port;

            // Try to parse the integer
            try {
                port = Integer.parseInt(bankPortInput.getText());
            } catch (NumberFormatException error) {
                return;
            }

            // Create the agent with the given port
            agent = new Agent(bankHostInput.getText(), port, this);

            // Start the GUI
            window.setScene( new Scene(bankIntroRoot(), APP_WIDTH, APP_HEIGHT));

        });

        // Test on this computer
        Button localTestButton = new Button("LocalHost Test");
        localTestButton.setOnAction(e -> {
            agent = new Agent("localhost", bankPort, this);
            window.setScene( new Scene(bankIntroRoot(), APP_WIDTH, APP_HEIGHT));
        });


        // Add components together
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
     * @return Bank Intro scene
     */
    private Parent bankIntroRoot() {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        // Create the button and assign the action
        Button bankLoginButton = new Button("Make new Account");
        bankLoginButton.setOnAction(e -> {
            agent.addAccount();
            notification.setText("Created an account: ID# " +
                    agent.getAccountID());
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

        // Gets auction from bank to start bidding
        Button auctionButton = new Button("Start Auction");
        auctionButton.setOnAction(e-> {

            agent.connectToAuctions();

            window.setScene( new Scene(auctionRoot(), APP_WIDTH, APP_HEIGHT));
        });


        // Add components to gui root
        root.add(bankLoginButton, 2,1);
        root.add(addFundsInput, 1, 3);
        root.add(addFundsButton, 2, 3);
        root.add(auctionButton, 1, 7);
        root.add(notification,1,9);

        return root;
    }

    /**
     * Shows user the items up for auction, allows user to bid on items
     * @return Parent object
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
     * @return GridPane of selection pane
     */
    private GridPane itemSelectionRoot() {

        GridPane auctionItemRoot = new GridPane();
        auctionItemRoot.setMaxWidth(400);
        ArrayList<ItemInfo> itemInfos = agent.getItems();

        // Table View
        ScrollPane scrollTable = new ScrollPane();

        tableView = new TableView<>();
        scrollTable.setContent(tableView);
        scrollTable.setMaxWidth(250);
        scrollTable.setMinWidth(250);

        // Add columns to name column
        TableColumn<ItemInfo, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemCol.setMinWidth(120);
        tableView.getColumns().add(itemCol);

        // Price columns
        TableColumn<ItemInfo, String> priceCol =
                new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(60);
        tableView.getColumns().add(priceCol);


        // Put the items within the table
        for (ItemInfo info : itemInfos){
            if(!tableView.getItems().contains(info)){
                tableView.getItems().add(info);
            }
        }

        
        // Select Item
        tableView.setOnMouseClicked(e -> {
            selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItemText != null && selectedItem != null) {
                selectedItemText.setText(selectedItem.getName());
            }
        });


        // Enter the amount to bid with
        Label amountLabel = new Label("Bid Amount");
        TextField amountInput = new TextField();

        // Bid on Item
        Button bidButton = new Button("Bid");
        bidButton.setOnAction(e -> {
            // Get selected item
            ItemInfo selectedItemInfo = tableView.getSelectionModel().
                    getSelectedItem();
            Double amount;

            // Get amount to start bid
            try {
                amount = Double.parseDouble(amountInput.getText());
            } catch (NumberFormatException error) {
                tableView.getSelectionModel().select(-1);
                selectedItem = null;
                return;
            }

            Bid bid;

            // Create bid to send to house
            if (selectedItemInfo != null) {
                bid = new Bid(amount, agent.getAccountID(),
                        selectedItemInfo.getItemID());
                // Display new bid to be updated with the reference to
                // that Infos AuctionProxy

                BidInfo status = agent.bid(selectedItemInfo.getProxy(), bid);
                if (status == BidInfo.REJECTION) {
                    newPopUp("Your bid on the item was rejected!");
                }
                selectedItem = null;
                selectedItemText.setText("");
                refreshTableView();
                refreshBalance();
            }

        });

        // Create a reset button for the Agent
        Button resetButton = new Button("Reset Selection");
        resetButton.setOnAction(e -> {
            tableView.getSelectionModel().select(-1);
            selectedItem = null;
        });


        // Add all the rest of the items to the parent components
        HBox buttons = new HBox(bidButton, resetButton);
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

        // Create a refresh thread
        Thread one = new Thread(() -> {
            while (true) {
                try{
                    Thread.sleep(2000);
                    refreshTableView();
                } catch(InterruptedException v) {
                    System.out.println("Interrupted");
                    return;
                }
            }
        });
        one.start();


        return auctionItemRoot;
    }

    /**
     * Refresh the table's graphical elements
     */
    private void refreshTableView() {

        // Check for selected item
        if (selectedItem == null) {
            ArrayList<ItemInfo> itemInfos = agent.getItems();

            // Get the items and refresh them
            tableView.getItems().clear();
            for (ItemInfo info : itemInfos) {
                if (!tableView.getItems().contains(info)) {
                    tableView.getItems().add(info);
                }
            }
        }
    }


    /**
     * Create the Bank account on the bidding page
     *
     * @return Node of object
     */
    private Node bankAccountRoot(){
        // Create the appropriate housing units
        StackPane pane = new StackPane();
        GridPane root = new GridPane();

        // Create the labes and text fields
        Label accountLabel = new Label("Account# : ");
        Text accountNum = new Text(String.valueOf(agent.getAccountID()));

        Label balanceLabel = new Label("Available Balance : ");
        accountBal = new Text(String.valueOf(agent.getBalance()));

        Label totalLabel = new Label("Total Balance : ");
        totalBal = new Text(String.valueOf(agent.getTotalBalance()));


        // Add funds input
        TextField addFundsInput = new TextField("100");
        Button addFundsButton = new Button("Add Funds");

        // Set behavior of the input
        addFundsButton.setOnAction(e -> {
            double fundsToAdd;

            // Catch error
            try {
                fundsToAdd = Double.parseDouble(addFundsInput.getText());
            } catch (NumberFormatException error) {
                notification.setText("Enter a number");
                return;
            }

            // Refresh otherwise
            agent.addFunds(fundsToAdd);
            notification.setText("Added $" + fundsToAdd + " to Account# " +
                    agent.getAccountID());
            refreshBalance();
        });

        // Add the components to the parent component
        Text title = new Text("Bank");
        title.setFont(Font.font(50));
        root.add(title,0,0);

        // Add the various components into the Grid
        root.add(accountLabel, 0,1);
        root.add(accountNum, 1, 1);

        root.add(balanceLabel, 0, 2);
        root.add(accountBal, 1,2);

        root.add(totalLabel, 0, 3);
        root.add(totalBal, 1,3);

        root.add(addFundsInput, 1, 4);
        root.add(addFundsButton, 2, 4);

        // Modify GUI of parent elements
        root.setAlignment(Pos.CENTER);
        root.setVgap(10.0);
        root.setHgap(5.0);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(root);
        pane.setPadding(new Insets(50));

        // Return the StackPane
        return pane;

    }

    /**
     * Refresh the balance of the Bank account view
     */
    private void refreshBalance() {
        // Check for nullity
        if (accountBal != null && totalBal != null) {
            accountBal.setText(String.valueOf(agent.getBalance()));
            totalBal.setText(String.valueOf(agent.getTotalBalance()));
        }
    }

    /**
     * Run the program
     *
     * @param args Should be nothing
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create a new popup window
     *
     * @param s String to display
     */
    public void newPopUp(String s) {
        Platform.runLater(() -> {

            // Refresh the balance
            refreshBalance();

            // Create text field
            Text str = new Text(s);

            // Create stage for popup
            Stage newStage = new Stage();
            StackPane comp = new StackPane();
            Scene stageScene = new Scene(comp);

            // Modify graphics
            comp.setPadding(new Insets(10));
            str.setFont(Font.font(20));

            // Add components
            comp.getChildren().add(str);
            newStage.setScene(stageScene);
            newStage.show();

            // Refresh the balance
            refreshBalance();
        });
    }
}
