import utils.Output;
import utils.Sessions;
import utils.TicketState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by WojtawDesktop on 20.10.2014.
 */
public class TicketMachine extends javax.servlet.http.HttpServlet {
    private Sessions<TicketState> allTickets = new Sessions<TicketState>();
    private HttpServletRequest request;
    private String sessionID;

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        Output.printLog("Incoming request");
        this.request = request;
        sessionID = getSessionID();
        String targetState = request.getParameter("targetState");
        TicketState currentState = allTickets.getData(sessionID);
        Output.printLog("Wants to be "+targetState+" and is "+currentState.toString());

        //Decide ticket state
        if(targetState.equals("NEW")){
            if(currentState == TicketState.NEW)
                response = respondNotModified(response);
            else
                response = respondNegative(response);
        }else if(targetState.equals("PAYMENT")){
            if(currentState == TicketState.NEW){
                allTickets.setData(sessionID, TicketState.PAYMENT);
                response = respondPositive(response);
            }else{
                response = respondNotModified(response);
            }
        }else if(targetState.equals("COMPLETED")){
            if(currentState == TicketState.PAYMENT){
                allTickets.setData(sessionID, TicketState.COMPLETED);
                response = respondPositive(response);
            }else{
                response = respondNotModified(response);
            }
        }else{
            response = respondNegative(response);
        }

        Output.printLog("Now is"+allTickets.getData(sessionID).toString());
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpServletResponse respondNegative(HttpServletResponse response){
        response.setStatus(412);
        return response;
    }


    private HttpServletResponse respondNotModified(HttpServletResponse response){
        response.setStatus(304);
        return response;
    }

    private HttpServletResponse respondPositive(HttpServletResponse response){
        response.setStatus(200);
        response.setHeader("Set-Cookie", "session-id="+ sessionID + "; MaxAge=3600");
        response.setHeader("Content-Type", "text/plain");
        try {
            response.getWriter().write("Ticket current state is" +
                    allTickets.getData(sessionID).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getSessionID(){
        String tmpID;
        try {
            tmpID = allTickets.getSessionID(request);
        } catch (Exception e) {
            tmpID = "";
            e.printStackTrace();
        }

        //Check if session was created otherwise new
        if(allTickets.getData(tmpID) == null){
            allTickets.setData(tmpID, TicketState.NEW);
        }

        return tmpID;
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        handleRequest(request, response);
    }
}
