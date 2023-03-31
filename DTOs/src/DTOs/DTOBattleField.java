package DTOs;

public class DTOBattleField {
    private String name;
    private int numOfAllies;
    private String difficulty;

    public DTOBattleField(String name, int numOfAllies, String difficulty) {
        this.name = name;
        this.numOfAllies = numOfAllies;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumOfAllies(int numOfAllies) {
        this.numOfAllies = numOfAllies;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
