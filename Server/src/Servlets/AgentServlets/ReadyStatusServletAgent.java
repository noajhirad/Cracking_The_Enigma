package Servlets.AgentServlets;

import Users.Agent;
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

import static Utils.Constants.AGENT;
import static Utils.Constants.ALLIES;

@WebServlet(name = "ReadyStatusServletAgent", urlPatterns = "/readyStatusAgent")
public class ReadyStatusServletAgent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String allieName = SessionUtils.getAllie(request);
        String username = SessionUtils.getUsername(request);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(allieName);
        Agent agent = userManager.getAgent(username);

        try{
            Gson gson = new Gson();
            boolean isReady = allie.isReady() && !allie.isOnWaitingAgent(agent);
            String jsonResponseString = gson.toJson(isReady);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}
