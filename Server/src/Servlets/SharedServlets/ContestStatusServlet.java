package Servlets.SharedServlets;

import Contest.AllContests;
import Contest.Contest;
import DTOs.ContestStatus;
import DTOs.DTOContestStatus;
import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Utils.Constants.*;

@WebServlet(name = "ContestStatusServlet", urlPatterns = "/contestStatus")
public class ContestStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request);
        String roleFromSession = SessionUtils.getRole(request);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        // if allie: get the uboat name
        if(roleFromSession.equals(ALLIES))
            usernameFromSession = SessionUtils.getUboat(request);

        // if agent: get the allie & get the uboat name
        else if (roleFromSession.equals(AGENT)) {
            String allieName = SessionUtils.getAllie(request);
            Allie allie = userManager.getAllie(allieName);
            usernameFromSession = allie.getUboatName();
        }

        AllContests allContests = ServletUtils.getAllContests(getServletContext());

        try{
            DTOContestStatus contestStatus = null;
            if(usernameFromSession == null || usernameFromSession.equals(""))
                contestStatus = null;

            else {
                Contest contest = allContests.getContestByUboat(usernameFromSession);
                Uboat uboat = userManager.getUboat(usernameFromSession);
                if(uboat != null && contest != null)
                    contestStatus = new DTOContestStatus(contest.getContestStatus(), uboat.checkForWinner());
            }

            Gson gson = new Gson();
            String jsonResponseString = gson.toJson(contestStatus);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
//            out.println("error found in ContestStatusServlet" + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
