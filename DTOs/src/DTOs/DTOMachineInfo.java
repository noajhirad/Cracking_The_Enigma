package DTOs;

import java.util.List;
import java.util.Set;

public class DTOMachineInfo {
    private int rotorsCount;
    private int amountOfDefinedRotors;
    private int reflectorsCount;
    private int numOfProcessedMessages;
    private List<Character> ABC = null;
    private DTOMachineConfig originalMachineConfig = null;
    private DTOMachineConfig currentMachineConfig = null;
    private Set<String> dictionary = null;

    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public int getAmountOfDefinedRotors() {
        return amountOfDefinedRotors;
    }

    public void setAmountOfDefinedRotors(int amountOfDefinedRotors) {
        this.amountOfDefinedRotors = amountOfDefinedRotors;
    }

    public DTOMachineConfig getOriginalMachineConfig() {
        return originalMachineConfig;
    }

    public void setOriginalMachineConfig(DTOMachineConfig machineConfig) {
        this.originalMachineConfig = machineConfig;
    }

    public int getNumOfProcessedMessages() {
        return numOfProcessedMessages;
    }

    public void setNumOfProcessedMessages(int numOfProcessedMessages) {
        this.numOfProcessedMessages = numOfProcessedMessages;
    }

    public int getReflectorsCount() {
        return reflectorsCount;
    }

    public void setReflectorsCount(int reflectorsCount) {
        this.reflectorsCount = reflectorsCount;
    }

    public DTOMachineConfig getCurrentMachineConfig() {
        return currentMachineConfig;
    }

    public void setCurrentMachineConfig(DTOMachineConfig currentMachineConfig) {
        this.currentMachineConfig = currentMachineConfig;
    }

    public List<Character> getABC() {
        return ABC;
    }

    public void setABC(List<Character> ABC) {
        this.ABC = ABC;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }
}
