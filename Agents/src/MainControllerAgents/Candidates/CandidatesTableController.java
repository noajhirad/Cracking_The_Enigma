package MainControllerAgents.Candidates;

import DTOs.DTOBruteForceResult;
import MainControllerAgents.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CandidatesTableController {

    private MainController mainController;

    @FXML private TableView<CandidateRow> candidatesTable;
    @FXML private TableColumn<CandidateRow, String> stringColumn;
    @FXML private TableColumn<CandidateRow, String> codeColumn;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){
        codeColumn.setCellValueFactory(new PropertyValueFactory<CandidateRow, String>("configStr"));
        stringColumn.setCellValueFactory(new PropertyValueFactory<CandidateRow, String>("str"));
        candidatesTable.setPlaceholder(new Label("No candidates were found yet."));
    }

    public void addToTable(DTOBruteForceResult res) {
        candidatesTable.getItems().add(new CandidateRow(res));
    }

    public void clear() {
        candidatesTable.getItems().clear();
    }
}
