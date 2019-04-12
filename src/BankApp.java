import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BankApp extends Application implements BankProcess {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(introScene());
        primaryStage.show();
    }

    private Scene introScene() {


        GridPane introRoot = new GridPane();

        TextField portNum = new TextField();

        Button enterButton = new Button("Enter");
        enterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handleMakeBank());


        //configure root and add to it
        introRoot.setAlignment(Pos.CENTER);

        introRoot.add(new Text("Bank"), 1,1);

        introRoot.add(new Text("portNumber"), 1, 3);
        introRoot.add(portNum, 2, 3);

        return new Scene(introRoot, 500, 500);
    }

    private EventHandler<MouseEvent> handleMakeBank() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        };
    }

    /**
     * Get the balance of the Account number
     *
     * @param AccountID Unique Identifier of Account
     * @return Amount of money
     */
    @Override
    public double getBalance(int AccountID) {
        return 0;
    }

    /**
     * Add the funds to the specified account number
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public void addFunds(int AccountID, double amount) {

    }

    /**
     * Removes the funds from the given account
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     */
    @Override
    public void removeFunds(int AccountID, double amount) {

    }

    /**
     * Locks a certain amount of money away for potential use, returns an
     * integer value that can be used for later use.
     *
     * @param AccountID Unique Identifier of Account
     * @param amount    Amount of Money
     * @return Random/Unique Integer of lock, for later retrieval
     */
    @Override
    public int lockFunds(int AccountID, double amount) {
        return 0;
    }

    /**
     * Unlocks the lock given by the identifier!
     *
     * @param AccountID Unique Identifier of Account
     * @param lockID    Identifier of the lock
     */
    @Override
    public void unlockFunds(int AccountID, int lockID) {

    }

    /**
     * Transfer funds of amount specified from ID1 to ID2
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param amount Amount of Money
     */
    @Override
    public void transferFunds(int fromID, int toID, double amount) {

    }

    /**
     * Transfer funds based on the lock within the account tied to the fromID
     *
     * @param fromID Unique Identifier of Account1
     * @param toID   Unique Identifier of Account2
     * @param lockID Lock identifier
     */
    @Override
    public void transferFunds(int fromID, int toID, int lockID) {

    }
}
