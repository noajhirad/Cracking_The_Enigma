package PropertiesUBoat;

import DTOs.DTOMachineConfig;
import DTOs.DTOMachineInfo;
import MainUBoat.DTOParser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.HashSet;
import java.util.Set;

public class MachineInfoProperty implements MachineInfoSupplier{
    private SimpleIntegerProperty rotorsCount;
    private SimpleIntegerProperty amountOfDefinedRotors;
    private SimpleIntegerProperty reflectorsCount;
    private SimpleIntegerProperty numOfProcessedMessages;
    private SimpleStringProperty originalMachineConfig;
    private SimpleListProperty<Character> ABC;
    private Set<String> dictionary;

    public MachineInfoProperty() {
        rotorsCount = new SimpleIntegerProperty();
        amountOfDefinedRotors = new SimpleIntegerProperty();
        reflectorsCount = new SimpleIntegerProperty();
        numOfProcessedMessages = new SimpleIntegerProperty();
        originalMachineConfig = new SimpleStringProperty();
        ABC = new SimpleListProperty<>();
        dictionary = new HashSet<>();
    }

    public void setAllInfo(DTOMachineInfo info) {
        rotorsCount.set(info.getRotorsCount());
        amountOfDefinedRotors.set(info.getAmountOfDefinedRotors());
        reflectorsCount.set(info.getReflectorsCount());
        numOfProcessedMessages.set(info.getNumOfProcessedMessages());
        ABC.set(FXCollections.observableArrayList(info.getABC()));
        dictionary = info.getDictionary();
    }

    public void bindRotorsCount(Label label) {
        label.textProperty().bind(rotorsCount.asString());
    }

    public void bindAmountOfDefinedRotors(Label label) {
        label.textProperty().bind(amountOfDefinedRotors.asString());
    }

    public void bindReflectorsCount(Label label) {
        label.textProperty().bind(reflectorsCount.asString());
    }

    public void bindNumOfProcessedMessages(Label label) {
        label.textProperty().bind(numOfProcessedMessages.asString());
    }

    public void bindOriginalMachineConfig(Label label) {
        label.textProperty().bind(originalMachineConfig);
    }

    public void setOriginalCodeConfigProperty(DTOMachineConfig config) {
        originalMachineConfig.set(DTOParser.parseDTOMachineConfigToString(config));
    }

    public int getRotorsCount() {
        return rotorsCount.get();
    }

    public int getAmountOfDefinedRotors() {
        return amountOfDefinedRotors.get();
    }

    public int getReflectorsCount() {
        return reflectorsCount.get();
    }

    public int getNumOfProcessedMessages() {
        return numOfProcessedMessages.get();
    }

    public ObservableList<Character> getABC() { return ABC.get(); }

    public void clearConfig() {
        originalMachineConfig.set("");
    }

    public Set<String> getDictionary() {
        return dictionary;
    }
}
