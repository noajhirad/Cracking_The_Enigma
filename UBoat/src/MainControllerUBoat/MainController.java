package MainControllerUBoat;

import ChatareaUBoat.ChatAreaController;
import DTOs.DTOMachineConfig;
import DTOs.DTOMachineInfo;
import FirstPageUBoat.FirstPageController;
import MainControllerUBoat.Header.HeaderController;
import MainControllerUBoat.Intro.IntroController;
import MainUBoat.Main;
import PropertiesUBoat.MachineConfigProperty;
import PropertiesUBoat.MachineInfoProperty;
import PropertiesUBoat.MachineInfoSupplier;
import SecondPageUBoat.SecondPageController;
import SecondPageUBoat.TeamsData.TeamsController;
import SecondPageUBoat.TeamsData.TeamsTableRefresher;
import UtilsUBoat.Constants;
import UtilsUBoat.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import DTOs.*;

import static UtilsUBoat.Constants.MARK_AS_READY;
import static UtilsUBoat.Constants.REFRESH_RATE;
import static UtilsUBoat.Constants.DELAY_RATE;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.Timer;


public class MainController {
    public static final String HEADER_fXML_RESOURCE = "/MainControllerUBoat/Header/header.fxml";
    public static final String FIRST_PAGE_FXML_RESOURCE = "/FirstPageUBoat/firstCenter1.fxml";
    public static final String SECOND_PAGE_FXML_RESOURCE = "/SecondPageUBoat/secondPageUBoat.fxml";
    private static final String INTRO_PAGE_FXML_RESOURCE = "/MainControllerUBoat/Intro/IntroPage.fxml";
    private final static String CHATROOM_fxml = "/ChatareaUBoat/chat-area.fxml";

    // controllers
    @FXML private HeaderController headerComponentController;
    @FXML private FirstPageController firstPageComponentController;
    @FXML private SecondPageController secondPageComponentController;
    @FXML private IntroController introController;
    private TeamsController teamsComponentController;
    private ChatAreaController chatAreaController;


    // UI components
    @FXML private BorderPane bodyComponent;
    @FXML private ScrollPane headerComponent;

    private Stage primaryStage;
    private Stage loginStage;
    private GridPane firstPane;
    private GridPane secondPane;
    private GridPane introPane;
    private GridPane chatComponent;

