package MainControllerAgents;

import AgentBruteForce.AgentProgressInfo;
import ChatareaAgent.ChatAreaController;
import DTOs.DTOBruteForceResult;
import AgentBruteForce.ThreadPoolManager;
import MainControllerAgents.Candidates.CandidatesTableController;
import MainControllerAgents.ContestData.ContestDataController;
import MainControllerAgents.ContestData.ContestDataRefresher;
import MainControllerAgents.Progress.AgentProgressController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.Timer;

import static UtilsAgent.Constants.DELAY_RATE;
import static UtilsAgent.Constants.REFRESH_RATE;

public class MainController {
    private final static String CANDIDATES_fXML_RESOURCE = "/MainControllerAgents/Candidates/candidatesTable.fxml";
    private final static String CONTEST_DATA_fXML_RESOURCE = "/MainControllerAgents/ContestData/contestData.fxml";
    private final static String PROGRESS_fXML_RESOURCE = "/MainControllerAgents/Progress/agentProgress.fxml";
    private final static String CHATROOM_fxml = "/ChatareaAgent/chat-area.fxml";

    @FXML private BorderPane chatComponent;
    @FXML private SplitPane majorSplitPane;
    @FXML private BorderPane contestDataComponent;
    @FXML private SplitPane smallSplitPane;
    @FXML private BorderPane progressComponent;
    @FXML private BorderPane candidateComponent;
    @FXML private Label feedbackLabel;
    @FXML private Menu skinsMenu;
    @FXML private MenuItem defaultMenuItem;
    @FXML private MenuItem peachMenuItem;
    @FXML private MenuItem cookieMenuItem;
    @FXML private MenuItem lionMenuItem;
    private Stage primaryStage;
    private Timer timer;
    private ReadyStatusRefresher readyStatusRefresher;
    private ContestDataRefresher contestDataRefresher;
    private ContestStatusRefresher contestStatusRefresher;
    private ProgressRefresher progressRefresher;
    private int threadsNumber;
    private String taskSize;
    private String userName;
    private String allie;
    private SkinsOptions skin = SkinsOptions.DEFAULT;

    // Controllers
    private CandidatesTableController candidatesTableComponentController;
    private ContestDataController contestDataComponentController;
    private AgentProgressController agentProgressComponentController;
    private ThreadPoolManager threadPoolManager;
    private ChatAreaController chatAreaController;

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if (candidatesTableComponentController != null && contestDataComponentController != null &&
                agentProgressComponentController != null) {

            candidatesTableComponentController.setMainController(this);
            contestDataComponentController.setMainController(this);
            agentProgressComponentController.setMainController(this);
        }
        majorSplitPane.setDividerPositions(0.33);
    }

    private void loadResources() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        // candidates
        URL url = getClass().getResource(CANDIDATES_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane candidateElem = fxmlLoader.load(url.openStream());
        candidatesTableComponentController = fxmlLoader.getController();
        candidateComponent.setCenter(candidateElem);

        // contest data
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CONTEST_DATA_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane contestElem = fxmlLoader.load(url.openStream());
        contestDataComponentController = fxmlLoader.getController();
        contestDataComponent.setCenter(contestElem);

        // progress
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(PROGRESS_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        GridPane progressElem = fxmlLoader.load(url.openStream());
        agentProgressComponentController = fxmlLoader.getController();
        progressComponent.setCenter(progressElem);

        // chatRoom
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CHATROOM_fxml);
        fxmlLoader.setLocation(url);
        GridPane chatRoomElem = fxmlLoader.load(url.openStream());
        chatAreaController = fxmlLoader.getController();
        chatComponent.setCenter(chatRoomElem);
    }

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void showPrimaryStage() {
        primaryStage.show();
        chatAreaController.startListRefresher();
        // update teams table
        contestDataRefresher = new ContestDataRefresher(
                contestDataComponentController::updateFields,
                this::setErrorMessage);

        timer = new Timer();
        timer.schedule(contestDataRefresher, DELAY_RATE, REFRESH_RATE);

        // update ready refresher
        readyStatusRefresher = new ReadyStatusRefresher(
                this::ready,
                this::setErrorMessage);

        timer = new Timer();
        timer.schedule(readyStatusRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void ready() {

        setContestStarted();
        readyStatusRefresher.cancel();
        threadPoolManager = new ThreadPoolManager(this, threadsNumber, allie);

        // contestData refresher
        contestStatusRefresher = new ContestStatusRefresher(
                this::setErrorMessage,
                this::finish,
                this::waiting);

        timer = new Timer();
        timer.schedule(contestStatusRefresher, DELAY_RATE, REFRESH_RATE);

        // progress refresher
        progressRefresher = new ProgressRefresher(
                this::setErrorMessage, threadPoolManager::getProgress);

        timer = new Timer();
        timer.schedule(progressRefresher, DELAY_RATE, REFRESH_RATE);

    }

    public void finish(String winningTeam){
        threadPoolManager.stopBruteForce();
//        progressRefresher.cancel();
        setWinningTeam(winningTeam);
    }

    public void waiting(){
        progressRefresher.cancel();
        contestStatusRefresher.cancel();
        // clear contest tables
        candidatesTableComponentController.clear();
        agentProgressComponentController.clear();
        feedbackLabel.setText("");

        // update ready refresher
        readyStatusRefresher = new ReadyStatusRefresher(
                this::ready,
                this::setErrorMessage);

        timer = new Timer();
        timer.schedule(readyStatusRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void updateUserName(String userName) {
        this.userName = userName;
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

    public void setThreadsNumber(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    public String getTaskSize(){
        return taskSize;
    }

    public void setTaskSize(String taskSize) {
        this.taskSize = taskSize;
    }

    public void bindLabelsToProgressProperty(AgentProgressInfo progressInfo){
        agentProgressComponentController.bindLabelsToProgressProperty(progressInfo);
    }

    public void addToResultsTable(DTOBruteForceResult res) {
        candidatesTableComponentController.addToTable(res);
    }

    public String getAgentName() {
        return userName;
    }

    public void setAllie(String value) {
        this.allie = value;
    }

    public void setWinningTeam(String winningTeam) {
        feedbackLabel.setText("Contest is over! The winning team: " + winningTeam);
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
        if(skin == SkinsOptions.LION_KING)
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: white");
    }

    public void setContestStarted() {
        feedbackLabel.setText("Contest Started!");
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: black");
        if(skin == SkinsOptions.LION_KING)
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: white");
    }

    public void setCompletedMissionsProp(long size) {
        agentProgressComponentController.setCompletedMissionsProp(size);
    }

    public void setTotalMissionsProp(long size) {
        agentProgressComponentController.setTotalMissionsProp(size);

    }
    @FXML
    void cookieMenuItemClicked(ActionEvent event) {
        setSkin(SkinsOptions.COOKIE_MONSTER);
    }

    @FXML
    void defaultMenuItemClicked(ActionEvent event) { setSkin(SkinsOptions.DEFAULT); }

    @FXML
    void lionMenuItemClicked(ActionEvent event) { setSkin(SkinsOptions.LION_KING); }

    @FXML
    void peachMenuItemClicked(ActionEvent event) { setSkin(SkinsOptions.PEACH);}
    public void setSkin(SkinsOptions skin){
        Scene scene = primaryStage.getScene();
        this.skin = skin;
        scene.getStylesheets().clear();

        if(skin != SkinsOptions.DEFAULT)
            scene.getStylesheets().add(skin.getCSS());
    }
}
