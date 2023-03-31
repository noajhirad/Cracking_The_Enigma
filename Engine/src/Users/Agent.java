package Users;

import DTOs.DTOProgressInfo;

public class Agent {
    private String userName;
    private String threadsNumber;
    private String taskSize;
    private DTOProgressInfo progressInfo = null;

    public Agent(String name){
        this.userName = name;
    }

    public Agent(String name, String taskSize, String threadsNumber){
        this.userName = name;
        this.taskSize = taskSize;
        this.threadsNumber = threadsNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getTaskSize() {
        return taskSize;
    }

    public String getThreadsNumber() {
        return threadsNumber;
    }

    public DTOProgressInfo getProgressInfo() {
        return progressInfo;
    }

    public void setProgressInfo(DTOProgressInfo progressInfo) {
        this.progressInfo = progressInfo;
    }
}
