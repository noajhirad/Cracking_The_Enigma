package Servlets.AgentServlets;

import Contest.AllContests;
import DTOs.DTOAllieContestData;
import DTOs.DTOContestRow;
import Users.Agent;
import Users.Allie;
import Users.UserManager;
import Utils.Constants;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Utils.Constants.AGENT;
import static Utils.Constants.ALLIES;

@WebServlet(name = "AllieContestDataServlet", urlPatterns = "/allieContestData")
public class AllieContestDataServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String roleFromSession = SessionUtils.getRole(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        AllContests allContests = ServletUtils.getAllContests(getServletContext());
        String userName = SessionUtils.getUsername(request);
        DTOContestRow contestDTO = new DTOContestRow();

        if(roleFromSession.equals(AGENT)) {

            Agent agent = userManager.getAgent(userName);
            userName = SessionUtils.getAllie(request); // get allie username
//            String userName = SessionUtils.getUsername(request); // get agent username

//            UserManager userManager = ServletUtils.getUserManager(getServletContext());
//            AllContests allContests = ServletUtils.getAllContests(getServletContext());

            Allie allie = userManager.getAllie(userName);


            String uboat = allie.getUboatName();
//            DTOContestRow contestDTO;
            if (uboat == null || uboat.equals("") || allie.isOnWaitingAgent(agent))
                contestDTO = new DTOContestRow();
            else
                contestDTO = allContests.getDTOContestByUboat(uboat);
        }
        else if(roleFromSession.equals(ALLIES)) {
            Allie allie = userManager.getAllie(userName);
            String uboat = allie.getUboatName();

            if (uboat == null || uboat.equals(""))
                contestDTO = new DTOContestRow();
            else
                contestDTO = allContests.getDTOContestByUboat(uboat);
        }

        try {
            Gson gson = new Gson();
            DTOAllieContestData allieContestData = new DTOAllieContestData(userName, contestDTO);
            String jsonResponseString = gson.toJson(allieContestData);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}