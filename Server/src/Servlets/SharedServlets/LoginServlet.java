package Servlets.SharedServlets;

import Contest.Contest;
import Contest.AllContests;
import DTOs.ContestStatus;
import EngineManager.EngineManager;
import Users.QueryParameters;
import Users.Uboat;
import Users.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static Utils.Constants.*;

@WebServlet(name = "LoginServlet", urlPatterns = "/loginShortResponse")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(USERNAME);
            String roleFromParameter = request.getParameter(ROLE);
            String taskSizeFromParameter = request.getParameter(TASK_SIZE);
            String alliesTeamParameter = request.getParameter(ALLIE_TEAM);
            String threadsNumParameter = request.getParameter(THREADS_NUMBER);

            // assert username and role
            if (usernameFromParameter == null || usernameFromParameter.isEmpty() ||
                    roleFromParameter == null || roleFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                if(roleFromParameter != null && !roleFromParameter.isEmpty()) {
                    if (roleFromParameter.trim().equals(AGENT)) {
                        if (taskSizeFromParameter == null || taskSizeFromParameter.isEmpty() ||
                                alliesTeamParameter == null || alliesTeamParameter.isEmpty() ||
                                threadsNumParameter == null || threadsNumParameter.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                        }
                    }
                }
            }
            else {

                usernameFromParameter = usernameFromParameter.trim();
                roleFromParameter = roleFromParameter.trim();

                if(roleFromParameter.equals(AGENT)) {
                    taskSizeFromParameter = taskSizeFromParameter.trim();
                    alliesTeamParameter = alliesTeamParameter.trim();
                    threadsNumParameter = threadsNumParameter.trim();
                }

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        if(roleFromParameter.equals(AGENT)) {
                            ContestStatus status;
                            String uboatName = userManager.getAllie(alliesTeamParameter).getUboatName();
                            if(uboatName == null || uboatName.equals(""))
                                status = ContestStatus.WAITING;
                            else {
                                AllContests allContests = ServletUtils.getAllContests(getServletContext());
                                Contest currentContest = allContests.getContestByUboat(uboatName);
                                status = currentContest.getContestStatus();
                            }
                            userManager.addAgentUser(usernameFromParameter, taskSizeFromParameter, alliesTeamParameter, threadsNumParameter, status);
                            request.getSession(true).setAttribute(ALLIES, alliesTeamParameter);
                        }
                        else
                            userManager.addUser(usernameFromParameter, roleFromParameter);

                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(ROLE, roleFromParameter);

                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}