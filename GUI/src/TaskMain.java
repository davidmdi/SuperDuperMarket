import Logic.Logic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import main.MainController;

import java.net.URL;

public class TaskMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource("main/main.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        //wire up controller
        MainController mainController = loader.getController();
        //Logic logic = new Logic(mainController);
        mainController.setPrimaryStage(primaryStage);
        //mainController.setLogic(logic);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
