package DTOs;

import java.io.Serializable;
import java.util.*;

public class DTOMachineConfig implements Serializable {

    private List<Integer> usedRotors;
    private List<Integer> notches;
    private List<Character> rotorsStartingPoints = new ArrayList<>();
    private int reflectorNumber;

    public void setUsedRotor(List<Integer> usedRotors) { this.usedRotors = usedRotors; }

    public void setRotorsPosition(Character startingPoint) { rotorsStartingPoints.add(startingPoint); }

    public void setReflectorNumber(int reflectorNumber) { this.reflectorNumber = reflectorNumber;}

    public void setNotches(List<Integer> notches) {
        this.notches = notches;
    }

    public List<Integer> getNotches() {
        return notches;
    }

    public int getReflectorNumber() {
        return reflectorNumber;
    }

    public List<Character> getRotorsStartingPoints() {
        return rotorsStartingPoints;
    }

    public List<Integer> getUsedRotors() {
        return usedRotors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOMachineConfig that = (DTOMachineConfig) o;
        return reflectorNumber == that.reflectorNumber && usedRotors.equals(that.usedRotors) &&
                rotorsStartingPoints.equals(that.rotorsStartingPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usedRotors, rotorsStartingPoints, reflectorNumber);
    }
}
