package Servlets.AgentServlets;

import DTOs.DTOBruteForceQueueItem;
import DTOs.DTOBruteForceTasksBatch;
import EnigmaMachine.EnigmaMachine;
import Users.Allie;
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
import java.util.List;

import static Utils.Constants.TASK_SIZE;

@WebServlet(name = "GetDecryptionTasksServlet", urlPatterns = "/getDecryptionTasks")
public class GetDecryptionTasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String taskSizeFromParameter = request.getParameter(TASK_SIZE);

        try {
            int taskSize = Integer.parseInt(taskSizeFromParameter);
            String allieName = SessionUtils.getAllie(request);
            Allie allie = userManager.getAllie(allieName);
            DTOBruteForceTasksBatch missions = allie.getMissionsFromQueue(taskSize);

//            if(missions != null && missions.getAllTasks() != null)
                //System.out.println("Missions taken: " + missions.getAllTasks().size());

            Gson gson = new Gson();
            String jsonResponseString = gson.toJson(missions);
//            System.out.println("Tasks:" +"\n" + jsonResponseString);

            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
//            e.printStackTrace();
            out.println(e.getMessage());
        }
    }

}
