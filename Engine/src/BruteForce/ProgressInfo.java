package BruteForce;

public class ProgressInfo {
    private long totalEncryptions = 0;
    private long completedEncryptions= 0;
    private int addedToFinishedQ= 0;
    private int removedFromFinishedQ= 0;
    private String statusMessage= "";
    private long totalTasks = 0;
    private long completedTasks = 0;
    private long totalTime = 0;

    public ProgressInfo(){ }

    public ProgressInfo(long totalEncryptions, long completedEncryptions, int addedToFinishedQ,
                        int removedFromFinishedQ, String statusMessage){
        this.totalEncryptions = totalEncryptions;
        this.completedEncryptions = completedEncryptions;
        this.addedToFinishedQ = addedToFinishedQ;
        this.removedFromFinishedQ = removedFromFinishedQ;
        this.statusMessage = statusMessage;
    }

    public long getTotalCompleted(){
        return completedEncryptions + removedFromFinishedQ;
    }

    public long getTotalMissions() {
        return totalEncryptions + addedToFinishedQ;
    }

    public Long getCompletedEncryptions() { return completedEncryptions; }

    public long getTotalEncryptions() { return totalEncryptions; }

    public int getAddedToFinishedQ() {
        return addedToFinishedQ;
    }

    public int getRemovedFromFinishedQ() {
        return removedFromFinishedQ;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public String getStatusMessage() {return statusMessage; }

    public void setAddedToFinishedQ(int addedToFinishedQ) {
        this.addedToFinishedQ = addedToFinishedQ;
    }

    public void setCompletedEncryptions(long completedEncryptions) {
        this.completedEncryptions = completedEncryptions;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void setRemovedFromFinishedQ(int removedFromFinishedQ) {
        this.removedFromFinishedQ = removedFromFinishedQ;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setTotalEncryptions(long totalEncryptions) {
        this.totalEncryptions = totalEncryptions;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void incrementAddedToFinishedQ() {
        addedToFinishedQ++;
    }

    public void incrementTotalTime(long add) {
        totalTime += add;
    }

    public void incrementCompletedEncryptions(long add) {
        completedEncryptions += add;
    }

    public void incrementRemovedFromFinishedQ() {
        removedFromFinishedQ++;
    }

    public void incrementCompletedTasks() {
        completedTasks++;
    }

    // calculate Mean
    public synchronized double getMeanTime(){
        return totalTime / completedTasks;
    }
}
