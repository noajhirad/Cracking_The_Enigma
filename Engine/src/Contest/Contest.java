package Contest;

import DTOs.ContestStatus;
import DTOs.DTOContestRow;
import Users.Uboat;

public class Contest {
    private BattleField battleField;
    private Uboat uboat;
    private ContestStatus status;

    public Contest(BattleField battleField, Uboat uboat){
        this.battleField = battleField;
        this.uboat = uboat;
        this.status = ContestStatus.WAITING;
    }

    public String getBattleName() {
        return battleField.getName();
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public Uboat getUboat() {
        return uboat;
    }

    public DTOContestRow getContestDTO() {
        DTOContestRow dto = new DTOContestRow(battleField.getName(),
                uboat.getUserName(),
                status.toString(),
                Integer.toString(battleField.getNumOfAllies()),
                battleField.getDifficulty().toString());
        return dto;
    }

    public void setContestStatus(ContestStatus status){
        this.status = status;
    }

    public ContestStatus getContestStatus(){
        return status;
    }
}

