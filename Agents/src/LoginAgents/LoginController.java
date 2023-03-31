package LoginAgents;

import Exceptions.LoginException;
import MainControllerAgents.MainController;
import UtilsAgent.Constants;
import UtilsAgent.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

import static UtilsAgent.Constants.DELAY_RATE;
import static UtilsAgent.Constants.REFRESH_RATE;

public class LoginController {

    @FXML private Slider threadNumberSlider;
    @FXML private ComboBox<String> allieTeamComboBox;
    @FXML private TextField taskSizeTextField;
    @FXML private TextField usernameTextField;
    @FXML private Label feedbackLabel;
    @FXML private Button loginBtn;

    private SimpleStringProperty taskSizeProperty = new SimpleStringProperty();
    private SimpleIntegerProperty threadsNumberProperty = new SimpleIntegerProperty();
    private SimpleStringProperty alliesTeamProperty = new SimpleStringProperty();
    private ObservableList<String> alliesNames = FXCollections.observableArrayList();

    private MainController mainController;
    private Stage loginStage;
    private AlliesOptionsRefresher alliesOptionsRefresher;
    private Timer timer;

    @FXML
    public void initialize(){
        taskSizeProperty.bind(taskSizeTextField.textProperty());
        threadsNumberProperty.bind(threadNumberSlider.valueProperty());
        alliesTeamProperty.bind(allieTeamComboBox.valueProperty());
        initializeSlider();
        activateAlliesRefresher();
        allieTeamComboBox.setItems(alliesNames);
        feedbackLabel.setWrapText(true);
    }

    private void activateAlliesRefresher() {
        alliesOptionsRefresher = new AlliesOptionsRefresher(
                this::buildAllieTeamComboBox,
                this::setErrorMessage);
        timer = new Timer();
        timer.schedule(alliesOptionsRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void buildAllieTeamComboBox(List<String> allAllies) {
        Platform.runLater(()->
                this.alliesNames.setAll(allAllies));
    }

    @FXML
    public void loginBtnClicked(ActionEvent event) {

        String userName = usernameTextField.getText().toLowerCase();
        if (userName.isEmpty()) {
            setErrorMessage("Your username is empty. Please insert a unique username.");
            return;
        }

        try {
            validateChoices();
            mainController.setThreadsNumber(threadsNumberProperty.getValue());
            mainController.setTaskSize(taskSizeProperty.getValue());
            mainController.setAllie(alliesTeamProperty.getValue());
        }
        catch (Exception e){
            setErrorMessage(e.getMessage());
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("role", Constants.ROLE)
                .addQueryParameter("taskSize", taskSizeProperty.getValue())
                .addQueryParameter("threadsNumber", threadsNumberProperty.getValue().toString())
                .addQueryParameter("allieTeam",alliesTeamProperty.getValue())
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    Platform.runLater(() -> {
                        mainController.updateUserName(userName);
                        loginStage.close();
                        mainController.showPrimaryStage();
                        alliesOptionsRefresher.cancel();
                    });
                }
            }
        });
    }

    private void validateChoices() throws Exception{

        try {
            int taskSize = Integer.parseInt(taskSizeProperty.getValue());
            if (taskSize < 1)
                throw new LoginException(LoginException.ErrorType.INVALID_TASK_SIZE);
        }
        catch (Exception e){
            throw new LoginException(LoginException.ErrorType.INVALID_TASK_SIZE);
        }
        if (alliesTeamProperty.getValue() == null || alliesTeamProperty.getValue().equals(""))
            throw new LoginException(LoginException.ErrorType.EMPTY_ALLIE_TEAM);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setErrorMessage(String message){
        feedbackLabel.setText(message);
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

    private void initializeSlider(){
        threadNumberSlider.setMin(1);
        threadNumberSlider.setMax(4);
        threadNumberSlider.setBlockIncrement(1);
        threadNumberSlider.setMajorTickUnit(1);
        threadNumberSlider.setMinorTickCount(0);
        threadNumberSlider.setShowTickLabels(true);
        threadNumberSlider.setShowTickMarks(true);
        threadNumberSlider.setSnapToTicks(true);
    }
}