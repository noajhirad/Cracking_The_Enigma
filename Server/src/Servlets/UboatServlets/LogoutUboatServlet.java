package Servlets.UboatServlets;

import Contest.AllContests;
import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static Utils.Constants.*;

@WebServlet(name = "LogoutUboatServlet", urlPatterns = "/logoutUboat")
public class LogoutUboatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        try{
            // delete contest
            AllContests allContests = ServletUtils.getAllContests(getServletContext());
            allContests.deleteContest(usernameFromSession);

            // remove Uboat name from allie
            Uboat uboat = userManager.getUboat(usernameFromSession);
            Set<Allie> allieSet = uboat.getAllAllies();
            for (Allie allie: allieSet) {
                allie.setUboatName(null);
                allie.setIsReady(false);
            }

            // delete Uboat from user manager
            userManager.removeUser(usernameFromSession, UBOAT);

            // clear session
            request.getSession(false).removeAttribute(USERNAME);
            request.getSession(false).removeAttribute(VERSION);
            request.getSession(false).removeAttribute(ROLE);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in LogoutUboatServlet:" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}
