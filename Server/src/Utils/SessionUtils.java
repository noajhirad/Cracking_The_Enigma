package Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import static Utils.Constants.*;

public class SessionUtils {
    public static final String USERNAME = "username";

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getRole (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ROLE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static String getUboat(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(UBOAT) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getAllie(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(ALLIES) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Integer getVersion(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(VERSION) : null;
        String value = sessionAttribute != null ? sessionAttribute.toString() : null;

        if (value != null) {
            try {
                Integer res = Integer.parseInt(value);
                return res;
            } catch (NumberFormatException numberFormatException) { }
        }
        return null;
    }
}