package PropertiesUBoat;

import javafx.collections.ObservableList;

public interface MachineInfoSupplier {
    public int getRotorsCount();
    public int getAmountOfDefinedRotors();
    public int getReflectorsCount();
    public int getNumOfProcessedMessages();
    public ObservableList<Character> getABC();
}
