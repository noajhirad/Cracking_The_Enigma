package FirstPageUBoat;

import ChatareaUBoat.ChatAreaController;
import DTOs.DTOMachineConfig;
import FirstPageUBoat.CodeCalibration.CodeCalibrationController;
import FirstPageUBoat.MachineDetails.MachineDetailsController;
import MainControllerUBoat.MainController;
import PropertiesUBoat.MachineInfoProperty;
import PropertiesUBoat.MachineInfoSupplier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;

public class FirstPageController {

    public static final String MACHINE_DETAILS_fXML_RESOURCE = "/FirstPageUBoat/MachineDetails/machineDetails.fxml";
    public static final String CODE_CALIBRATION_fXML_RESOURCE = "/FirstPageUBoat/CodeCalibration/codeCalibration.fxml";

    // UI Components
    @FXML private BorderPane chatComponent;

    @FXML private BorderPane codeCalibrationComponent;
    @FXML private BorderPane machineDetailsComponent;
    @FXML private SplitPane majorSplitPane;
    @FXML private SplitPane smallSplitPane;

    // Controllers
    private MainController mainController;
    private MachineDetailsController machineDetailsComponentController;
    private CodeCalibrationController codeCalibrationComponentController;
    private ChatAreaController chatAreaController;


    @FXML
    public void initialize() throws Exception {
        loadResources();
        if(machineDetailsComponentController != null && codeCalibrationComponentController != null) {
            machineDetailsComponentController.setFirstPageController(this);
            codeCalibrationComponentController.setFirstPageController(this);
        }
        setSplitPaneSizes();
    }

    private void setSplitPaneSizes() {
        majorSplitPane.setDividerPositions(0.3);
        smallSplitPane.setDividerPositions(0.7);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void loadResources() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        // load machine details controller & component from fxml
        URL url = getClass().getResource(MACHINE_DETAILS_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane machineDetails = fxmlLoader.load(url.openStream());
        machineDetailsComponentController = fxmlLoader.getController();
        machineDetailsComponent.setCenter(machineDetails);

        // load header controller & component from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CODE_CALIBRATION_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        BorderPane codeCalibration = fxmlLoader.load(url.openStream());
        codeCalibrationComponentController = fxmlLoader.getController();
        codeCalibrationComponent.setCenter(codeCalibration);
    }

    public void bindLabels(MachineInfoProperty machineInfoProperty) {
        machineDetailsComponentController.bindLabels(machineInfoProperty);
    }

    public void randomCodeBtnClicked() throws Exception{
        mainController.randomCodeBtnClicked();
    }

    public MachineInfoSupplier getMachineInfo() {
        return mainController.getMachineInfo();
    }

    public void buildCalibrationElement() {
        codeCalibrationComponentController.buildElement();
    }

    public void setManualCodeConfig(DTOMachineConfig configData) throws Exception {
        mainController.setManualCodeConfig(configData);
    }

    public void bindComponents() {
       bindLabels(mainController.getMachineInfoProperty());
    }

    public void setErrorMessage(String message){
        mainController.setErrorMessage(message);
    }

    public void setSuccessMessage(String message){
        mainController.setSuccessMessage(message);
    }

    public void setChatController(ChatAreaController chatAreaController) {
        this.chatAreaController = chatAreaController;
    }

    public void showChat(GridPane chatElement){
        chatComponent.setCenter(chatElement);
    }
}
