package Servlets.AgentServlets;

import EngineManager.UBoatEngineManager;
import EnigmaMachine.EnigmaMachine;
import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "BruteForceInitInfoServlet", urlPatterns = "/bruteForceInitInfo")
public class BruteForceInitInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        try {
            String allieName = SessionUtils.getAllie(request);
            Allie allie = userManager.getAllie(allieName);
            Uboat uboat = userManager.getUboat(allie.getUboatName());
            EnigmaMachine enigma = uboat.getEnigmaCopy();

            Gson gson = new Gson();

            String jsonResponseString = gson.toJson(enigma);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//           e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}
