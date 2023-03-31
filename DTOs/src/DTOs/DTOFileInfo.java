package DTOs;

public class DTOFileInfo {
    private DTOMachineInfo dtoMachineInfo;
    private DTOBattleField dtoBattleField;

    public DTOMachineInfo getDtoMachineInfo() {
        return dtoMachineInfo;
    }

    public void setDtoMachineInfo(DTOMachineInfo dtoMachineInfo) {
        this.dtoMachineInfo = dtoMachineInfo;
    }

    public void setDtoBattleField(DTOBattleField dtoBattleField) {
        this.dtoBattleField = dtoBattleField;
    }

    public DTOBattleField getDtoBattleField() {
        return dtoBattleField;
    }
}
