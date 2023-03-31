package SecondPageAllies;

import DTOs.DTOAllieContestData;
import DTOs.DTOContestRow;
import UtilsAllies.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UtilsAllies.Constants.ALLIE_CONTEST_DATA;
import static UtilsAllies.Constants.GSON_INSTANCE;

public class ContestDataRefresher extends TimerTask {

    private final Consumer<DTOContestRow> contestsdataConsumer;
    private final Consumer<String> onFail;

    public ContestDataRefresher(Consumer<DTOContestRow> contestsdataConsumer, Consumer<String> onFail) {
        this.contestsdataConsumer = contestsdataConsumer;
        this.onFail = onFail;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(ALLIE_CONTEST_DATA, new Callback() {
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
                DTOAllieContestData contestData = GSON_INSTANCE.fromJson(jsonListOfContestsRows, DTOAllieContestData.class);
                Platform.runLater(() ->
                        contestsdataConsumer.accept(contestData.getDtoContestRow()));
            }
        });

    }
}
