package ChatareaUBoat;

import ChatareaUBoat.model.ChatLinesWithVersion;
import UtilsUBoat.HttpClientUtil;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static UtilsUBoat.Constants.CHAT_LINES_LIST;
import static UtilsUBoat.Constants.GSON_INSTANCE;

public class ChatAreaRefresher extends TimerTask {

    private final Consumer<ChatLinesWithVersion> chatlinesConsumer;
    private final IntegerProperty chatVersion;
    private int requestNumber;

    public ChatAreaRefresher(IntegerProperty chatVersion, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.chatlinesConsumer = chatlinesConsumer;
        this.chatVersion = chatVersion;
        requestNumber = 0;
    }

    @Override
    public void run() {

        final int finalRequestNumber = ++requestNumber;

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(CHAT_LINES_LIST)
                .newBuilder()
                .addQueryParameter("chatversion", String.valueOf(chatVersion.get()))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Something went wrong with Chat Request # " + finalRequestNumber);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    ChatLinesWithVersion chatLinesWithVersion = GSON_INSTANCE.fromJson(rawBody, ChatLinesWithVersion.class);
                    chatlinesConsumer.accept(chatLinesWithVersion);
                } else {
                    System.out.println("Something went wrong with Request # " + finalRequestNumber + ". Code is " + response.code());
                }
            }
        });

    }

}
