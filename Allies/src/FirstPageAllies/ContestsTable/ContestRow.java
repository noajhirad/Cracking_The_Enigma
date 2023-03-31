package FirstPageAllies.ContestsTable;

import DTOs.DTOContestRow;
import javafx.scene.control.RadioButton;

public class ContestRow {
    private String battleField;
    private String uboat;
    private String contestStatus;
    private String requiredTeams;
    private RadioButton choice;
    private String level;

    public ContestRow(String battleField, String uboat, String contestStatus, String requiredTeams, String level){
        this.battleField = battleField;
        this.uboat = uboat;
        this.contestStatus = contestStatus;
        this.requiredTeams = requiredTeams;
        this.level = level;
        this.choice = new RadioButton();
    }

    public ContestRow(DTOContestRow dtoContestRow){
        this.battleField = dtoContestRow.getBattleField();
        this.uboat = dtoContestRow.getUboat();
        this.contestStatus = dtoContestRow.getContestStatus();
        this.requiredTeams = dtoContestRow.getRequiredTeams();
        this.choice = new RadioButton();
        this.level = dtoContestRow.getLevel();
    }

    public String getBattleField() {
        return battleField;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public String getRequiredTeams() {
        return requiredTeams;
    }

    public String getUboat() {
        return uboat;
    }

    public void setBattleField(String battleField) {
        this.battleField = battleField;
    }

    public void setContestStatus(String contestStatus) {
        this.contestStatus = contestStatus;
    }

    public void setRequiredTeams(String requiredTeams) {
        this.requiredTeams = requiredTeams;
    }

    public void setUboat(String uboat) {
        this.uboat = uboat;
    }

    public RadioButton getChoice() { return choice; }

    public String getLevel() {
        return level;
    }
}
