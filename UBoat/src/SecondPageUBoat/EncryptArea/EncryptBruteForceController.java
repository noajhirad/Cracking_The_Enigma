package SecondPageUBoat.EncryptArea;

import SecondPageUBoat.SecondPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class EncryptBruteForceController {
    private SecondPageController secondPageController;
    @FXML private TextArea inputField;
    @FXML private TextArea outputField;
    @FXML private Button proccessBtn;
    @FXML private Button resetBtn;
    @FXML private Button clearBtn;

    @FXML void clearBtnClicked(ActionEvent event) {
        inputField.clear();
        outputField.clear();
        secondPageController.disableReadyBtn();
    }

    @FXML void proccessBtnClicked(ActionEvent event) {
        try {
            secondPageController.encryptBruteForce(inputField.getText());
        }
        catch (Exception e){
            secondPageController.setErrorMessage(e.getMessage());
        }
    }

    public void updateOutputField(String output) {
        outputField.setText(output);
    }

    @FXML void resetBtnClicked(ActionEvent event) {
        try {
            inputField.clear();
            outputField.clear();
            secondPageController.resetMachineCode();
            secondPageController.disableReadyBtn();
        }
        catch(Exception e){
            secondPageController.setErrorMessage(e.getMessage());
        }
    }

    public void setSecondPageController(SecondPageController secondPageController) {
        this.secondPageController = secondPageController;
    }

    public void setChoosenDictionaryWord(String word) {
        if(inputField.getText().equals(""))
            inputField.setText(word);
        else
            inputField.setText(inputField.getText() + " " + word);
    }

    public void clearAllFields() {
        inputField.clear();
        outputField.clear();
    }

    public void clearContestData() {
        inputField.clear();
        outputField.clear();
    }
}
