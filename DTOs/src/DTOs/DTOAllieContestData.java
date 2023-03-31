package DTOs;

public class DTOAllieContestData {
    private String allieName;
    private DTOContestRow dtoContestRow;

    public DTOAllieContestData(String allieName, DTOContestRow dtoContestRow) {
        this.allieName = allieName;
        this.dtoContestRow = dtoContestRow;
    }

    public DTOContestRow getDtoContestRow() {
        return dtoContestRow;
    }

    public String getAllieName() {
        return allieName;
    }
}
