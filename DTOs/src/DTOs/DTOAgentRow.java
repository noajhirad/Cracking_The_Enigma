package DTOs;

public class DTOAgentRow {
    private String name;
    private String threadsNumber;
    private String taskSize;

    public DTOAgentRow(String name, String threadsNumber, String taskSize){
        this.name = name;
        this.taskSize = taskSize;
        this.threadsNumber = threadsNumber;
    }

    public String getName() {
        return name;
    }

    public String getTaskSize() {
        return taskSize;
    }

    public String getThreadsNumber() {
        return threadsNumber;
    }
}
