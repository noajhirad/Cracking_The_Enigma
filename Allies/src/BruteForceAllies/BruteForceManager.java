package BruteForceAllies;

import MainControllerAllies.MainController;
import UtilsAllies.Constants;
import UtilsAllies.HttpClientUtil;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import java.io.IOException;

public class BruteForceManager {
    private MainController mainController;

    public BruteForceManager(MainController mainController){
        this.mainController = mainController;
    }

    // create dm on server & start mission allocating
    public void startBruteForce() {
        String finalUrl = HttpUrl
                .parse(Constants.START_BRUTE_FORCE)
                .newBuilder()
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        mainController.setErrorMessage(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            mainController.setErrorMessage(responseBody));
                } else {
                    String responseBody = response.body().string();

                    Platform.runLater(() -> {
//                        System.out.println("brute force started!");
                    });
                }
            }
        });
    }
}
