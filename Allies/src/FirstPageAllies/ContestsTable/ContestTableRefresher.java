package FirstPageAllies.ContestsTable;

import DTOs.DTOContestRow;
import UtilsAllies.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import static UtilsAllies.Constants.*;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ContestTableRefresher extends TimerTask {

    private final Consumer<List<DTOContestRow>> contestsListConsumer;
    private final Consumer<String> onFail;

    public ContestTableRefresher(Consumer<List<DTOContestRow>> usersListConsumer, Consumer<String> onFail) {
        this.contestsListConsumer = usersListConsumer;
        this.onFail = onFail;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(CONTESTS_TABLE, new Callback() {

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
                List<DTOContestRow> allContests = GSON_INSTANCE.fromJson(jsonListOfContestsRows, new TypeToken<List<DTOContestRow>>(){}.getType());
                Platform.runLater(() ->
                    contestsListConsumer.accept(allContests));
            }
        });
    }
}
