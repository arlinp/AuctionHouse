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



public class AuctionHouseApp extends Application {

    AuctionHouse auctionHouse;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setScene(introScene());
        primaryStage.show();
    }

    private Scene introScene() {

        GridPane introRoot = new GridPane();


        TextField bankHostName = new TextField();
        TextField bankPortNum = new TextField();

        Button enterButton = new Button("Enter");
        enterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handleMakeAuctionHouse());


        //configure root and add to it
        introRoot.setAlignment(Pos.CENTER);

        introRoot.add(new Text("Auction House"), 1,1);

        introRoot.add(new Text("bank host name:"), 1, 2);
        introRoot.add(bankHostName, 2, 2);

        introRoot.add(new Text("bank portNumber"), 1, 3);
        introRoot.add(bankPortNum, 2, 3);

        return new Scene(introRoot, 500, 500);
    }

    private EventHandler<MouseEvent> handleMakeAuctionHouse() {
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {

            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
