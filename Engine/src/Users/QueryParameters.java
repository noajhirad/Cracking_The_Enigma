package Users;

public class QueryParameters {

    String taskSize;
    String alliesTeam;
    String threadsNum;

    public QueryParameters(String taskSize, String alliesTeam, String threadsNum){
        this.alliesTeam = alliesTeam;
        this.threadsNum = threadsNum;
        this.taskSize = taskSize;
    }

    public String getTaskSize() {
        return taskSize;
    }

    public String getAlliesTeam() {
        return alliesTeam;
    }

    public String getThreadsNum() {
        return threadsNum;
    }
}
