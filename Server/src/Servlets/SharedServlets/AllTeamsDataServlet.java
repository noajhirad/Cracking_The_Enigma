package Servlets.SharedServlets;

import DTOs.DTOTeamRow;
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
import java.util.ArrayList;
import java.util.List;
import static Utils.Constants.ALLIES;
import static Utils.Constants.UBOAT;

@WebServlet(name = "AllTeamsDataServlet", urlPatterns = "/allTeams")
public class AllTeamsDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request); // get username
        String roleFromSession = SessionUtils.getRole(request); // get role

        if(roleFromSession == null) {
//            System.out.println("role from session is null! ");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

//        if(roleFromSession.equals(UBOAT))
//            System.out.println("UBOAT ROLE IS GOOD!");

        if(roleFromSession.equals(ALLIES))
            usernameFromSession = SessionUtils.getUboat(request);

        // get the uboat
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(usernameFromSession);
        // get the dto list of all teams
        List<DTOTeamRow> allDtos = new ArrayList<>();
        if(uboat != null)
            allDtos = uboat.getAllTeamsDTO();
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
//            System.out.println("error found in allTeamsDataServlet" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}
