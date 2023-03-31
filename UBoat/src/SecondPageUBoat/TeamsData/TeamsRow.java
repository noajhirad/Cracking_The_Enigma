package SecondPageUBoat.TeamsData;

import DTOs.DTOTeamRow;

public class TeamsRow {
    private String name;
    private String agents;
    private String taskSize;

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskSize(String taskSize) {
        this.taskSize = taskSize;
    }

    public String getAgents() {
        return agents;
    }

    public String getName() {
        return name;
    }

    public String getTaskSize() {
        return taskSize;
    }

    public TeamsRow(String name, String taskSize, String agents){
        this.name = name;
        this.taskSize = taskSize;
        this.agents = agents;
    }

    public TeamsRow(DTOTeamRow dtoTeamRow){
        this.name = dtoTeamRow.getName();
        this.taskSize = dtoTeamRow.getTaskSize();
        this.agents = dtoTeamRow.getAgents();
    }
}
