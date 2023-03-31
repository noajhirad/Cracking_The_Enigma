package Servlets.UboatServlets;

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


@WebServlet(name = "ClearContestUboat", urlPatterns = "/clearContestUboat")
public class ClearContestUboat extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request); //get username

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        try {
            Uboat uboat = userManager.getUboat(usernameFromSession);
            uboat.clearContestData();

            request.getSession(false).setAttribute(VERSION, 0);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in ClearContestUboat:" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}
