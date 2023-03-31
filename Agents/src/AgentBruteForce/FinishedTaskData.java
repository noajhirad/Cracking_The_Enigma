package AgentBruteForce;

import DTOs.DTOBruteForceResult;

import java.util.List;

public class FinishedTaskData {
    private int taskSize;
    private long time;
    private List<DTOBruteForceResult> allCandidates;

    public FinishedTaskData(int taskSize, long time, List<DTOBruteForceResult> allCandidates) {
        this.taskSize = taskSize;
        this.time = time;
        this.allCandidates = allCandidates;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public long getTime() {
        return time;
    }

    public List<DTOBruteForceResult> getAllCandidates() {
        return allCandidates;
    }
}
