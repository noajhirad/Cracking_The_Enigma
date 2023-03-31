package SecondPageAllies;

import BruteForceAllies.BruteForceManager;
import ChatareaAllies.ChatAreaController;
import DTOs.DTOContestRow;
import Exceptions.BruteForceException;
import MainControllerAllies.MainController;
import SecondPageAllies.ContestData.ContestDataController;
import SecondPageAllies.TeamsCandidates.TeamsCandidatesController;
import SecondPageAllies.TeamsData.TeamsController;
import SecondPageAllies.TeamsData.TeamsTableRefresher;
import SecondPageAllies.TeamsProgress.TeamsProgressController;
import SecondPageAllies.TeamsProgress.TeamsProgressRefresher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static UtilsAllies.Constants.REFRESH_RATE;
import static UtilsAllies.Constants.DELAY_RATE;
import java.net.URL;
import java.util.Timer;

public class SecondPageController {

    private final static String CONTEST_DATA_FXML_RESOURCE = "/SecondPageAllies/ContestData/contestData.fxml";
    private final static String CANDIDATES_RESOURCE = "/SecondPageAllies/TeamsCandidates/teamsCandidates.fxml";
    private final static String TEAM_DATA_RESOURCE = "/SecondPageAllies/TeamsData/teamsTable.fxml";
    private final static String PROGRESS_RESOURCE = "/SecondPageAllies/TeamsProgress/teamsProgress.fxml";
    @FXML private BorderPane chatComponent;

    @FXML private SplitPane majorSplitPane;
    @FXML private SplitPane leftSplitPane;
    @FXML private SplitPane rightSplitPane;

    @FXML private BorderPane contestDataComponent;
    @FXML private BorderPane contestsTeamsComponent;
    @FXML private BorderPane progressComponent;
    @FXML private BorderPane candidatesComponent;
    @FXML private TextField taskSizeTextField;
    @FXML private Button readyBtn;

