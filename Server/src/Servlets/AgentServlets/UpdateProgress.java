package Servlets.AgentServlets;

import DTOs.DTOProgressInfo;
import Users.Agent;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "UpdateProgress", urlPatterns = "/updateProgress")
public class UpdateProgress extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userName = SessionUtils.getUsername(request);

        BufferedReader rd = request.getReader();
        String jsonStr = rd.lines().collect(Collectors.joining());

        Gson gson = new Gson();
        DTOProgressInfo progress = gson.fromJson(jsonStr, DTOProgressInfo.class);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Agent agent = userManager.getAgent(userName);

        try {
            agent.setProgressInfo(progress);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}
