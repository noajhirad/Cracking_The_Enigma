package SecondPageUBoat;

import DTOs.DTOCandidatesAndVersion;
import UtilsUBoat.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UtilsUBoat.Constants.*;

public class CandidatesRefresher extends TimerTask {

    Consumer<String> onFail;
    Consumer<DTOCandidatesAndVersion> onResp;

    public CandidatesRefresher(Consumer<DTOCandidatesAndVersion> onResp, Consumer<String> onFail) {
        this.onResp = onResp;
        this.onFail = onFail;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(ALL_CANDIDATES, new Callback() {

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
                    DTOCandidatesAndVersion candidatesAndVersion = GSON_INSTANCE.fromJson(responseBody, DTOCandidatesAndVersion.class);
                    Platform.runLater(() ->
                            onResp.accept(candidatesAndVersion));
                }
            }
        });
    }
}
