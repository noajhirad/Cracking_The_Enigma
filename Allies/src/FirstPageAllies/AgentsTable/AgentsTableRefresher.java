package FirstPageAllies.AgentsTable;

import DTOs.DTOAgentRow;
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

import static UtilsAllies.Constants.AGENTS_TABLE;
import static UtilsAllies.Constants.GSON_INSTANCE;

public class AgentsTableRefresher extends TimerTask {
    private final Consumer<List<DTOAgentRow>> agentsListConsumer;
    private final Consumer<String> onFail;

    public AgentsTableRefresher(Consumer<List<DTOAgentRow>> usersListConsumer, Consumer<String> onFail) {
        this.agentsListConsumer = usersListConsumer;
        this.onFail = onFail;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsync(AGENTS_TABLE, new Callback() {

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
                    String jsonListOfAgents = response.body().string();
                        List<DTOAgentRow> allAgents = GSON_INSTANCE.fromJson(jsonListOfAgents, new TypeToken<List<DTOAgentRow>>(){}.getType());
                    Platform.runLater(() ->
                            agentsListConsumer.accept(allAgents));
                }
            }
        });
    }
}
