package main;

import Logic.Logic;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML private Button loadXmlBtn;
    @FXML private Button showStoreBtn;
    @FXML private Button showItemsBtn;
    @FXML private Button showCustomerBtn;
    @FXML private Button makeOrderBtn;
    @FXML private Button updateInfoBtn;
    @FXML private Button showMapBtn;

    private SimpleBooleanProperty isFileSelected;

    private Logic  logic;
    private Stage primaryStage;

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MainController() {
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    void loadXmlOnAction(ActionEvent event) {

    }

    @FXML
    void makeOrderOnAction(ActionEvent event) {

    }

    @FXML
    void showCustomerOnAction(ActionEvent event) {

    }

    @FXML
    void showItemsOnAction(ActionEvent event) {

    }

    @FXML
    void showMapOnAction(ActionEvent event) {

    }

    @FXML
    void showStoreOnAction(ActionEvent event) {

    }

    @FXML
    void updateInfoOnAction(ActionEvent event) {

    }


    @FXML
    private void initialize() {
        showCustomerBtn.disableProperty().bind(isFileSelected.not());
        showItemsBtn.disableProperty().bind(isFileSelected.not());
        showMapBtn.disableProperty().bind(isFileSelected.not());
        showStoreBtn.disableProperty().bind(isFileSelected.not());
        makeOrderBtn.disableProperty().bind(isFileSelected.not());
        updateInfoBtn.disableProperty().bind(isFileSelected.not());
    }

}
