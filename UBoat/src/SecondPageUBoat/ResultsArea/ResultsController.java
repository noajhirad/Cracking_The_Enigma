package SecondPageUBoat.ResultsArea;

import DTOs.DTOBruteForceResult;
import DTOs.DTOCandidatesAndVersion;
import SecondPageUBoat.SecondPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ResultsController {
    private SecondPageController secondPageController;
    @FXML private TableView<ResultRow> resultsTable;
    @FXML private TableColumn<ResultRow, String> stringColumn;
    @FXML private TableColumn<ResultRow, String> alliesColumn;
    @FXML private TableColumn<ResultRow, String> codeColumn;

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    @FXML
    public void initialize(){
        codeColumn.setCellValueFactory(new PropertyValueFactory<ResultRow, String>("configStr"));
        stringColumn.setCellValueFactory(new PropertyValueFactory<ResultRow, String>("str"));
        alliesColumn.setCellValueFactory(new PropertyValueFactory<ResultRow, String>("allies"));
        resultsTable.setPlaceholder(new Label("No candidates were found yet."));
    }

    public void addCandidatesToTable(DTOCandidatesAndVersion candidatesAndVersion){
        List<DTOBruteForceResult> allCandidates = candidatesAndVersion.getCandidates();
        if(allCandidates == null)
            return;

        for(DTOBruteForceResult res : allCandidates){
            resultsTable.getItems().add(new ResultRow(res));
        }
    }

    public void clearContestData() {
        resultsTable.getItems().clear();
    }
}
