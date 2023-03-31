package FirstPageAllies.AgentsTable;

import DTOs.DTOAgentRow;
import DTOs.DTOContestRow;
import FirstPageAllies.ContestsTable.ContestRow;
import FirstPageAllies.FirstPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AgentsTableController {

    @FXML private TableView<AgentRow> teamsTable;
    @FXML private TableColumn<AgentRow, String> nameColumn;
    @FXML private TableColumn<AgentRow, String> threadsNumberColumn;
    @FXML private TableColumn<AgentRow, String> taskSizeColumn;

    private FirstPageController firstPageController;

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<AgentRow, String>("name"));
        threadsNumberColumn.setCellValueFactory(new PropertyValueFactory<AgentRow, String>("threadsNumber"));
        taskSizeColumn.setCellValueFactory(new PropertyValueFactory<AgentRow, String>("taskSize"));
        teamsTable.setPlaceholder(new Label("No agents enrolled yet."));
    }

    public void buildElement(List<DTOAgentRow> allRows){
        teamsTable.getItems().clear();

        for(DTOAgentRow dtoRow : allRows){
            AgentRow row = new AgentRow(dtoRow);
            teamsTable.getItems().add(row);
        }
    }

}
