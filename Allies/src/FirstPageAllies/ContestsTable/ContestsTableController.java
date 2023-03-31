package FirstPageAllies.ContestsTable;

import DTOs.ContestStatus;
import DTOs.DTOContestRow;
import FirstPageAllies.FirstPageController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContestsTableController {

    @FXML private TableView<ContestRow> contestsTable;
    @FXML private TableColumn<ContestRow, String> battleFieldColumn;
    @FXML private TableColumn<ContestRow, String> uboatColumn;
    @FXML private TableColumn<ContestRow, String> contestStatusColumn;
    @FXML private TableColumn<ContestRow, String> levelColumn;
    @FXML private TableColumn<ContestRow, String> requiredTeamsColumn;
    @FXML private TableColumn<ContestRow, RadioButton> choiceColumn;
    @FXML private Button readyBtn;
    private ToggleGroup tGroup = new ToggleGroup();
    private Map<RadioButton, DTOContestRow> btnContestsMap;

    private FirstPageController firstPageController;

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    @FXML
    public void initialize() {
        battleFieldColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, String>("battleField"));
        uboatColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, String>("uboat"));
        contestStatusColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, String>("contestStatus"));
        requiredTeamsColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, String>("requiredTeams"));
        choiceColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, RadioButton>("choice"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<ContestRow, String>("level"));
        contestsTable.setPlaceholder(new Label("No contests were created yet."));
    }

    public void buildElement(List<DTOContestRow> allRows){
        DTOContestRow selectedDtoContestRow = null;
        RadioButton selectedBtn = (RadioButton) tGroup.getSelectedToggle();
        if(selectedBtn != null) {
            selectedDtoContestRow = btnContestsMap.get(selectedBtn);
        }

        contestsTable.getItems().clear();
        btnContestsMap = new HashMap<>();
        tGroup= new ToggleGroup();

        for(DTOContestRow dtoRow: allRows){
            ContestRow row = new ContestRow(dtoRow);
            row.getChoice().setToggleGroup(tGroup);
            if(dtoRow.equals(selectedDtoContestRow))
                row.getChoice().setSelected(true);
            btnContestsMap.put(row.getChoice(), dtoRow);
            contestsTable.getItems().add(row);
        }
    }

    @FXML void readyBtnClicked(ActionEvent event) {
        // get info of selected contest
        RadioButton selectedBtn = (RadioButton) tGroup.getSelectedToggle();

        if(selectedBtn == null) {
            firstPageController.setErrorMessage("No contest was selected. please choose a contest first.");
            return;
        }

        DTOContestRow selectedContest = btnContestsMap.get(selectedBtn);
        if(!selectedContest.getContestStatus().equals(ContestStatus.WAITING.toString())){
            firstPageController.setErrorMessage("You can't join a contest which already started or finished.");
            return;
        }

        firstPageController.readyBtnClicked(selectedContest);
    }
}
