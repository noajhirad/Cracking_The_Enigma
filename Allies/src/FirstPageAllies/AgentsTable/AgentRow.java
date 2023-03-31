package FirstPageAllies.AgentsTable;

import DTOs.DTOAgentRow;

public class AgentRow {

    private String name;
    private String threadsNumber;
    private String taskSize;

    public AgentRow(String name, String threadsNumber, String taskSize){
        this.name = name;
        this.taskSize = taskSize;
        this.threadsNumber = threadsNumber;
    }

    public AgentRow(DTOAgentRow dtoAgentRow){
        this.name = dtoAgentRow.getName();
        this.taskSize = dtoAgentRow.getTaskSize();
        this.threadsNumber = dtoAgentRow.getThreadsNumber();
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

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskSize(String taskSize) {
        this.taskSize = taskSize;
    }

    public void setThreadsNumber(String threadsNumber) {
        this.threadsNumber = threadsNumber;
    }
}
