/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reports;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Packetizer;

/**
 *
 * @author nbp
 */
public class EmpEntryServlet extends HttpServlet {

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
            /**
             * ******* Get Login Session *****************
             */
            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);
            /*
             * Read in the message from the servlet
             */
            String msg = myHTTPConn.readLine();


            /*
             * Write the message back to the applet
             */


            if (msg.equals("getEmpEntryDetails")) {
                getEmpEntryDetails(myHTTPConn, loginSession);
            } else {
                myHTTPConn.println("ERROR:unknown request");
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
     */
    private void getEmpEntryDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String entrydate = myHTTPConn.readLine();
            String empcode = myHTTPConn.readLine();
            entrydate = entrydate + " %";

            String query = "select "
                    + "(select Location from readersetting where readersetting.SelfIP=swipedata.IPAddress) as Location,"
                    + "(select ReaderType from readersetting where readersetting.SelfIP=swipedata.IPAddress) as ReaderType, "
                    + "DATE_FORMAT(ProDateTime, '%H:%i:%s') `Time` "
                    + "from "
                    + "swipedata "
                    + "where "
                    + "EmpCode='" + empcode + "' "
                    + "and "
                    + "ProDateTime  "
                    + "like '" + entrydate + "' "
                    + "order by ProDateTime";
            Packetizer p;
            try (ResultSet rs = stmt.executeQuery(query)) {
                p = new Packetizer();
                p.setCounter();
                while (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString(rs.getString(2));
                    p.addString(rs.getString(3));
                    p.incrCounter();
                }
            }
            conn.close();
            stmt.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
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
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }
}
