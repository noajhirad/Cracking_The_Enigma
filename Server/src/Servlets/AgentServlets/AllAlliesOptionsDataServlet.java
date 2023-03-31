package Servlets.AgentServlets;

import Contest.AllContests;
import DTOs.DTOContestRow;
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
import java.util.Collection;
import java.util.List;

@WebServlet(name = "AllAlliesOptionsDataServlet", urlPatterns = "/allAlliesOptions")
public class AllAlliesOptionsDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Collection<String> allAlliesNames = userManager.getAllAlliesNames();
        Gson gson = new Gson();

        try {
            String jsonResponseString = gson.toJson(allAlliesNames);
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
