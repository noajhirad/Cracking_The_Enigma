package LoginAllies;

import MainControllerAllies.MainController;
import UtilsAllies.Constants;
import UtilsAllies.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.*;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameTextField;
    @FXML private Label feedbackLabel;
    @FXML private Button loginBtn;
    MainController mainController;
    Stage loginStage;

    @FXML
    public void initialize() {
        feedbackLabel.setWrapText(true);
    }

    @FXML
    public void loginBtnClicked(ActionEvent event) {

        String userName = usernameTextField.getText().toLowerCase();
        if (userName.isEmpty()) {
            setErrorMessage("Your username is empty. Please insert a unique username.");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("role", Constants.ROLE)
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
                    });
                }
            }
        });
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
}