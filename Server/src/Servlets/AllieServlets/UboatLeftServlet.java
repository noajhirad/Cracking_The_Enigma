package Servlets.AllieServlets;

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

@WebServlet(name = "UboatLeftServlet", urlPatterns = "/uboatLeft")
public class UboatLeftServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        Allie allie = userManager.getAllie(usernameFromSession);
        String uboatName = allie.getUboatName();
        try {
            Boolean isUboatLeft;
            isUboatLeft = uboatName == null || uboatName.equals("");

            Gson gson = new Gson();
            String jsonResponseString;
            jsonResponseString = gson.toJson(isUboatLeft);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
            out.println("error found in UboatLeftServlet: " + e.getMessage());
        }
    }
}
