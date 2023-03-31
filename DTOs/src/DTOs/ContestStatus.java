package DTOs;

public enum ContestStatus {
    WAITING("waiting"),
    ACTIVE("active"),
    FINISHED("finished");

    private String str;

    ContestStatus(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
