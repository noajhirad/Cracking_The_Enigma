package Servlets.UboatServlets;

import DTOs.DTOEncryptResponse;
import DTOs.DTOMachineConfig;
import EngineManager.UBoatEngineManager;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "UBoatEncrypt", urlPatterns = "/uboatEncrypt")
public class UBoatEncryptServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request); //get username

        BufferedReader rd = request.getReader();
        String input = rd.lines().collect(Collectors.joining());

        Gson gson = new Gson();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(usernameFromSession);

        try {
            DTOEncryptResponse encryptResponse = uboat.encrypt(input);
            String jsonResponseString = gson.toJson(encryptResponse);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in UBoatEncrypt servlet:" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}