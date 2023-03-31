package BruteForce;

public enum BruteForceDifficulty {
    EASY("Easy", 1),
    MEDIUM("Medium", 5),
    HARD("Hard", 10),
    IMPOSSIBLE("Impossible", 20);

    private String stringValue;
    private int maxResultsAmount;

    BruteForceDifficulty(String str, int maxResultsAmount){
        this.maxResultsAmount = maxResultsAmount;
        stringValue = str;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static BruteForceDifficulty convertStringToValue(String s) {
        s = s.toUpperCase();
        switch (s) {
            case "EASY":
                return BruteForceDifficulty.EASY;
            case "MEDIUM":
                return BruteForceDifficulty.MEDIUM;
            case "HARD":
                return BruteForceDifficulty.HARD;
            case "IMPOSSIBLE":
                return BruteForceDifficulty.IMPOSSIBLE;
            default:
                return null;
        }
    }

    public int getMaxResultsAmount() {
        return maxResultsAmount;
    }
}
