package Servlets.AgentServlets;

import DTOs.DTOBruteForceQueueItem;
import DTOs.DTOBruteForceResult;
import DTOs.DTOMachineConfig;
import EngineManager.UBoatEngineManager;
import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "SendCandidatesServlet", urlPatterns = "/sendCandidates")
public class SendCandidatesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String allieNameFromSession = SessionUtils.getAllie(request); //get allie

        BufferedReader rd = request.getReader();
        String jsonStr = rd.lines().collect(Collectors.joining());

        Gson gson = new Gson();
        List<DTOBruteForceResult> allCandidates = gson.fromJson(jsonStr, new TypeToken<ArrayList<DTOBruteForceResult>>(){}.getType());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(allieNameFromSession);

        Uboat uboat = userManager.getUboat(allie.getUboatName());
        try {
            allie.receiveCandidates(allCandidates, uboat);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}