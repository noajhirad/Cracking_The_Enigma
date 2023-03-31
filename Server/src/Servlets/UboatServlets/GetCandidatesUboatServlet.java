package Servlets.UboatServlets;

import DTOs.DTOBruteForceResult;
import DTOs.DTOCandidatesAndVersion;
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
import java.util.List;

import static Utils.Constants.VERSION;

@WebServlet(name = "GetCandidatesUboatServlet", urlPatterns = "/getCandidatesUboat")
public class GetCandidatesUboatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request); //get username
        Integer version = SessionUtils.getVersion(request);
        if (version == null)
            version = 0;

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(usernameFromSession);

        int currentVersion = 0;
        List<DTOBruteForceResult> allCandidates;
        synchronized (uboat) {
            allCandidates = uboat.getAllCandidates(version);
            currentVersion = uboat.getVersion();
        }

        try {
            request.getSession(true).setAttribute(VERSION, currentVersion);
            DTOCandidatesAndVersion candidatesAndVersion = new DTOCandidatesAndVersion(allCandidates ,currentVersion);
//            System.out.println("Candidates: "+candidatesAndVersion.getCandidates() + " Version: "+candidatesAndVersion.getVersion());
            Gson gson = new Gson();
            String jsonResponseString;
            synchronized (uboat.getCandidateAndVersionLock()) {
                jsonResponseString = gson.toJson(candidatesAndVersion);
            }
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in getCsndidateServlet:" + e.getMessage());
            out.println(e.getMessage());
        }
    }
}