    // controllers
    private MainController mainController;
    private ContestDataController contestDataComponentController;
    private TeamsCandidatesController teamsCandidateComponentController;
    private TeamsController teamsComponentController;
    private TeamsProgressController teamsProgressComponentController;
    private ChatAreaController chatAreaController;
    private ContestStatusRefresher contestStatusRefresher;
    private CandidatesRefresher candidatesRefresher;
    private Timer timer;
    private TeamsProgressRefresher progressRefresher;
    private TeamsTableRefresher teamsTableRefresher;
    private ContestDataRefresher contestDataRefresher;
    private UboatLeftRefresher uboatLeftRefresher = null;

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if (contestDataComponentController != null && teamsCandidateComponentController != null
                && teamsComponentController != null && teamsProgressComponentController != null) {
            contestDataComponentController.setSecondPageController(this);
            teamsCandidateComponentController.setSecondPageController(this);
            teamsComponentController.setSecondPageController(this);
            teamsProgressComponentController.setSecondPageController(this);
        }
        leftSplitPane.setDividerPositions(0.33);
        rightSplitPane.setDividerPositions(0.33,0.66);
        bindReadyBtnToTextFiled();
    }

    public void bindReadyBtnToTextFiled() {
        readyBtn.disableProperty().bind(taskSizeTextField.textProperty().isEmpty());
    }

    private void loadResources() throws Exception {

        // contest data
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(CONTEST_DATA_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane contestData = fxmlLoader.load(url.openStream());
        contestDataComponentController = fxmlLoader.getController();
        contestDataComponent.setCenter(contestData);

        // teams candidate
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CANDIDATES_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane candidate = fxmlLoader.load(url.openStream());
        teamsCandidateComponentController = fxmlLoader.getController();
        candidatesComponent.setCenter(candidate);

        // teams
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(TEAM_DATA_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane teamsElem = fxmlLoader.load(url.openStream());
        teamsComponentController = fxmlLoader.getController();
        contestsTeamsComponent.setCenter(teamsElem);

        // progress
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(PROGRESS_RESOURCE);
        fxmlLoader.setLocation(url);
        SplitPane progress = fxmlLoader.load(url.openStream());
        teamsProgressComponentController = fxmlLoader.getController();
        progressComponent.setCenter(progress);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public TeamsController getTeamsController() {
        return teamsComponentController;
    }

    public void setContestInfo(DTOContestRow selectedContest) {
        contestDataComponentController.setContestInfo(selectedContest);
    }

    @FXML
    void readyBtnClicked(ActionEvent event) {
        String taskSize = taskSizeTextField.getText();
        try{
            int taskSizeInt = Integer.parseInt(taskSize);
            if(taskSizeInt < 1)
                throw new BruteForceException(BruteForceException.ErrorType.INVALID_TASK_SIZE);
            mainController.markAsReady(taskSize);
        }
        catch (Exception e){
            mainController.setErrorMessage(BruteForceException.ErrorType.INVALID_TASK_SIZE.getMessage());
        }
    }

    public void startContestStatusRefresher(){
        contestStatusRefresher = new ContestStatusRefresher(
                this::ready,
                mainController::setErrorMessage,
                this::finish);

        timer = new Timer();
        timer.schedule(contestStatusRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void startIsUboatLeftRefresher(){
        uboatLeftRefresher = new UboatLeftRefresher(
                this::uboatLeft,
                mainController::setErrorMessage);

        timer = new Timer();
        timer.schedule(uboatLeftRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void ready() {
        mainController.setContestMessages("Contest Started!");
        BruteForceManager bruteForceManager = new BruteForceManager(mainController);
        bruteForceManager.startBruteForce();

        if(uboatLeftRefresher != null)
            uboatLeftRefresher.cancel();

        // candidates refresher
        candidatesRefresher = new CandidatesRefresher(
                teamsCandidateComponentController::addCandidatesToTable,
                mainController::setErrorMessage);

        timer = new Timer();
        timer.schedule(candidatesRefresher, DELAY_RATE, REFRESH_RATE);

        // progress refresher
        progressRefresher = new TeamsProgressRefresher(
                teamsProgressComponentController::updateProgress,
                mainController::setErrorMessage);

        timer = new Timer();
        timer.schedule(progressRefresher, DELAY_RATE, REFRESH_RATE);
    }
    
    public void finish(String winningTeam) {
        //progressRefresher.cancel();
        teamsTableRefresher.cancel();
        mainController.setWinningTeam(winningTeam);
    }

    public void uboatLeft(){
        // cancel refreshers
        teamsTableRefresher.cancel();
        cancelContestRefreshers();

        mainController.setContestMessages("The Uboat has left the contest! Please select a different contest.");
        mainController.showFirstPage();
        uboatLeftRefresher.cancel();
    }

    public void cancelContestRefreshers(){
        candidatesRefresher.cancel();
        contestStatusRefresher.cancel();
        progressRefresher.cancel();
    }

    public void disableTaskSizeComponent(){
        readyBtn.disableProperty().unbind();
        readyBtn.setDisable(true);
        taskSizeTextField.setDisable(true);
    }

    public void clearContestData() {
        teamsComponentController.clear();
        teamsCandidateComponentController.clear();
        teamsProgressComponentController.clear();
        taskSizeTextField.clear();
        taskSizeTextField.setDisable(false);
    }

    public void startTeamsTableRefresher() {
        // update teams table
        teamsTableRefresher = new TeamsTableRefresher(
                teamsComponentController::buildElement,
                mainController::setErrorMessage);

        timer = new Timer();
        timer.schedule(teamsTableRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void startContestDataRefresher() {
        contestDataRefresher = new ContestDataRefresher(
                contestDataComponentController::setContestInfo,
                mainController::setErrorMessage);
        timer = new Timer();
        timer.schedule(contestDataRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void setChatController(ChatAreaController chatAreaController) {
        this.chatAreaController = chatAreaController;
    }

    public void showChat(GridPane chatElement){
        chatComponent.setCenter(chatElement);
    }
}
