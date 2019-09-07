package javacode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class ObjectServlet
 * Created on Sep 7, 2013
 * @version 1.0.0
 * @author
 */
public abstract class ObjectServlet  extends HttpServlet {
    private static int[] reqTypes;
    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected ObjectServlet(){
        ObjectServlet.reqTypes = getRequiredLoginTypes();
    }
    /**
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public final void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = req.getRemoteAddr();
        try {
            resp.setContentType("text/plain");
            try (PrintWriter out = resp.getWriter()) {
                out.println("Error: this servlet does not support the GET method!");
            }
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */    
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MCC mcc = null;
        LoginSession loginSession = null;
        try {
            resp.setContentType("text/plain");
            mcc = new MCC(req, resp);
            HttpSession session = req.getSession(false);
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            String reqMsg = mcc.readLine();
            handleRequest(reqMsg, mcc, loginSession);
        } catch (Exception e) {
            try{
                if(mcc != null){
                    mcc.println("ERROR:"+e.getMessage());
                }
                e.printStackTrace();
            }catch(Exception ex){
            }
        } finally {
            AppContext.close(mcc);
        }
    }
    /**
     * 
     * @param reqMsg
     * @param mcc
     * @param loginSession 
     */
    private void handleRequest(String reqMsg, MCC mcc, LoginSession loginSession){
        DBConnection conn = null;
        try{
            conn = AppContext.getDBConnection();
            processRequest(reqMsg, mcc, loginSession, conn);
        }catch (Exception e) {
            e.printStackTrace();
            mcc.println("ERROR:" + e.getMessage());
        }finally{
            AppContext.close(conn);
        }          
    }
    /**
     * 
     * @return 
     */
    protected abstract int[] getRequiredLoginTypes();
    /**
     * 
     * @param loginSession
     * @param reqMsg
     * @param mcc 
     */
    protected abstract void processRequest(String reqMsg, MCC mcc, LoginSession loginSession, DBConnection conn) throws Exception;
}