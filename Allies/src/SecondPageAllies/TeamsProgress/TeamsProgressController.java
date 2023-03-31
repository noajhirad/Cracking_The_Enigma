package SecondPageAllies.TeamsProgress;

import DTOs.DTOAllieProgressInfo;
import DTOs.DTOProgressInfo;
import SecondPageAllies.SecondPageController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sun.nio.ch.SelectorImpl;

import java.util.List;

public class TeamsProgressController {

    @FXML private SplitPane splitPane;
    // table
    @FXML private TableView<AgentsProgressTableRow> agentsProgressTable;
    @FXML private TableColumn<AgentsProgressTableRow, String> agentNameColumn;
    @FXML private TableColumn<AgentsProgressTableRow, String> totalMissionsColumn;
    @FXML private TableColumn<AgentsProgressTableRow, String> waitingMissionsColumn;
    @FXML private TableColumn<AgentsProgressTableRow, String> foundCandidatesColumn;
    @FXML private TableColumn<AgentsProgressTableRow, String> currentInputColumn;

    // progress fields
    @FXML private ProgressBar progressBar;
    @FXML private Label totalMissionsPH;
    @FXML private Label createdMissionsPH;
    @FXML private Label completedMissionsPH;

    private SecondPageController secondPageController;
    private SimpleDoubleProperty prop = new SimpleDoubleProperty(0);
    private SimpleLongProperty completedMissionsProp = new SimpleLongProperty(0);
    private SimpleLongProperty totalMissionsProp = new SimpleLongProperty(1);

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    @FXML
    public void initialize(){
        agentNameColumn.setCellValueFactory(new PropertyValueFactory<AgentsProgressTableRow, String>("agentName"));
        totalMissionsColumn.setCellValueFactory(new PropertyValueFactory<AgentsProgressTableRow, String>("totalMissions"));
        waitingMissionsColumn.setCellValueFactory(new PropertyValueFactory<AgentsProgressTableRow, String>("waitingMissions"));
        foundCandidatesColumn.setCellValueFactory(new PropertyValueFactory<AgentsProgressTableRow, String>("foundCandidates"));
        currentInputColumn.setCellValueFactory(new PropertyValueFactory<AgentsProgressTableRow, String>("currentInput"));

        bindProgressBar();
    }

    public void bindProgressBar() {
        prop.set(0);
        progressBar.progressProperty().bind(prop);
    }

    public void updateProgress(DTOAllieProgressInfo progressInfo){

        if (progressInfo == null)
            return;

        List<DTOProgressInfo> progressInfoList = progressInfo.getAllAgentsProgress();
        if(progressInfoList == null)
            return;

        // update fields
        completedMissionsPH.setText(Long.toString(progressInfo.getCompletedMissions()));
        totalMissionsPH.setText(Long.toString(progressInfo.getTotalMissions()));
        createdMissionsPH.setText(Long.toString(progressInfo.getCreatedMissions()));

        // progress bar
        completedMissionsProp.set(progressInfo.getCompletedMissions());
        if(progressInfo.getTotalMissions() != 0) {
            totalMissionsProp.set(progressInfo.getTotalMissions());
            prop.set(((float)completedMissionsProp.get())/((float)totalMissionsProp.get()));
        }

        // update table
        agentsProgressTable.getItems().clear();
        for(DTOProgressInfo progress : progressInfo.getAllAgentsProgress()) {
            if(progress == null)
                continue;
            agentsProgressTable.getItems().add(new AgentsProgressTableRow(progress));
        }
    }

    public void clear() {
        agentsProgressTable.getItems().clear();
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);

        completedMissionsPH.setText("");
        totalMissionsPH.setText("");
        createdMissionsPH.setText("");
    }
}
