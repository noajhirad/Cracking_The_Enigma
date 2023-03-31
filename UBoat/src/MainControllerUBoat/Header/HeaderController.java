package MainControllerUBoat.Header;

import MainControllerUBoat.MainController;
import MainControllerUBoat.SkinsOptions;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class HeaderController {

    private MainController mainController;
    @FXML private Label enigmaMachineLable;
    @FXML private Button loadFileBtn;
    @FXML private TextField XMLFileTextField;
    @FXML private ToggleButton machineBtn;
    @FXML private ToggleGroup MenuBarBtn;
    @FXML private ToggleButton contestBtn;
    @FXML private Label feedbackLabel;

    @FXML private Menu skinsMenu;
    @FXML private MenuItem defaultMenuItem;
    @FXML private MenuItem peachMenuItem;
    @FXML private MenuItem cookieMenuItem;
    @FXML private MenuItem lionMenuItem;
    @FXML private Button OKBtn;


    @FXML
    public void initialize(){
        XMLFileTextField.setId("XMLFileTextField");
        XMLFileTextField.setPromptText("Please upload XML file.");
        OKBtn.setVisible(false);
    }

    @FXML
    void cookieMenuItemClicked(ActionEvent event) {
        mainController.setSkin(SkinsOptions.COOKIE_MONSTER);
    }

    @FXML
    void defaultMenuItemClicked(ActionEvent event) { mainController.setSkin(SkinsOptions.DEFAULT); }

    @FXML
    void lionMenuItemClicked(ActionEvent event) { mainController.setSkin(SkinsOptions.LION_KING); }

    @FXML
    void peachMenuItemClicked(ActionEvent event) { mainController.setSkin(SkinsOptions.PEACH);}

    public void bindXmlPathToTextField(SimpleStringProperty path){
        XMLFileTextField.textProperty().bind(path);
    }

    public void bindScreensToButtons() {
        machineBtn.selectedProperty().addListener(e -> {
            mainController.showFirstPage();
        });

        contestBtn.selectedProperty().addListener(e -> {
            mainController.showSecondPage();
        });
    }

    public void bindToIsFXMLLoaded(){
        SimpleBooleanProperty isFileLoaded = mainController.getIsFileLoadedProperty();
        machineBtn.disableProperty().bind(isFileLoaded.not());
        contestBtn.disableProperty().bind(isFileLoaded.not());
    }

    public void bindToIsConfigurated(){
        SimpleBooleanProperty isConfigurated = mainController.getIsConfiguratedProperty();
        contestBtn.disableProperty().bind(isConfigurated.not());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void loadFileButtonClicked(ActionEvent event) {
        try {
            mainController.loadXML();
        }
        catch(Exception e){
            setErrorMessage(e.getMessage());
        }
    }

    public void setErrorMessage(String message){
        feedbackLabel.setText(message);
//        System.out.println(message);
        feedbackLabel.setStyle("-fx-background-color: #fc6060; -fx-font-size: 14px; -fx-text-fill: white");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
        pauseTransition.setOnFinished(e -> feedbackLabel.setText(""));
        pauseTransition.play();
    }

    public void setSuccessMessage(String message){
        feedbackLabel.setText(message);
        feedbackLabel.setStyle("-fx-background-color: #D3EBCD; -fx-font-size: 14px; -fx-text-fill: black");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
        pauseTransition.setOnFinished(e -> feedbackLabel.setText(""));
        pauseTransition.play();
    }

    public void bindComponents(SimpleStringProperty path) {
        bindToIsFXMLLoaded();
        bindToIsConfigurated();
        bindScreensToButtons();
        bindXmlPathToTextField(path);
    }

    public void setMachineBtnSelected() {
        machineBtn.setSelected(true);
    }

    public void disableLoadBtn(){
        loadFileBtn.setDisable(true);
    }

    public void setWinningTeam(String winningTeam) {
        feedbackLabel.setText("Contest is over! The winning team: " + winningTeam);
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
        OKBtn.setVisible(true);
    }

    public void setContestStarted() {
        feedbackLabel.setText("Contest Started!");
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
    }

    @FXML
    void OKBtnClicked(ActionEvent event) {
        feedbackLabel.setText("");
        OKBtn.setVisible(false);
        mainController.clearContestData();
    }
}
