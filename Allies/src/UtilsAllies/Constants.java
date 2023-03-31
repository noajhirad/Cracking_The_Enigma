package UtilsAllies;

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
    public final static String CONTESTS_TABLE = FULL_SERVER_PATH + "/allContests";
    public final static String AGENTS_TABLE = FULL_SERVER_PATH + "/allAgents";
    public static final String TEAMS_TABLE = FULL_SERVER_PATH + "/allTeams";
    public static final String CONTEST_ENROLL = FULL_SERVER_PATH + "/contestEnroll";
    public static final String MARK_AS_READY = FULL_SERVER_PATH + "/markAsReady";
    public static final String CONTEST_STATUS = FULL_SERVER_PATH + "/contestStatus";
    public static final String START_BRUTE_FORCE = FULL_SERVER_PATH + "/startBruteForceAllie";
    public static final String ALL_CANDIDATES = FULL_SERVER_PATH + "/getCandidatesAllie";
    public static final String GET_AGENTS_PROGRESS = FULL_SERVER_PATH + "/getAgentsProgress";
    public static final String CLEAR_CONTEST =  FULL_SERVER_PATH + "/clearContestAllie";
    public static final String ALLIE_CONTEST_DATA = FULL_SERVER_PATH + "/allieContestData";
    public static final String UBOAT_LEFT =  FULL_SERVER_PATH + "/uboatLeft";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain;charset=UTF-8");

    public final static String ROLE = "Allies";
    public final static String TASK_SIZE = "taskSize";

    public final static int REFRESH_RATE = 500;
    public final static int DELAY_RATE = 0;

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
