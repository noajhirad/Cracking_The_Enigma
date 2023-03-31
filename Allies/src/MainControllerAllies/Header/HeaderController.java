package MainControllerAllies.Header;

import MainControllerAllies.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class HeaderController {

    @FXML private ToggleButton dashboardBtn;
    @FXML private ToggleGroup MenuBarBtn;
    @FXML private ToggleButton contestBtn;
    @FXML private Label feedbackLabel;
    @FXML private Menu skinsMenu;
    @FXML private MenuItem defaultMenuItem;
    @FXML private MenuItem peachMenuItem;
    @FXML private MenuItem cookieMenuItem;
    @FXML private MenuItem lionMenuItem;
    @FXML private HBox feedbackComponent;
    @FXML private Button OKBtn;

    private MainController mainController;

    @FXML
    public void initialize(){
        contestBtn.setDisable(true);
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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void bindScreensToButtons() {
        dashboardBtn.selectedProperty().addListener(e -> {
            mainController.showFirstPage();
        });

        contestBtn.selectedProperty().addListener(e -> {
            mainController.showSecondPage();
        });
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

    public void setWinningTeam(String winningTeam) {
        feedbackLabel.setText("Contest is over! The winning team: " + winningTeam);
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
        OKBtn.setVisible(true);
    }

    public void setContestMessages(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
    }

    public void setContestBtnSelected() {
        dashboardBtn.setSelected(false);
        contestBtn.setSelected(true);
    }

    public void setDashboardBtnSelected() {
        contestBtn.setSelected(false);
        dashboardBtn.setSelected(true);
    }

    public void setContestBtnEnabled() {
        contestBtn.setDisable(false);
    }
    public void setContestBtnDisabled() {
        contestBtn.setDisable(true);
    }


    @FXML
    void OKBtnClicked(ActionEvent event) {
        feedbackLabel.setText("");
        OKBtn.setVisible(false);
        mainController.clearContestData();
        mainController.showFirstPage();
        mainController.cancelContestRefreshers();
    }

    public void setDashboardBtnEnabled() {
        dashboardBtn.setDisable(false);
    }

    public void setDashboardBtnDisabled() {
        dashboardBtn.setDisable(true);
    }
}
