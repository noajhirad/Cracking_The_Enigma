package Servlets.AllieServlets;

import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import static Utils.Constants.*;

@WebServlet(name = "ClearContestAllies", urlPatterns = "/clearContestAllie")
public class ClearContestAllies extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request); //get username

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(usernameFromSession);

        String uboatName = SessionUtils.getUboat(request); //get uboat name

        try {
            Uboat uboat = userManager.getUboat(uboatName);
            allie.clearContestData();
            if(uboat != null)
                uboat.removeAllie(allie);

            request.getSession(false).setAttribute(UBOAT, "");
            request.getSession(false).setAttribute(VERSION, 0);

            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
            out.println("error found:" + e.getMessage());
        }
    }
}
