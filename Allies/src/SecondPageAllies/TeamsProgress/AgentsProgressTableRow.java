package SecondPageAllies.TeamsProgress;

import DTOs.DTOProgressInfo;

public class AgentsProgressTableRow {

    private String agentName;
    private String totalMissions;
    private String waitingMissions;
    private String foundCandidates;
    private String currentInput;

    public AgentsProgressTableRow(String agentName, String totalMissions, String waitingMissions,
                                  String foundCandidates, String currentInput){
        this.agentName = agentName;
        this.totalMissions = totalMissions;
        this.waitingMissions = waitingMissions;
        this.foundCandidates = foundCandidates;
        this.currentInput = currentInput;
    }

    public AgentsProgressTableRow(DTOProgressInfo progressInfo){
        this.agentName = progressInfo.getAgentName();
        this.totalMissions = progressInfo.getTotalMissions();
        this.waitingMissions = progressInfo.getWaitingMissions();
        this.foundCandidates = progressInfo.getFoundCandidates();
        this.currentInput = progressInfo.getCurrentInput();
    }

    public String getAgentName() {
        return agentName;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public String getFoundCandidates() {
        return foundCandidates;
    }

    public String getTotalMissions() {
        return totalMissions;
    }

    public String getWaitingMissions() {
        return waitingMissions;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setTotalMissions(String totalMissions) {
        this.totalMissions = totalMissions;
    }

    public void setWaitingMissions(String waitingMissions) {
        this.waitingMissions = waitingMissions;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public void setFoundCandidates(String foundCandidates) {
        this.foundCandidates = foundCandidates;
    }
}
