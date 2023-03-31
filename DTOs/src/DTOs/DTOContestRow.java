package DTOs;

import java.util.Objects;

public class DTOContestRow {

    private String battleField;
    private String uboat;
    private String contestStatus;
    private String requiredTeams;
    private String level;

    public DTOContestRow() {
        this.battleField = "";
        this.contestStatus = "";
        this.requiredTeams = "";
        this.uboat = "";
        this.level = "";
    }

    public DTOContestRow(String battleField, String uboat, String contestStatus, String requiredTeams, String level) {
        this.battleField = battleField;
        this.contestStatus = contestStatus;
        this.requiredTeams = requiredTeams;
        this.uboat = uboat;
        this.level = level;
    }

    public String getUboat() {
        return uboat;
    }

    public String getRequiredTeams() {
        return requiredTeams;
    }

    public String getContestStatus() {
        return contestStatus;
    }

    public String getBattleField() {
        return battleField;
    }

    public String getLevel(){ return level; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOContestRow that = (DTOContestRow) o;
        return Objects.equals(battleField, that.battleField) && Objects.equals(uboat, that.uboat) &&
                Objects.equals(contestStatus, that.contestStatus) && Objects.equals(requiredTeams, that.requiredTeams)
                && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(battleField, uboat, contestStatus, requiredTeams, level);
    }
}
