package Servlets.UboatServlets;

import DTOs.DTOMachineConfig;
import EngineManager.*;
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


@WebServlet(name = "RandomMachineCodeServlet", urlPatterns = "/randomMachineCode")
public class RandomMachineCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request); // get username

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(usernameFromSession);
        UBoatEngineManager engineManager = uboat.getEngineManager();

        Gson gson = new Gson();

        try {
            DTOMachineConfig config = engineManager.setAutomaticConfig();
            String jsonResponseString = gson.toJson(config);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);

        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in RandomMachineCodeServlet:" + e.getMessage());
            out.println(e.getMessage());
        }
    }

}
