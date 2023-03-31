package SecondPageAllies.TeamsData;

import DTOs.DTOTeamRow;
import SecondPageAllies.SecondPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TeamsController {
    private SecondPageController secondPageController;

    @FXML private TableView<TeamsRow> teamsTable;
    @FXML private TableColumn<TeamsRow, String> nameColumn;
    @FXML private TableColumn<TeamsRow, String> agentsColumn;
    @FXML private TableColumn<TeamsRow, String> taskSizeColumn;

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    @FXML
    public void initialize(){
        taskSizeColumn.setCellValueFactory(new PropertyValueFactory<TeamsRow, String>("taskSize"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<TeamsRow, String>("name"));
        agentsColumn.setCellValueFactory(new PropertyValueFactory<TeamsRow, String>("agents"));
        teamsTable.setPlaceholder(new Label("No teams enrolled yet."));
    }

    public void buildElement(List<DTOTeamRow> dtoTeamRows) {
        teamsTable.getItems().clear();

        for (DTOTeamRow dtoRow: dtoTeamRows) {
            TeamsRow row = new TeamsRow(dtoRow);
            teamsTable.getItems().add(row);
        }
    }

    public void clear() {
        teamsTable.getItems().clear();
    }
}