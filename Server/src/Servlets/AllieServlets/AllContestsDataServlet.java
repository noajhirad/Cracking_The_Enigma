package Servlets.AllieServlets;

import Contest.AllContests;
import DTOs.DTOContestRow;
import Utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AllContestsDataServlet", urlPatterns = "/allContests")

public class AllContestsDataServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        AllContests allContests = ServletUtils.getAllContests(getServletContext());
        List<DTOContestRow> allContestsDTO = allContests.getAllContestsDTO();
        Gson gson = new Gson();

        try {
            String jsonResponseString = gson.toJson(allContestsDTO);
            out.print(jsonResponseString);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            e.printStackTrace();
            out.println(e.getMessage());
        }
    }
}