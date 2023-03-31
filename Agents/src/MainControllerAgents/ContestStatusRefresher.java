package MainControllerAgents;

import DTOs.ContestStatus;
import DTOs.DTOContestStatus;
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
import static UtilsAgent.Constants.CONTEST_STATUS;

public class ContestStatusRefresher extends TimerTask {
    private final Consumer<String> onFail;
    private boolean onWaitingActive = true;
    private boolean onFinishActive = false;
    private final Consumer<String> onFinish;
    private final Runnable onWaiting;

    public ContestStatusRefresher(Consumer<String> onFail, Consumer<String> onFinish, Runnable onWaiting) {
        this.onFail = onFail;
        this.onFinish = onFinish;
        this.onWaiting = onWaiting;
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
                } else {
                    String responseBody = response.body().string();
                    DTOContestStatus dtoContestStatus = GSON_INSTANCE.fromJson(responseBody, DTOContestStatus.class);
                    ContestStatus contestStatus;
                    if(dtoContestStatus == null)
                        contestStatus = ContestStatus.WAITING;
                    else
                        contestStatus = dtoContestStatus.getContestStatus();

                    switch (contestStatus) {
                        case WAITING:
                            if (!onWaitingActive) {
                                Platform.runLater(() ->
                                        onWaiting.run());
                                onWaitingActive = true;
                                onFinishActive = false;
                            }
                            break;
                        case ACTIVE:
                            break;
                        case FINISHED:
                            if (!onFinishActive) {
                                Platform.runLater(() ->
                                        onFinish.accept(dtoContestStatus.getWinningTeam()));
                                onFinishActive = true;
                                onWaitingActive = false;
                            }
                            break;
                    }
                }
            }
        });
    }
}


