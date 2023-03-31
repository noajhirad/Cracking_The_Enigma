package SecondPageAllies.TeamsProgress;

import DTOs.DTOAllieProgressInfo;
import DTOs.DTOProgressInfo;
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

import static UtilsAllies.Constants.*;

public class TeamsProgressRefresher extends TimerTask {

    Consumer<String> onFail;
    Consumer<DTOAllieProgressInfo> onResp;

    public TeamsProgressRefresher(Consumer<DTOAllieProgressInfo> onResp, Consumer<String> onFail) {
        this.onResp = onResp;
        this.onFail = onFail;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(GET_AGENTS_PROGRESS, new Callback() {

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
//                    System.out.println("agents progress in Allie: " + responseBody);
                    DTOAllieProgressInfo progressInfo = GSON_INSTANCE.fromJson(responseBody, DTOAllieProgressInfo.class);
//                    System.out.println("Current Input: " + progressInfo.getAllAgentsProgress().get(0).getCurrentInput());
                    Platform.runLater(() ->
                            onResp.accept(progressInfo));
                }
            }
        });
    }
}
