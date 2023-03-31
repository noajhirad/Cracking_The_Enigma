package Servlets.SharedServlets;

import Users.Allie;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Utils.Constants.*;

@WebServlet(name = "MarkAsReadyServlet", urlPatterns = "/markAsReady")

public class MarkAsReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        String roleFromSession = SessionUtils.getRole(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        try {
            String taskSize = "";
            if (roleFromSession.equals(ALLIES)) {
                taskSize = request.getParameter(TASK_SIZE);
                String uboatNameFromSession = SessionUtils.getUboat(request);

                Allie allie = userManager.getAllie(usernameFromSession);
                Uboat uboat = userManager.getUboat(uboatNameFromSession);

                uboat.setAllieToReady(allie);
                allie.setTaskSize(Integer.parseInt(taskSize));
            } else {
                Uboat uboat = userManager.getUboat(usernameFromSession);
                uboat.markAsReady();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            PrintWriter out = response.getWriter();
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in MarkAsReadyServlet:" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}
