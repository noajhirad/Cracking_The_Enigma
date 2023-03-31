package SecondPageAllies;

import UtilsAllies.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UtilsAllies.Constants.UBOAT_LEFT;
import static UtilsAllies.Constants.GSON_INSTANCE;

public class UboatLeftRefresher extends TimerTask {
    private final Consumer<String> onFail;
    private Runnable uboatLeft;
    private Boolean isUboatLeftActive = false;

    public UboatLeftRefresher(Runnable uboatLeft, Consumer<String> onFail) {
        this.uboatLeft = uboatLeft;
        this.onFail = onFail;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(UBOAT_LEFT, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        onFail.accept(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            onFail.accept(responseBody));
                } else {
                    String responseBody = response.body().string();
                    Boolean isUboatLeft = GSON_INSTANCE.fromJson(responseBody, Boolean.class);
                    if (isUboatLeft)
                        if(!isUboatLeftActive) {
                            Platform.runLater(() ->
                                    uboatLeft.run());
                            isUboatLeftActive = true;
                        }
                }
            }
        });
    }
}
