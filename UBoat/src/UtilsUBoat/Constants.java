package UtilsUBoat;

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
    public final static String LOAD_XML_PAGE = FULL_SERVER_PATH + "/loadFile";
    public final static String RANDOM_CODE_PAGE = FULL_SERVER_PATH + "/randomMachineCode";
    public final static String MANUAL_CODE_PAGE = FULL_SERVER_PATH + "/manualMachineCode";
    public static final String UBOAT_ENCRYPT_PAGE = FULL_SERVER_PATH + "/uboatEncrypt";
    public static final String RESET_PAGE = FULL_SERVER_PATH + "/reset";
    public static final String TEAMS_TABLE = FULL_SERVER_PATH + "/allTeams";
    public static final String MARK_AS_READY = FULL_SERVER_PATH + "/markAsReady";
    public static final String CONTEST_STATUS = FULL_SERVER_PATH + "/contestStatus";
    public static final String ALL_CANDIDATES = FULL_SERVER_PATH + "/getCandidatesUboat";
    public static final String CLEAR_CONTEST_DATA = FULL_SERVER_PATH + "/clearContestUboat";
    public static final String LOGOUT = FULL_SERVER_PATH + "/logoutUboat";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain;charset=UTF-8");

    public final static String ROLE = "Uboat";

    public final static int DELAY_RATE = 0;
    public final static int REFRESH_RATE = 500;

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
