package Servlets.UboatServlets;

import Contest.AllContests;
import Contest.BattleField;
import Contest.Contest;
import DTOs.DTOBattleField;
import DTOs.DTOFileInfo;
import DTOs.DTOMachineInfo;
import EngineManager.*;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.Scanner;
import Exceptions.*;

@WebServlet(name = "FileUploadServlet", urlPatterns = "/loadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request); //get username

//        System.out.println("UsernameFromSession: " + usernameFromSession);

        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts)
            fileContent.append(readFromInputStream(part.getInputStream()));

        Gson gson = new Gson();
        InputStream targetStream = new ByteArrayInputStream(fileContent.toString().getBytes());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Uboat uboat = userManager.getUboat(usernameFromSession);
        UBoatEngineManager engineManager = uboat.getEngineManager();

        try {
            DTOFileInfo fileInfo = engineManager.readXML(targetStream);

            // verify battlefield name & create new contest
            DTOBattleField dtoBattleField = fileInfo.getDtoBattleField();
            AllContests allContests = ServletUtils.getAllContests(getServletContext());
            synchronized (this) {
                if(allContests.isContestExists(dtoBattleField.getName()))
                    throw new FileException(FileException.ErrorType.CONTEST_NAME_EXIST);
                BattleField battleField = new BattleField(dtoBattleField);
                Contest newContest = allContests.createNewContest(battleField, uboat);
                uboat.setContest(newContest);
                uboat.setBattleField(battleField);
            }

            DTOMachineInfo machineInfo = fileInfo.getDtoMachineInfo();
            String jsonResponseString = gson.toJson(machineInfo);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
//            System.out.println("error found in fileUploadServlet" + e.getMessage());
            out.println(e.getMessage());
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
