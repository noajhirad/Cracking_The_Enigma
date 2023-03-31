package PropertiesUBoat;

import DTOs.DTOMachineConfig;
import MainUBoat.DTOParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;

import java.util.Map;

public class MachineConfigProperty {

    private SimpleStringProperty reflectorNumber;
    private SimpleStringProperty usedRotors;
    private SimpleStringProperty notches;
    private SimpleStringProperty rotorsStartingPoints;
    private SimpleStringProperty plugBoard;
    private SimpleStringProperty formattedString;

    public MachineConfigProperty(){
        reflectorNumber = new SimpleStringProperty();
        usedRotors = new SimpleStringProperty();
        notches = new SimpleStringProperty();
        rotorsStartingPoints = new SimpleStringProperty();
        plugBoard = new SimpleStringProperty();
        formattedString = new SimpleStringProperty();
    }

    public void setAll(DTOMachineConfig config) {
        Map<String,String> parsedConfig = DTOParser.parseDTOMachineConfig(config);
        reflectorNumber.set(parsedConfig.get("reflector ID"));
        usedRotors.set(parsedConfig.get("used rotors"));
        notches.set(parsedConfig.get("nothces pos"));
        rotorsStartingPoints.set(parsedConfig.get("rotors starting pos"));
        plugBoard.set(parsedConfig.get("plugs"));
        formattedString.set(DTOParser.parseDTOMachineConfigToString(config));
    }

    public void bindReflectorNumber(Label label) {
        label.textProperty().bind(reflectorNumber);
    }

    public void bindUsedRotors(Label label) {
        label.textProperty().bind(usedRotors);
    }

    public void bindNotches(Label label) {
        label.textProperty().bind(notches);
    }

    public void bindRotorsStartingPoints(Label label) {
        label.textProperty().bind(rotorsStartingPoints);
    }

    public void bindPlugBoard(Label label) {
        label.textProperty().bind(plugBoard);
    }

    public void bindFormattedString(Label label) {
        label.textProperty().bind(formattedString);
    }

    public void clear() {
        reflectorNumber.set("");
        usedRotors.set("");
        notches.set("");
        rotorsStartingPoints.set("");
        plugBoard.set("");
        formattedString.set("");
    }

    public SimpleStringProperty getFormattedString() {
        return formattedString;
    }
}
