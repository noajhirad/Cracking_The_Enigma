package DTOs;

public class DTOTeamRow {
    private String name;
    private String agents;
    private String taskSize;

    public DTOTeamRow(String name, String agents, String taskSize){
        this.name = name;
        this.agents = agents;
        this.taskSize = taskSize;
    }

    public String getTaskSize() {
        return taskSize;
    }

    public String getName() {
        return name;
    }

    public String getAgents() {
        return agents;
    }
}
