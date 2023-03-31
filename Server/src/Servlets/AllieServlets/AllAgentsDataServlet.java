package Servlets.AllieServlets;

import Contest.AllContests;
import DTOs.DTOAgentRow;
import DTOs.DTOContestRow;
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
import java.util.List;

@WebServlet(name = "AllAgentsDataServlet", urlPatterns = "/allAgents")
public class AllAgentsDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request); // get username

        // get the allie
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(usernameFromSession);

        if(allie == null){
//            System.out.println("Allie is null!");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        // get the dto list of all of it's agents
        List<DTOAgentRow> allDtos = allie.getAllAgentsDTO();
        Gson gson = new Gson();

        try {
            String jsonResponseString = gson.toJson(allDtos);
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
