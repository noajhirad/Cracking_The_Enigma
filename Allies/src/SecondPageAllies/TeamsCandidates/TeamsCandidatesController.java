package SecondPageAllies.TeamsCandidates;

import DTOs.DTOBruteForceResult;
import DTOs.DTOCandidatesAndVersion;
import SecondPageAllies.SecondPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TeamsCandidatesController {
    private SecondPageController secondPageController;
    @FXML private TableView<CandidatesRow> resultsTable;
    @FXML private TableColumn<CandidatesRow, String> stringColumn;
    @FXML private TableColumn<CandidatesRow, String> agentColumn;
    @FXML private TableColumn<CandidatesRow, String> codeColumn;

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    @FXML
    public void initialize(){
        codeColumn.setCellValueFactory(new PropertyValueFactory<CandidatesRow, String>("configStr"));
        stringColumn.setCellValueFactory(new PropertyValueFactory<CandidatesRow, String>("str"));
        agentColumn.setCellValueFactory(new PropertyValueFactory<CandidatesRow, String>("agent"));
        resultsTable.setPlaceholder(new Label("No candidates were found yet."));
    }

    public void addCandidatesToTable(DTOCandidatesAndVersion candidatesAndVersion){
        List<DTOBruteForceResult> allCandidates = candidatesAndVersion.getCandidates();
        if(allCandidates == null)
            return;
        for(DTOBruteForceResult res : allCandidates){
            resultsTable.getItems().add(new CandidatesRow(res));
        }
    }

    public void clear() {
        resultsTable.getItems().clear();
    }
}
