package BruteForce;

public class FinishedTaskData {
    private int taskSize;
    private long time;

    public FinishedTaskData(int taskSize, long time) {
        this.taskSize = taskSize;
        this.time = time;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public long getTime() {
        return time;
    }
}
