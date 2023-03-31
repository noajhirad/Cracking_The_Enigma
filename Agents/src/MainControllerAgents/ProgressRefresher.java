package MainControllerAgents;

import DTOs.DTOProgressInfo;
import UtilsAgent.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static UtilsAgent.Constants.*;

public class ProgressRefresher extends TimerTask {

    private final Consumer<String> onFail;
    private final Supplier<DTOProgressInfo> getProgress;

    public ProgressRefresher(Consumer<String> onFail, Supplier<DTOProgressInfo> getProgress) {
        this.onFail = onFail;
        this.getProgress = getProgress;
    }

    @Override
    public void run() {

        String json = GSON_INSTANCE.toJson(getProgress.get());
//        System.out.println("progress in agent ProgressRefresher: " + json);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(UPDATE_PROGRESS)
                .post(body)
                .build();

        HttpClientUtil.runPostAsync(request, new Callback() {
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
                String jsonListOfContestsRows = response.body().string();
            }
        });
    }
}
