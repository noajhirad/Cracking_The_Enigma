package DTOs;

import java.util.ArrayList;
import java.util.List;

public class DTOBruteForceTasksBatch {
    private List<DTOBruteForceQueueItem> allTasks;
    private boolean isMissionOver;
    private String toDecrypt;

    public DTOBruteForceTasksBatch() {
        this.allTasks = new ArrayList<>();
        this.isMissionOver = false;
        this.toDecrypt = "";
    }

    public DTOBruteForceTasksBatch(List<DTOBruteForceQueueItem> allTasks, boolean bol, String toDecrypt){
        this.allTasks = allTasks;
        this.isMissionOver = bol;
        this.toDecrypt = toDecrypt;
    }

    public List<DTOBruteForceQueueItem> getAllTasks() {
        return allTasks;
    }

    public boolean isMissionOver() {
        return isMissionOver;
    }

    public void setMissionOver(boolean missionOver) {
        isMissionOver = missionOver;
    }

    public void setAllTasks(List<DTOBruteForceQueueItem> allTasks) {
        this.allTasks = allTasks;
    }

    public String getToDecrypt() {
        return toDecrypt;
    }
}
