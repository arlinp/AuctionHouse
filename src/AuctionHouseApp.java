import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class AuctionHouseApp extends Application {

    AuctionHouse auctionHouse;

    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        primaryStage.setScene(introScene());
        primaryStage.show();
    }

    private Scene introScene() {

        GridPane introRoot = new GridPane();


        TextField bankHostName = new TextField();
        TextField bankPortNum = new TextField();

        Button enterButton = new Button("Enter");
//        enterButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handleMakeAuctionHouse());

        enterButton.setOnAction(e -> {

            ClientProxy clientProxy = null;
            ServerProxy bankProxy = new ServerProxy();

            auctionHouse = new AuctionHouse();

            window.setScene(auctionHouseScene());
        });

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

    private Scene auctionHouseScene() {
        GridPane sceneRoot = new GridPane();



        ScrollPane itemScrollPane = new ScrollPane();
        ListView<String> itemlistView = new ListView<>();
        ObservableList<String> names = FXCollections.observableArrayList(getitemsList(new File("items.txt")));

        itemScrollPane.setContent();

        return new Scene(sceneRoot, 500, 500);
    }

    private ArrayList<String> getitemsList(File file) {

        ArrayList<String> list = new ArrayList<>();

        Bu
    }

    public static void main(String[] args) {
        launch(args);
    }
}
