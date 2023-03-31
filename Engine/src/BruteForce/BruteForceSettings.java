package BruteForce;

public class BruteForceSettings {
    private String toDecrypt;
    private int agentsNumber;
    private int taskSize;
    private BruteForceDifficulty difficulty;
//    private Consumer<List<BruteForceResult>> whenResFound;

    public BruteForceSettings(BruteForceDifficulty difficulty, String toDecrypt) {
        this.difficulty = difficulty;
        this.toDecrypt = toDecrypt;
    }

    public BruteForceSettings(String toDecrypt, int agentsNumber, int taskSize, BruteForceDifficulty difficulty){
        this.toDecrypt = toDecrypt;
        this.agentsNumber = agentsNumber;
        this.taskSize = taskSize;
        this.difficulty = difficulty;
        //this.whenResFound = whenResFound;
    }

    public int getAgentsNumber() {
        return agentsNumber;
    }

    public BruteForceDifficulty getDifficulty() {
        return difficulty;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getToDecrypt() {
        return toDecrypt;
    }

    public void setAgentsNumber(int agentsNumber) {
        this.agentsNumber = agentsNumber;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    //    public Consumer<List<BruteForceResult>> getWhenResFound() {
//        return whenResFound;
//    }
//
//    public void setWhenResultFound(Consumer<List<BruteForceResult>> whenResultFound) {
//        this.whenResFound = whenResultFound;
//    }
}
