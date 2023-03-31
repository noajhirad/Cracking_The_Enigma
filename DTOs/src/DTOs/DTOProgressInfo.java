package DTOs;

public class DTOProgressInfo {
    private String agentName;
    private String totalMissions;
    private String waitingMissions;
    private String foundCandidates;
    private String currentInput;
    private String completedMissions;

    public DTOProgressInfo(String agentName, String totalMissions, String waitingMissions,
                           String foundCandidates, String currentInput, String completedMissions){
        this.agentName = agentName;
        this.totalMissions = totalMissions;
        this.waitingMissions = waitingMissions;
        this.foundCandidates = foundCandidates;
        this.currentInput = currentInput;
        this.completedMissions = completedMissions;
    }

    public String getWaitingMissions() {
        return waitingMissions;
    }

    public String getTotalMissions() {
        return totalMissions;
    }

    public String getFoundCandidates() {
        return foundCandidates;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getCompletedMissions() { return completedMissions; }
}
