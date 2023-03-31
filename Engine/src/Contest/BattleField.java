package Contest;

import BruteForce.BruteForceDifficulty;
import DTOs.DTOBattleField;

public class BattleField {
    private String name;
    private int numOfAllies;
    private BruteForceDifficulty difficulty;

    public BattleField(){}

    public BattleField(DTOBattleField dtoBattleField) {
        this.name = dtoBattleField.getName();
        this.numOfAllies = dtoBattleField.getNumOfAllies();
        this.difficulty = BruteForceDifficulty.convertStringToValue(dtoBattleField.getDifficulty());
    }

    public BruteForceDifficulty getDifficulty() {
        return difficulty;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(BruteForceDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setNumOfAllies(int numOfAllies) {
        this.numOfAllies = numOfAllies;
    }
}
