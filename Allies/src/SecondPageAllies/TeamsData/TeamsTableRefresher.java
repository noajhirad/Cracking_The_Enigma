package SecondPageAllies.TeamsData;

import DTOs.DTOTeamRow;
import UtilsAllies.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;
import static UtilsAllies.Constants.GSON_INSTANCE;
import static UtilsAllies.Constants.TEAMS_TABLE;


public class TeamsTableRefresher extends TimerTask {
    private final Consumer<List<DTOTeamRow>> teamsListConsumer;
    private final Consumer<String> onFail;

    public TeamsTableRefresher(Consumer<List<DTOTeamRow>> usersListConsumer, Consumer<String> onFail) {
        this.teamsListConsumer = usersListConsumer;
        this.onFail = onFail;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(TEAMS_TABLE, new Callback() {

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
                    String jsonListOfTeams = response.body().string();
                    List<DTOTeamRow> allTeams = GSON_INSTANCE.fromJson(jsonListOfTeams, new TypeToken<List<DTOTeamRow>>(){}.getType());
                    Platform.runLater(() ->
                            teamsListConsumer.accept(allTeams));
                }
            }
        });
    }
}

