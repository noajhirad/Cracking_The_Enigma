package FirstPageUBoat.MachineDetails;

import FirstPageUBoat.FirstPageController;
import PropertiesUBoat.MachineInfoProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MachineDetailsController {

    @FXML private Label rotorsCountPH;
    @FXML private Label amountOfDefinedRotorsPH;
    @FXML private Label numOfReflectorsPH;
    @FXML private Label numOfMessagesPH;
    @FXML private Label originalCodeConfigPH;

    private FirstPageController firstPageController;

    public void setFirstPageController(FirstPageController firstPageController) {
        this.firstPageController = firstPageController;
    }

    public void bindLabels(MachineInfoProperty machineInfoProperty) {
        machineInfoProperty.bindRotorsCount(rotorsCountPH);
        machineInfoProperty.bindAmountOfDefinedRotors(amountOfDefinedRotorsPH);
        machineInfoProperty.bindReflectorsCount(numOfReflectorsPH);
        machineInfoProperty.bindNumOfProcessedMessages(numOfMessagesPH);
        machineInfoProperty.bindOriginalMachineConfig(originalCodeConfigPH);
    }
}
