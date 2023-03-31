package UtilsAgent;

import com.google.gson.Gson;
import okhttp3.MediaType;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
//    public final static String CONTEXT_PATH = "";
    public final static String CONTEXT_PATH = "/Server_Web";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public static final String ALLIES_OPTIONS = FULL_SERVER_PATH + "/allAlliesOptions";
    public static final String ALLIE_CONTEST_DATA = FULL_SERVER_PATH + "/allieContestData";
    public static final String READY_STATUS = FULL_SERVER_PATH + "/readyStatusAgent";
    public static final String CONTEST_STATUS = FULL_SERVER_PATH + "/contestStatus";

    public static final String SEND_CANDIDATES = FULL_SERVER_PATH + "/sendCandidates";
    public static final String BRUTE_FORCE_INIT_INFO = FULL_SERVER_PATH + "/bruteForceInitInfo";
    public static final String GET_DECRYPTION_TASKS = FULL_SERVER_PATH + "/getDecryptionTasks";
    public static final String UPDATE_PROGRESS = FULL_SERVER_PATH + "/updateProgress";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    public final static int REFRESH_RATE = 500;
    public final static int DELAY_RATE = 0;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain;charset=UTF-8");

    public final static String ROLE = "Agents";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
