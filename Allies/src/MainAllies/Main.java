package MainAllies;

import LoginAllies.LoginController;
import MainControllerAllies.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.InputStream;
import java.net.URL;

public class Main extends Application {
    public static final String APP_FXML_LIGHT_RESOURCE = "/MainControllerAllies/app.fxml";
    public static final String LOGIN_FXML = "/LoginAllies/login.fxml";

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        // load master app and controller from fxml
        URL mainFxml = getClass().getResource(APP_FXML_LIGHT_RESOURCE);
        fxmlLoader.setLocation(mainFxml);
        InputStream stream = mainFxml.openStream();
        ScrollPane root = fxmlLoader.load(stream);

        // Wire up controller
        MainController mainController = fxmlLoader.getController();
        mainController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Allies App");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // load login component and controller from fxml
        fxmlLoader = new FXMLLoader();
        URL loginFxml = getClass().getResource(LOGIN_FXML);
        fxmlLoader.setLocation(mainFxml);
        GridPane loginComponent = fxmlLoader.load(loginFxml.openStream());
        LoginController loginController = fxmlLoader.getController();

        // wire up controller
        loginController.setMainController(mainController);

        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginController.setStage(loginStage);
        Scene loginScene = new Scene(loginComponent, 200, 300);
        loginStage.setScene(loginScene);
        loginStage.show();

    }
}
