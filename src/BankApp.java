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

public class BankApp extends Application {
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
}
