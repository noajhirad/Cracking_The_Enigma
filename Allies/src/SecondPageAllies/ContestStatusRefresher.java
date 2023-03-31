package SecondPageAllies;

import DTOs.ContestStatus;
import DTOs.DTOContestStatus;
import UtilsAllies.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;
import static UtilsAllies.Constants.CONTEST_STATUS;
import static UtilsAllies.Constants.GSON_INSTANCE;

public class ContestStatusRefresher extends TimerTask {
    private final Runnable onReady;
    private final Consumer<String> onFinish;
    private final Consumer<String> onFail;
    private boolean onReadyActive = false;
    private boolean onFinishActive = false;

    public ContestStatusRefresher(Runnable onReady, Consumer<String> onFail, Consumer<String> onFinish) {
        this.onReady = onReady;
        this.onFail = onFail;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(CONTEST_STATUS, new Callback() {

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
                    DTOContestStatus dtoContestStatus = GSON_INSTANCE.fromJson(responseBody, DTOContestStatus.class);

                    if (dtoContestStatus != null) {
                        ContestStatus contestStatus = dtoContestStatus.getContestStatus();

                        switch (contestStatus) {
                            case WAITING:
                                break;
                            case ACTIVE:
                                if (!onReadyActive) {
                                    Platform.runLater(() ->
                                            onReady.run());
                                    onReadyActive = true;
                                    onFinishActive = false;
                                }
                                break;
                            case FINISHED:
                                if (!onFinishActive) {
                                    Platform.runLater(() ->
                                            onFinish.accept(dtoContestStatus.getWinningTeam()));
                                    onReadyActive = false;
                                    onFinishActive = true;
                                }
                        }
                    }
                }
            }
        });
    }
}

