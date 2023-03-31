package DTOs;

public class DTOContestStatus {
    private ContestStatus contestStatus;
    private String winningTeam;
//    private boolean isUboatLeft;

    public DTOContestStatus(ContestStatus status, String winningTeam){
        this.contestStatus = status;
        this.winningTeam = winningTeam;
//        this.isUboatLeft = isUboatLeft;
    }

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

//    public boolean isUboatLeft() {
//        return isUboatLeft;
//    }
}
