package FirstPageAllies;

import ChatareaAllies.ChatAreaController;
import DTOs.DTOContestRow;
import FirstPageAllies.AgentsTable.AgentsTableController;
import FirstPageAllies.ContestsTable.ContestsTableController;
import MainControllerAllies.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;

public class FirstPageController {
    private final static String AGENTS_TABLE_FXML = "/FirstPageAllies/AgentsTable/agentsTable.fxml";
    private final static String CONTESTS_TABLE_FXML = "/FirstPageAllies/ContestsTable/contestTable.fxml";
    @FXML private BorderPane chatComponent;
    @FXML private SplitPane splitPane;
    @FXML private BorderPane agentsDataComponent;
    @FXML private BorderPane contestDataComponent;

    // controllers
    private MainController mainController;
    private AgentsTableController  agentsTableComponentController;
    private ContestsTableController contestsTableComponentController;
    private ChatAreaController chatAreaController;


    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(agentsTableComponentController != null && contestsTableComponentController != null) {
            agentsTableComponentController.setFirstPageController(this);
            contestsTableComponentController.setFirstPageController(this);
        }
        //setSplitPaneSizes();
    }

    private void loadResources() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        // load Agents Table controller & component from fxml
        URL url = getClass().getResource(AGENTS_TABLE_FXML);
        fxmlLoader.setLocation(url);
        BorderPane agentsTable = fxmlLoader.load(url.openStream());
        agentsTableComponentController = fxmlLoader.getController();
        agentsDataComponent.setCenter(agentsTable);

        // load Contest Table controller & component from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CONTESTS_TABLE_FXML);
        fxmlLoader.setLocation(url);
        BorderPane contestTable = fxmlLoader.load(url.openStream());
        contestsTableComponentController = fxmlLoader.getController();
        contestDataComponent.setCenter(contestTable);
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public ContestsTableController getContestTableController() {
        return contestsTableComponentController;
    }

    public AgentsTableController getAgentsTableController() {
        return agentsTableComponentController;
    }

    public void setErrorMessage(String s) {
        mainController.setErrorMessage(s);
    }

    public void readyBtnClicked(DTOContestRow dtoContestRow) {
        mainController.readyBtnClicked(dtoContestRow);
    }

    public void setChatController(ChatAreaController chatAreaController) {
        this.chatAreaController = chatAreaController;
    }

    public void showChat(GridPane chatElement){
        chatComponent.setCenter(chatElement);
    }
}
