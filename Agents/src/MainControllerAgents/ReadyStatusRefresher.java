package MainControllerAgents;

import UtilsAgent.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UtilsAgent.Constants.GSON_INSTANCE;
import static UtilsAgent.Constants.READY_STATUS;

public class ReadyStatusRefresher extends TimerTask {
    private final Runnable onReady;
    private final Consumer<String> onFail;

    public ReadyStatusRefresher(Runnable onReady, Consumer<String> onFail) {
        this.onReady = onReady;
        this.onFail = onFail;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(READY_STATUS, new Callback() {

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
                }
                else {
                    String responseBody = response.body().string();
                    Boolean isReady = GSON_INSTANCE.fromJson(responseBody, Boolean.class);
                    if (isReady) {
                        Platform.runLater(() ->
                                onReady.run());
                    }
                }
            }
        });
    }
}