    // Properties
    private SimpleBooleanProperty isFileLoaded;
    private SimpleBooleanProperty isConfigurated;
    private SimpleStringProperty xmlPathProperty;
    private MachineInfoProperty machineInfoProperty;
    private MachineConfigProperty currentMachineConfigProperty;
    private SkinsOptions skin;
    private TeamsTableRefresher teamsTableRefresher;
    private Timer timer;
    private Main main;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        isConfigurated = new SimpleBooleanProperty(false);
        xmlPathProperty = new SimpleStringProperty();
        machineInfoProperty = new MachineInfoProperty();
        currentMachineConfigProperty = new MachineConfigProperty();
        skin = SkinsOptions.DEFAULT;
    }

    @FXML
    public void initialize() throws Exception {
        loadResources();
        if (headerComponentController != null && firstPageComponentController != null &&
                secondPageComponentController != null) {

            headerComponentController.setMainController(this);
            firstPageComponentController.setMainController(this);
            firstPageComponentController.setChatController(chatAreaController);
            secondPageComponentController.setMainController(this);
            secondPageComponentController.setChatController(chatAreaController);

            //bind all components
            this.bindComponents();
            headerComponentController.bindComponents(xmlPathProperty);
            firstPageComponentController.bindComponents();
            secondPageComponentController.bindComponents(xmlPathProperty);

            teamsComponentController = secondPageComponentController.getTeamsController();
        }
    }

    private void loadResources() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();

        // load header controller & component from fxml
        URL url = getClass().getResource(HEADER_fXML_RESOURCE);
        fxmlLoader.setLocation(url);
        headerComponent = fxmlLoader.load(url.openStream());
        headerComponentController = fxmlLoader.getController();
        bodyComponent.setTop(headerComponent);

        // load intro page component from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(INTRO_PAGE_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        introPane = fxmlLoader.load(url.openStream());
        introController = fxmlLoader.getController();

        // load first page component & controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(FIRST_PAGE_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        firstPane = fxmlLoader.load(url.openStream());
        firstPageComponentController = fxmlLoader.getController();

        // load second page component & controller from fxml
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(SECOND_PAGE_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        secondPane = fxmlLoader.load(url.openStream());
        secondPageComponentController = fxmlLoader.getController();

        // chatRoom
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource(CHATROOM_fxml);
        fxmlLoader.setLocation(url);
        chatComponent = fxmlLoader.load(url.openStream());
        chatAreaController = fxmlLoader.getController();

        bodyComponent.setCenter(introPane);
    }

    // show pages
    public void showFirstPage() {
        bodyComponent.setCenter(firstPane);
        firstPageComponentController.showChat(chatComponent);
    }

    public void showSecondPage() {
        bodyComponent.setCenter(secondPane);
        secondPageComponentController.showChat(chatComponent);

        // update teams table
        teamsTableRefresher = new TeamsTableRefresher(
                teamsComponentController::buildElement,
                headerComponentController::setErrorMessage);

        timer = new Timer();
        timer.schedule(teamsTableRefresher, DELAY_RATE, REFRESH_RATE);
    }

    public void showPrimaryStage() {
        primaryStage.show();
        chatAreaController.startListRefresher();
    }


    // binding methods
    private void bindClearMachineToXMLPath(){
        xmlPathProperty.addListener(e ->{
            firstPageComponentController.buildCalibrationElement();
            secondPageComponentController.clearAllFields();
            machineInfoProperty.clearConfig();
            currentMachineConfigProperty.clear();
        });
    }

    private void bindToIsFileLoaded() {
        isFileLoaded.addListener(e->{
            showFirstPage();
            headerComponentController.setMachineBtnSelected();
        });
    }

    private void bindComponents(){
        bindClearMachineToXMLPath();
        bindToIsFileLoaded();
        bindSecondPageToIsConfigurated();
    }

    private void bindSecondPageToIsConfigurated() {
        isConfigurated.addListener(e ->{
            secondPageComponentController.clearAllFields();
        });
    }

    // skins methods
    public void setSkin(SkinsOptions skin){
        Scene scene = primaryStage.getScene();
        this.skin = skin;
        scene.getStylesheets().clear();
        introController.setIntroImage(skin.getIntroImagePath());

        if(skin != SkinsOptions.DEFAULT)
            scene.getStylesheets().add(skin.getCSS());
    }

    public SkinsOptions getSkin() { return skin; }

    public Color getSkinColor() { return skin.getBtnColor(); }

    // Feedback methods
    public void setErrorMessage(String message){
        headerComponentController.setErrorMessage(message);
    }

    public void setSuccessMessage(String message){
        headerComponentController.setSuccessMessage(message);
    }

    public MachineInfoProperty getMachineInfoProperty() {
        return machineInfoProperty;
    }

    public MachineConfigProperty getCurrentMachineConfigProperty() {
        return currentMachineConfigProperty;
    }

    public SimpleBooleanProperty getIsFileLoadedProperty() {
        return isFileLoaded;
    }

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    // Engine & Server operations
    public void loadXML() throws Exception {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Machine File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null) { // user closed file choosing dialog
            setErrorMessage("File not found.");
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("text/xml")))
                .build();

        Request request = new Request.Builder()
                .url(Constants.LOAD_XML_PAGE)
                .post(requestBody)
                .build();

        HttpClientUtil.runPostAsync(request, new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                setErrorMessage(e.getMessage());
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        DTOMachineInfo machineInfo = Constants.GSON_INSTANCE.fromJson(responseBody, DTOMachineInfo.class);
                        machineInfoProperty.setAllInfo(machineInfo);
                        xmlPathProperty.set(file.getAbsolutePath());
                        isFileLoaded.set(false);
                        isFileLoaded.set(true);
                        isConfigurated.set(false);
                        headerComponentController.disableLoadBtn();
                        setSuccessMessage("File loaded successfully.");
                    });
                }
            }
        });
    }

    public void randomCodeBtnClicked() throws Exception{

        String finalUrl = HttpUrl
                .parse(Constants.RANDOM_CODE_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    DTOMachineConfig config = Constants.GSON_INSTANCE.fromJson(responseBody, DTOMachineConfig.class);
                    Platform.runLater(() -> {
                        isConfigurated.set(false);
                        isConfigurated.set(true);
                        currentMachineConfigProperty.setAll(config);
                        machineInfoProperty.setOriginalCodeConfigProperty(config);
                    });
                }
            }
        });
    }

    public MachineInfoSupplier getMachineInfo() {
        return machineInfoProperty;
    }

    public void setManualCodeConfig(DTOMachineConfig configData) throws Exception{

        String json = Constants.GSON_INSTANCE.toJson(configData);

        RequestBody body = RequestBody.create(json, Constants.JSON);

        Request request = new Request.Builder()
                .url(Constants.MANUAL_CODE_PAGE)
                .post(body)
                .build();


        HttpClientUtil.runPostAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    DTOMachineConfig config = Constants.GSON_INSTANCE.fromJson(responseBody, DTOMachineConfig.class);
                    Platform.runLater(() -> {
                        isConfigurated.set(false);
                        isConfigurated.set(true);
                        machineInfoProperty.setOriginalCodeConfigProperty(config);
                        currentMachineConfigProperty.setAll(config);
                    });
                }
            }
        });
    }

    public void resetCode() throws Exception{

        String finalUrl = HttpUrl
                .parse(Constants.RESET_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    DTOMachineConfig config = Constants.GSON_INSTANCE.fromJson(responseBody, DTOMachineConfig.class);
                    Platform.runLater(() -> {
                        currentMachineConfigProperty.setAll(config);
                    });
                }
            }
        });
    }

    public SimpleBooleanProperty getIsConfiguratedProperty() {
        return isConfigurated;
    }

    public void encryptBruteForce(String input) throws Exception {

        RequestBody body = RequestBody.create(input, Constants.STRING);

        Request request = new Request.Builder()
                .url(Constants.UBOAT_ENCRYPT_PAGE)
                .post(body)
                .build();


        HttpClientUtil.runPostAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    DTOEncryptResponse encryptResponse = Constants.GSON_INSTANCE.fromJson(responseBody, DTOEncryptResponse.class);
                    Platform.runLater(() -> {
                        secondPageComponentController.updateOutputField(encryptResponse.getOutput());
                        secondPageComponentController.enableReadyBtn();
                        currentMachineConfigProperty.setAll(encryptResponse.getMachineConfig());
                    });
                }
            }
        });
    }

    public Set<String> getDictionary() {
        return machineInfoProperty.getDictionary();
    }

    public void updateUserName(String userName) {
    }

    public void markAsReady() {

        String finalUrl = HttpUrl
                .parse(MARK_AS_READY)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    Platform.runLater(() -> {
                        secondPageComponentController.disableReadyBtn();
                    });
                }
            }
        });
    }

    public void setWinningTeam(String winningTeam) {
        headerComponentController.setWinningTeam(winningTeam);
    }

    public void setCompetitionStarted() {
        headerComponentController.setContestStarted();
    }

    public void clearContestData() {
        secondPageComponentController.clearContestData();

        String finalUrl = HttpUrl
                .parse(Constants.CLEAR_CONTEST_DATA)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                }
            }
        });
    }

    public void logout() {
        // cancel all refreshers
        teamsTableRefresher.cancel();
        secondPageComponentController.cancelTimer();

        // go to server: delete contest, uboat etc
        String finalUrl = HttpUrl
                .parse(Constants.LOGOUT)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        // close window of primary stage
                        primaryStage.close();
                        // show login stage
                        try {
                            main.loadPrimaryAppRecource(new Stage());
                            loginStage.show();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
