package DTOs;

import java.util.List;

public class DTOAllieProgressInfo {

    private List<DTOProgressInfo> allAgentsProgress;
    private long totalMissions;
    private long createdMissions;
    private long completedMissions;

    public DTOAllieProgressInfo(List<DTOProgressInfo> agentsProgress, long totalMissions, long createdMissions, long completedMissions){
        this.allAgentsProgress = agentsProgress;
        this.createdMissions = createdMissions;
        this.completedMissions = completedMissions;
        this.totalMissions = totalMissions;
    }

    public List<DTOProgressInfo> getAllAgentsProgress() {
        return allAgentsProgress;
    }

    public long getCompletedMissions() {
        return completedMissions;
    }

    public long getCreatedMissions() {
        return createdMissions;
    }

    public long getTotalMissions() {
        return totalMissions;
    }
}
