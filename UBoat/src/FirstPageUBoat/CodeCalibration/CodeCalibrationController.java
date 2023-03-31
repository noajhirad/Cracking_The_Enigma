package FirstPageUBoat.CodeCalibration;

import DTOs.DTOMachineConfig;
import Exceptions.ConfigException;
import FirstPageUBoat.FirstPageController;
import MainUBoat.DTOParser;
import PropertiesUBoat.MachineInfoSupplier;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

public class CodeCalibrationController {

    @FXML private Button randomCodeBtn;
    @FXML private Button setCodeBtn;
    @FXML private ChoiceBox<String> reflectorCB;
    @FXML private HBox rotorsNumbers;
    @FXML private HBox rotorsPositions;
    private FirstPageController firstPageController;
    private List<ChoiceBox<Character>> rotorsPositionsChoiceBoxList;
    private List<ChoiceBox<Integer>> usedRotorsChoiceBoxList;

    DTOMachineConfig data = new DTOMachineConfig();


    public CodeCalibrationController(){
        rotorsPositionsChoiceBoxList = new ArrayList<>();
        usedRotorsChoiceBoxList = new ArrayList<>();
    }

    @FXML
    public void initialize() { }

    @FXML
    public void randomCodeBtnClicked(ActionEvent event) {
        try {
            firstPageController.randomCodeBtnClicked();
            clearSelections();
            firstPageController.setSuccessMessage("Machine code was successfully set.");
        }
        catch(Exception e){
            firstPageController.setErrorMessage(e.getMessage());
        }
    }

    @FXML
    public void setCodeBtnClicked(ActionEvent event) {
        data = new DTOMachineConfig();

        try {
            // choicebox reflector & rotors pos & used rotors -> collect data
            collectData();
            firstPageController.setManualCodeConfig(data);
            firstPageController.setSuccessMessage("Machine code was successfully set.");
        }
        catch (Exception e) {
            firstPageController.setErrorMessage(e.getMessage());
        }
        clearSelections();
    }

    private void clearSelections(){
        for(ChoiceBox<Integer> usedRotorCB : usedRotorsChoiceBoxList)
            usedRotorCB.setValue(null);

        // rotors positions
        for(ChoiceBox<Character> rotorPosCB : rotorsPositionsChoiceBoxList)
            rotorPosCB.setValue(null);

        // refletor
        reflectorCB.setValue(null);
    }

    private void collectData() throws ConfigException {
        List<Integer> usedRotors = new ArrayList<>();
        String rotorsPos = "";

        // used rotors
        for(ChoiceBox<Integer> usedRotorCB : usedRotorsChoiceBoxList) {
            if(usedRotorCB.getValue()!= null)
                usedRotors.add(usedRotorCB.getValue());
            else
                throw new ConfigException(ConfigException.ErrorType.EMPTY_SELECTION);
        }
        data.setUsedRotor(usedRotors);

        // rotors positions
        for(ChoiceBox<Character> rotorPosCB : rotorsPositionsChoiceBoxList) {
            if(rotorPosCB.getValue() != null)
                data.setRotorsPosition(rotorPosCB.getValue());
            else throw new ConfigException((ConfigException.ErrorType.EMPTY_SELECTION));
        }

        // refletor
        if(reflectorCB.getValue() == null)
            throw new ConfigException((ConfigException.ErrorType.EMPTY_SELECTION));
        int reflector = DTOParser.getIntByString(reflectorCB.getValue());
        data.setReflectorNumber(reflector);
    }

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    public void buildElement(){

        // clear all lists & remove elements
        destroyElement();

        // get machineDetails
        MachineInfoSupplier machineInfo = firstPageController.getMachineInfo();
        int rotorsCount = machineInfo.getRotorsCount();

        // selected rotors choicebox
        buildRotorsNumbersVB(rotorsCount,machineInfo.getAmountOfDefinedRotors());

        // rotors positions choicebox
        List<Character>ABC = machineInfo.getABC();
        buildRotorsPositionsVB(rotorsCount, ABC);

        // reflector
        int reflectorCount = machineInfo.getReflectorsCount();
        buildReflectorCB(reflectorCount);
    }

    private void buildReflectorCB(int reflectorCount){
        for (int i = 1; i <=reflectorCount ; i++) {
            reflectorCB.getItems().add(DTOParser.getIDStringByValue(i));
        }
    }
    private void destroyElement() {
        usedRotorsChoiceBoxList.clear();
        rotorsNumbers.getChildren().clear();

        rotorsPositionsChoiceBoxList.clear();
        rotorsPositions.getChildren().clear();

        reflectorCB.getItems().clear();
    }

    private void buildRotorsNumbersVB(int rotorsCount, int definedRotors){
        List<Integer> values = new ArrayList();
        for (int i = 1; i <= definedRotors; i++)
            values.add(i);

        ObservableList<Integer> vals = new ObservableListWrapper<>(values);
        for (int i = 0; i<rotorsCount ; i++) {
            ChoiceBox<Integer> current = new ChoiceBox<>(vals);
            usedRotorsChoiceBoxList.add(current);
            rotorsNumbers.getChildren().add(current);
        }
    }

    private void buildRotorsPositionsVB(int rotorsCount, List<Character>ABC){
        ObservableList<Character> vals = new ObservableListWrapper<>(ABC);
        for (int i = 0; i<rotorsCount ; i++) {
            ChoiceBox<Character> current = new ChoiceBox<>(vals);
            rotorsPositionsChoiceBoxList.add(current);
            rotorsPositions.getChildren().add(current);
        }
    }
}
