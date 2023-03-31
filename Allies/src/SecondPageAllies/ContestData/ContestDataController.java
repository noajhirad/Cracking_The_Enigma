package SecondPageAllies.ContestData;

import DTOs.DTOContestRow;
import SecondPageAllies.SecondPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestDataController {

    @FXML private Label battleFieldPH;
    @FXML private Label uBoatPH;
    @FXML private Label contestStatusPH;
    @FXML private Label requiredTeamsPH;
    @FXML private Label levelPH;

    private SecondPageController secondPageController;

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    public void setContestInfo(DTOContestRow selectedContest) {
        battleFieldPH.setText(selectedContest.getBattleField());
        uBoatPH.setText(selectedContest.getUboat());
        contestStatusPH.setText(selectedContest.getContestStatus());
        requiredTeamsPH.setText(selectedContest.getRequiredTeams());
        levelPH.setText(selectedContest.getLevel());
    }
}
