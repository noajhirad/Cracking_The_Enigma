package Servlets.AllieServlets;

import DTOs.DTOAllieProgressInfo;
import DTOs.DTOProgressInfo;
import Users.Allie;
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
import java.util.List;

@WebServlet(name = "GetAgentsProgressServlet", urlPatterns = "/getAgentsProgress")
public class GetAgentsProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userName = SessionUtils.getUsername(request);

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(userName);

        try {
            DTOAllieProgressInfo progress = allie.getProgress();
            Gson gson = new Gson();
            String jsonResponseString = gson.toJson(progress);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
            out.println("error found:" + e.getMessage());
        }
    }
}
