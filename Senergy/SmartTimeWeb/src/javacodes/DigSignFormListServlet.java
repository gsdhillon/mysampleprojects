package javacodes;

import SmartTimeApplet.visitor.vis_app.VisitorForm;
import java.io.PrintWriter;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javacodes.vis_app.VisAppClass;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Packetizer;

/**
 * DigSignFormListServlet.java Created on Sep 28, 2007, 1:22:41 PM
 *
 * @author Gurmeet Singh
 */
public class DigSignFormListServlet extends HttpServlet {

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            resp.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(req, resp);

            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            String reqMsg = myHTTPConn.readLine();
            switch (reqMsg) {
                case "getDocListForSign":
                    getDocListForSign(loginSession, myHTTPConn);
                    break;
                case "getsigneddoclist":
                    getSignedDocList(loginSession, myHTTPConn);
                    break;
                case "getAppliedForms":
                    getAppliedForms(loginSession, myHTTPConn);
                    break;
                default:
                    myHTTPConn.println("ERROR:unknown request - "+reqMsg);
                    break;
            }
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    /**
     *
     * @param loginSession
     * @param myHTTPConn
     */
    private void getDocListForSign(LoginSession loginSession, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            String userID = loginSession.getUserID();
            stmt = conn.createStatement();
            Packetizer p = new Packetizer();
            p.setCounter();
            //get VisAppClass applications
            VisAppClass.addFormsForSign(stmt, userID, p);
            //TODO NEW_APP
            stmt.close();
            //return the doclist
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.toString().replaceAll("\n", ";"));
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param loginSession
     * @param myHTTPConn
     */
    private void getAppliedForms(LoginSession loginSession, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            String userID = loginSession.getUserID();
            String type = myHTTPConn.readLine();
            String year = myHTTPConn.readLine();
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            stmt = conn.createStatement();
            Packetizer p = new Packetizer();
            p.setCounter();
       //     System.out.println(" type : " + type + "   VisitorForm.appShortName : " + VisitorForm.appShortName);
            if (type.equals(VisitorForm.shortName)) {
                //get VisAppClass applications
                VisAppClass.addFormsApplied(stmt, userID, year, p);
            }
            //TODO NEW_APP

            stmt.close();
            //return the doclist
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(null, e);
            myHTTPConn.println("ERROR:" + e.toString().replaceAll("\n", ";"));
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param bs
     * @param myHTTPConn
     */
    private void getSignedDocList(LoginSession loginSession, MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            String userID = loginSession.getUserID();
            String type = myHTTPConn.readLine();
            String year = myHTTPConn.readLine();
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            stmt = conn.createStatement();
            Packetizer p = new Packetizer();
            p.setCounter();
            if (type.equals(VisitorForm.shortName)) {
                //get VisAppClass applications
                VisAppClass.addSignedForms(stmt, userID, year, p);
            }
            //TODO NEW_APP
            stmt.close();
            //return the doclist
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.toString().replaceAll("\n", ";"));
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = req.getRemoteAddr();
        try {
            resp.setContentType("text/plain");
            try (PrintWriter out = resp.getWriter()) {
                out.println("Error: this servlet does not support the GET method!");
            }
        } catch (Exception e) {
            //System.out.println("GET request from " + remoteAddr);
            AppContext.log(null, e);
        }
    }
}
