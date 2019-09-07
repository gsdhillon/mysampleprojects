package SwipeCollection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nbp
 */
public class SwipeServerServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String remoteAddr = req.getRemoteAddr();
        try {
            resp.setContentType("text/plain");
            try (PrintWriter out = resp.getWriter()) {
                out.println("SwipeServer Status = " + AppContext.swipeCollectorStat);
            }
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            myHTTPConn = new MyHTTPConnection(req, resp);
            //Get Login Session
            int[] reqTypes = {
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN,
                LoginSession.TYPE_USER
            };
            HttpSession session = req.getSession(false);
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            String ReqType = myHTTPConn.readLine();
            switch (ReqType) {
                case "getSwipeServerStatus":
                    getSwipeServerStatus(myHTTPConn, loginSession);
                    break;
                default:
                    myHTTPConn.println("unknownRequest");
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
     * @param myHTTPConn
     * @param loginSession
     */
    private void getSwipeServerStatus(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            myHTTPConn.println("OK:SwipeServerStatus = " + AppContext.swipeCollectorStat);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
    /**
     *
     * @param myHTTPConn
     * @param loginSession
     */

}
