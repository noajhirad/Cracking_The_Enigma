package Servlets.AllieServlets;

import Contest.AllContests;
import DTOs.DTOContestRow;
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

import static Utils.Constants.UBOAT;

@WebServlet(name = "ContestEnrollServlet", urlPatterns = "/contestEnroll")

public class ContestEnrollServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request); //get username
        String uboatName = request.getParameter(UBOAT);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        AllContests allContests = ServletUtils.getAllContests(getServletContext());

        Allie allie = userManager.getAllie(usernameFromSession);
        Uboat uboat = userManager.getUboat(uboatName);

        try {
            uboat.addAllie(allie);
            request.getSession(true).setAttribute(UBOAT, uboatName);
            allie.setUboatName(uboatName);

            Gson gson = new Gson();
            DTOContestRow contest = allContests.getDTOContestByUboat(uboatName);

            if(contest == null){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.println("No contest was found.");
                return;
            }

            String jsonResponseString = gson.toJson(contest);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            System.out.println(e.getStackTrace());
            out.println(e.getMessage());
        }
    }
}
