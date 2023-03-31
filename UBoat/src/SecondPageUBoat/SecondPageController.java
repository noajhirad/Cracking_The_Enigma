package SecondPageUBoat;
import ChatareaUBoat.ChatAreaController;
import MainControllerUBoat.MainController;
import SecondPageUBoat.Dictionary.DictionaryController;
import SecondPageUBoat.EncryptArea.EncryptBruteForceController;
import SecondPageUBoat.TeamsData.TeamsController;
import SecondPageUBoat.ResultsArea.ResultsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Set;
import java.util.Timer;

import static UtilsUBoat.Constants.DELAY_RATE;
import static UtilsUBoat.Constants.REFRESH_RATE;

public class SecondPageController {

    private final static String ENCRYPT_FXML_RESOURCE = "/SecondPageUBoat/EncryptArea/encryptBruteForce.fxml";
    private final static String RESULTS_RESOURCE = "/SecondPageUBoat/ResultsArea/resultsTable.fxml";
    private final static String TEAMS_RESOURCE = "/SecondPageUBoat/TeamsData/teamsTable.fxml";
    private final static String DICTIONARY_RESOURCE = "/SecondPageUBoat/Dictionary/dictionary.fxml";

    @FXML private BorderPane chatComponent;
    @FXML private SplitPane majorSplitPane;
    @FXML private SplitPane smallSplitPane;
    @FXML private BorderPane encryptComponent;
    @FXML private BorderPane activeTeamsComponent;
    @FXML private BorderPane candidatesComponent;
    @FXML private Button readyBtn;
    @FXML private Label macineConfigLabel;
    @FXML private SplitPane ecryptSplitPane;
    @FXML private BorderPane dictionaryComponent;
    @FXML private Button logoutBtn;

    private MainController mainController;
    private EncryptBruteForceController encryptComponentController;
    private ResultsController resultsComponentController;
    private DictionaryController dictionaryComponentController;
    private TeamsController teamsComponentController;
    private ContestStatusRefresher contestStatusRefresher;
    private ChatAreaController chatAreaController;
    private CandidatesRefresher candidatesRefresher;
    private Timer timer;

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if (encryptComponentController != null && resultsComponentController != null
        && dictionaryComponentController != null && teamsComponentController != null) {
            encryptComponentController.setSecondPageController(this);
            resultsComponentController.setSecondPageController(this);
            dictionaryComponentController.setSecondPageController(this);
            teamsComponentController.setSecondPageController(this);
        }
        ecryptSplitPane.setDividerPositions(0.7);
        readyBtn.setDisable(true);
        logoutBtn.setVisible(false);
    }

    private void loadResources() throws Exception {
        // load encrypt controller from fxml
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(ENCRYPT_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        GridPane encrypt = fxmlLoader.load(url.openStream());
        encryptComponentController = fxmlLoader.getController();
        encryptComponent.setCenter(encrypt);

        // load results table
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(RESULTS_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane resultsTable = fxmlLoader.load(url.openStream());
        resultsComponentController = fxmlLoader.getController();
        candidatesComponent.setCenter(resultsTable);

        // load Dictionary
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(DICTIONARY_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane dictionaryElem = fxmlLoader.load(url.openStream());
        dictionaryComponentController = fxmlLoader.getController();
        dictionaryComponent.setCenter(dictionaryElem);

        // load teams table
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(TEAMS_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane teamsTable = fxmlLoader.load(url.openStream());
        teamsComponentController = fxmlLoader.getController();
        activeTeamsComponent.setCenter(teamsTable);
    }

    @FXML
    void readyBtnClicked(ActionEvent event) {
        mainController.markAsReady();

        contestStatusRefresher = new ContestStatusRefresher(
                this::ready,
                mainController::setErrorMessage,
                this::finished);

        timer = new Timer();
        timer.schedule(contestStatusRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void ready() {
        mainController.setCompetitionStarted();

        // candidates refresher
        candidatesRefresher = new CandidatesRefresher(
                resultsComponentController::addCandidatesToTable,
                mainController::setErrorMessage);

        timer = new Timer();
        timer.schedule(candidatesRefresher, DELAY_RATE, REFRESH_RATE);
        logoutBtn.setVisible(false);
    }

    public void finished(String winningTeam){
        mainController.setWinningTeam(winningTeam);
        logoutBtn.setVisible(true);
    }

    public void clearContestData() {
        candidatesRefresher.cancel();
        encryptComponentController.clearContestData();
        resultsComponentController.clearContestData();
        teamsComponentController.clearContestData();
        enableReadyBtn();
    }

    public void encryptBruteForce(String input) throws Exception{
        mainController.encryptBruteForce(input);
    }

    public void resetMachineCode() throws Exception { mainController.resetCode(); }

    public void setMainController(MainController mainController) { this.mainController = mainController; }

    public void setErrorMessage(String message){
        mainController.setErrorMessage(message);
    }

    public void bindComponents(SimpleStringProperty XMLPathProperty) {
        mainController.getCurrentMachineConfigProperty().bindFormattedString(macineConfigLabel);
        dictionaryComponentController.bindToXMLPath(XMLPathProperty);
    }

    public void clearAllFields() {
        encryptComponentController.clearAllFields();
    }

    public void updateOutputField(String output) {
        encryptComponentController.updateOutputField(output);
    }

    public Set<String> getDictionary() {
        return mainController.getDictionary();
    }

    public void setChoosenDictionaryWord(String word) {
        encryptComponentController.setChoosenDictionaryWord(word);
    }

    public void setDictionary(Set<String> dictionary) {
        dictionaryComponentController.setDictionary(dictionary);
    }

    public TeamsController getTeamsController(){
        return teamsComponentController;
    }

    public void disableReadyBtn() {
        readyBtn.setDisable(true);
    }

    public void enableReadyBtn() {
        readyBtn.setDisable(false);
    }

    @FXML
    void logoutBtnClicked(ActionEvent event) {
        mainController.logout();
    }

    public void cancelTimer() {
        contestStatusRefresher.cancel();
        candidatesRefresher.cancel();
    }

    public void setChatController(ChatAreaController chatAreaController) {
        this.chatAreaController = chatAreaController;
    }

    public void showChat(GridPane chatElement){
        chatComponent.setCenter(chatElement);
    }
}
