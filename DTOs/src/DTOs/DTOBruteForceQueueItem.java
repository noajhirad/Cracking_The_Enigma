package DTOs;

public class DTOBruteForceQueueItem {

    private int taskSize;
    private int startingPoint;
    private DTOMachineConfig dtoMachineConfig;
    private String toDecrypt;

    public DTOBruteForceQueueItem(int taskSize, int startingPoint, String toDecrypt) {
        this.startingPoint = startingPoint;
        this.taskSize = taskSize;
        this.toDecrypt = toDecrypt;
        dtoMachineConfig = null;
    }

    public DTOBruteForceQueueItem(int taskSize, int startingPoint, DTOMachineConfig dtoMachineConfig, String toDecrypt) {
        this.startingPoint = startingPoint;
        this.taskSize = taskSize;
        this.dtoMachineConfig = dtoMachineConfig;
        this.toDecrypt = toDecrypt;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public int getStartingPoint() {
        return startingPoint;
    }

    public DTOMachineConfig getDtoMachineConfig() {
        return dtoMachineConfig;
    }

    public String getToDecrypt() {
        return toDecrypt;
    }
}
