package MainControllerAllies;
import ChatareaAllies.ChatAreaController;
import DTOs.DTOContestRow;
import FirstPageAllies.AgentsTable.AgentsTableController;
import FirstPageAllies.AgentsTable.AgentsTableRefresher;
import FirstPageAllies.ContestsTable.ContestTableRefresher;
import FirstPageAllies.ContestsTable.ContestsTableController;
import FirstPageAllies.FirstPageController;
import MainControllerAllies.Header.HeaderController;
import SecondPageAllies.SecondPageController;
import SecondPageAllies.TeamsData.TeamsController;
import UtilsAllies.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;

import static UtilsAllies.Constants.*;

public class MainController {
    public static final String HEADER_fXML_RESOURCE = "/MainControllerAllies/Header/header.fxml";
    public static final String FIRST_PAGE_FXML_RESOURCE = "/FirstPageAllies/firstCenter1.fxml";
    public static final String SECOND_PAGE_FXML_RESOURCE = "/SecondPageAllies/secondPageAllies.fxml";
    private final static String CHATROOM_fxml = "/ChatareaAllies/chat-area.fxml";

    @FXML private BorderPane bodyComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private FirstPageController firstPageComponentController;
    @FXML private SecondPageController secondPageComponentController;
    @FXML private ContestsTableController contestsTableComponentController;
    @FXML private AgentsTableController agentsTableComponentController;
    private TeamsController teamsComponentController;
    private ChatAreaController chatAreaController;


    @FXML private ScrollPane headerComponent;
    private Stage primaryStage;
    private GridPane firstPane;
    private GridPane secondPane;
    private GridPane chatComponent;


    private ContestTableRefresher contestTableRefresher;
    private AgentsTableRefresher agentsTableRefresher;

    private Timer timer;
    private SkinsOptions skin = SkinsOptions.DEFAULT;

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if (headerComponentController != null && firstPageComponentController != null &&
                secondPageComponentController != null) {

            headerComponentController.setMainController(this);
            firstPageComponentController.setMainController(this);
            firstPageComponentController.setChatController(chatAreaController);
            secondPageComponentController.setMainController(this);
            secondPageComponentController.setChatController(chatAreaController);

            contestsTableComponentController = firstPageComponentController.getContestTableController();
            agentsTableComponentController = firstPageComponentController.getAgentsTableController();
            teamsComponentController = secondPageComponentController.getTeamsController();
        }
    }

    private void loadResources() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        // load header controller & component from fxml
        URL url = getClass().getResource(HEADER_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        headerComponent = fxmlLoader.load(url.openStream());
        headerComponentController = fxmlLoader.getController();
        bodyComponent.setTop(headerComponent);

        // load first page component & controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(FIRST_PAGE_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        firstPane = fxmlLoader.load(url.openStream());
        firstPageComponentController = fxmlLoader.getController();

        // load second page component & controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(SECOND_PAGE_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        secondPane = fxmlLoader.load(url.openStream());
        secondPageComponentController = fxmlLoader.getController();

        // chatRoom
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CHATROOM_fxml);
        fxmlLoader.setLocation(url);
        chatComponent = fxmlLoader.load(url.openStream());
        chatAreaController = fxmlLoader.getController();

        bodyComponent.setCenter(firstPane);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showPrimaryStage() {
        primaryStage.show();
        chatAreaController.startListRefresher();
        showFirstPage();

        // update contests table
        contestTableRefresher = new ContestTableRefresher(
                contestsTableComponentController::buildElement,
                headerComponentController::setErrorMessage);

        timer = new Timer();
        timer.schedule(contestTableRefresher, DELAY_RATE, REFRESH_RATE);

        // update teams table
        agentsTableRefresher = new AgentsTableRefresher(
                agentsTableComponentController::buildElement,
                headerComponentController::setErrorMessage);

        timer = new Timer();
        timer.schedule(agentsTableRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void showFirstPage() {
        bodyComponent.setCenter(firstPane);
        firstPageComponentController.showChat(chatComponent);

        headerComponentController.setDashboardBtnEnabled();
        headerComponentController.setDashboardBtnSelected();
        headerComponentController.setContestBtnDisabled();
    }

    public void showSecondPage() {
        bodyComponent.setCenter(secondPane);
        secondPageComponentController.showChat(chatComponent);

        headerComponentController.setContestBtnEnabled();
        headerComponentController.setContestBtnSelected();
        headerComponentController.setDashboardBtnDisabled();

        secondPageComponentController.bindReadyBtnToTextFiled();
        secondPageComponentController.startTeamsTableRefresher();
    }

    public void updateUserName(String userName) {
    }

    // Feedback methods
    public void setErrorMessage(String message){
        headerComponentController.setErrorMessage(message);
    }

    public void setSuccessMessage(String message){
        headerComponentController.setSuccessMessage(message);
    }

    public void readyBtnClicked(DTOContestRow dtoContestRow) {

        String uboatName = dtoContestRow.getUboat();

        String finalUrl = HttpUrl
                .parse(CONTEST_ENROLL)
                .newBuilder()
                .addQueryParameter("Uboat", uboatName)
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
                    String responseBody = response.body().string();
                    DTOContestRow selectedContest = GSON_INSTANCE.fromJson(responseBody, DTOContestRow.class);
                    Platform.runLater(() -> {
                        showSecondPage();
                        secondPageComponentController.startContestDataRefresher();
                        secondPageComponentController.startIsUboatLeftRefresher();
                        headerComponentController.setSuccessMessage(String.format("Signing to competition %s succeed", dtoContestRow.getBattleField()));
                    });
                }
            }
        });
    }

    public void markAsReady(String taskSize) {

        String finalUrl = HttpUrl
                .parse(MARK_AS_READY)
                .newBuilder()
                .addQueryParameter("taskSize", taskSize)
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
                        secondPageComponentController.disableTaskSizeComponent();
                        secondPageComponentController.startContestStatusRefresher();
                    });
                }
            }
        });
    }

    public void setWinningTeam(String winningTeam) {
        headerComponentController.setWinningTeam(winningTeam);
    }

    public void setContestMessages(String message) {
        headerComponentController.setContestMessages(message);
    }

    public void clearContestData() {
        // clear tables & progress
        secondPageComponentController.clearContestData();

        // clear task size from server && update agents -> clear contest
        String finalUrl = HttpUrl
                .parse(CLEAR_CONTEST)
                .newBuilder()
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
                    String responseBody = response.body().string();
                }
            }
        });
    }

    public void cancelContestRefreshers() {
        secondPageComponentController.cancelContestRefreshers();
    }

    public void setSkin(SkinsOptions skin){
        Scene scene = primaryStage.getScene();
        this.skin = skin;
        scene.getStylesheets().clear();

        if(skin != SkinsOptions.DEFAULT)
            scene.getStylesheets().add(skin.getCSS());
    }
}
