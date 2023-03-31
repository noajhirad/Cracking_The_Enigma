package Servlets.AllieServlets;

import DTOs.DTOBruteForceResult;
import DTOs.DTOCandidatesAndVersion;
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
import static Utils.Constants.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetCandidatesAllieServlet", urlPatterns = "/getCandidatesAllie")
public class GetCandidatesAlliesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String usernameFromSession = SessionUtils.getUsername(request); //get username
        Integer version = SessionUtils.getVersion(request);
        if (version == null)
            version = 0;

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Allie allie = userManager.getAllie(usernameFromSession);

        int currentVersion;
        List<DTOBruteForceResult> allCandidates;
        synchronized (allie) {
            allCandidates = allie.getAllCandidates(version);
            currentVersion = allie.getVersion();
        }

        try {
            request.getSession(true).setAttribute(VERSION, currentVersion);
            DTOCandidatesAndVersion candidatesAndVersion = new DTOCandidatesAndVersion(allCandidates ,currentVersion);
//            System.out.println("Candidates: "+candidatesAndVersion.getCandidates() + " Version: "+candidatesAndVersion.getVersion());
            Gson gson = new Gson();
            String jsonResponseString;
            synchronized (allie.getCandidatesAndVersionLock()) {
                jsonResponseString = gson.toJson(candidatesAndVersion);
            }
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

