package MainControllerAgents.ContestData;

import DTOs.DTOAllieContestData;
import DTOs.DTOContestRow;
import MainControllerAgents.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ContestDataController {

    @FXML private Label allieTeamPH1;
    @FXML private Label battleFieldPH;
    @FXML private Label uBoatPH;
    @FXML private Label contestStatusPH;
    @FXML private Label requiredTeamsPH;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateFields(DTOAllieContestData data){
        allieTeamPH1.setText(data.getAllieName());

        DTOContestRow contest = data.getDtoContestRow();
        battleFieldPH.setText(contest.getBattleField());
        uBoatPH.setText(contest.getUboat());
        contestStatusPH.setText(contest.getContestStatus());
        requiredTeamsPH.setText(contest.getRequiredTeams());

    }
}
