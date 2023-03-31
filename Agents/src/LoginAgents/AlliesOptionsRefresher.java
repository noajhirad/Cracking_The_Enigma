package LoginAgents;

import UtilsAgent.HttpClientUtil;
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

import static UtilsAgent.Constants.ALLIES_OPTIONS;
import static UtilsAgent.Constants.GSON_INSTANCE;

public class AlliesOptionsRefresher extends TimerTask {

    private final Consumer<List<String>> alliesOptionsConsumer;
    private final Consumer<String> onFail;

    public AlliesOptionsRefresher(Consumer<List<String>> alliesOptionsConsumer, Consumer<String> onFail) {
        this.alliesOptionsConsumer = alliesOptionsConsumer;
        this.onFail = onFail;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(ALLIES_OPTIONS, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        onFail.accept(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(()->
                            onFail.accept(responseBody));
                }
                else {
                    String jsonListOfAllies = response.body().string();
                    List<String> allAllies = GSON_INSTANCE.fromJson(jsonListOfAllies, new TypeToken<List<String>>(){}.getType());
                    alliesOptionsConsumer.accept(allAllies);
                }
            }
        });
    }
}
