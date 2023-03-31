package AgentBruteForce;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class AgentProgressInfo {
    private SimpleIntegerProperty missionsInQueue = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty missionsInProgress = new SimpleIntegerProperty(0);
    private SimpleLongProperty completedMissions = new SimpleLongProperty(0);
    private SimpleIntegerProperty candidateFound = new SimpleIntegerProperty(0);
    private SimpleLongProperty totalMissions = new SimpleLongProperty(0);

    // Missions in queue
    public void incrementMissionsInQueue(){
        missionsInQueue.set(missionsInQueue.get() + 1);
    }

    public void decrementMissionsInQueue(){
        missionsInQueue.set(missionsInQueue.get() - 1);
    }

    public SimpleIntegerProperty getMissionsInQueue() {
        return missionsInQueue;
    }

    public void setMissionsInQueue(int size) {
        this.missionsInQueue.set(size);
    }

    // Missions In Progress
    public void incrementMissionsInProgress(){
         missionsInProgress.set(missionsInProgress.get() + 1);
    }

    public void decrementMissionsInProgress() {
         missionsInProgress.set(missionsInProgress.get() - 1);
    }

    public SimpleIntegerProperty getMissionsInProgress() {
        return missionsInProgress;
    }

    // Candidates
    public void incrementCandidateFound(){
        candidateFound.set(candidateFound.get() + 1);
    }

    public void addToCandidateFound(int val){
        candidateFound.set(candidateFound.get() + val);
    }

    public SimpleIntegerProperty getCandidateFound() {
        return candidateFound;
    }

    // Completed Missions
    public void incrementCompletedMissions(){
        completedMissions.set(completedMissions.get() + 1);
    }

    public SimpleLongProperty getCompletedMissions() {
        return completedMissions;
    }

    public void addToTotalMissions(int size) {
        totalMissions.set(totalMissions.get() + size);
        // totalMissions.add(totalMissions.get() + size);
    }

    public SimpleLongProperty getTotalMissions() {
        return totalMissions;
    }
}
