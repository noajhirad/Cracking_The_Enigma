package MainControllerAgents.Progress;

import AgentBruteForce.AgentProgressInfo;
import MainControllerAgents.MainController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class AgentProgressController {

    @FXML private ProgressBar progressBar;
    @FXML private Label missionsInQueuePH;
    @FXML private Label missionsInProgressPH;
    @FXML private Label completedMissionsPH;
    @FXML private Label candidatesFoundPH;
    private final SimpleDoubleProperty prop = new SimpleDoubleProperty(0);
    private float totalMissions = 0;
    private float completedMissions = 0;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void bindLabelsToProgressProperty(AgentProgressInfo progressInfo){
        missionsInQueuePH.textProperty().bind(progressInfo.getMissionsInQueue().asString());
        missionsInProgressPH.textProperty().bind(progressInfo.getMissionsInProgress().asString());
        completedMissionsPH.textProperty().bind(progressInfo.getCompletedMissions().asString());
        candidatesFoundPH.textProperty().bind(progressInfo.getCandidateFound().asString());
        //prop.bind(Bindings.divide(progressInfo.getCompletedMissions(), progressInfo.getTotalMissions()));

        progressBar.progressProperty().bind(prop);
    }

    public void clear() {
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);

        missionsInProgressPH.textProperty().unbind();
        missionsInProgressPH.setText("");

        missionsInQueuePH.textProperty().unbind();
        missionsInQueuePH.setText("");

        completedMissionsPH.textProperty().unbind();
        completedMissionsPH.setText("");

        candidatesFoundPH.textProperty().unbind();
        candidatesFoundPH.setText("");
    }

    public void setCompletedMissionsProp(long size) {
        completedMissions  = size;
        if(totalMissions != 0)
            prop.set(completedMissions / totalMissions);
    }

    public void setTotalMissionsProp(long size) {
        totalMissions = size;
        if(totalMissions != 0)
            prop.set(completedMissions / totalMissions);
    }
}
