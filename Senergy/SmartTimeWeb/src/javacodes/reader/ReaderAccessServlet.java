/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reader;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javacodes.services.servlet.EmployeeFormServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Packetizer;

/**
 *
 * @author nbp
 */
public class ReaderAccessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("GetReaders")) {
                getRdrListForReaderAccess(myHTTPConn);
            } else if (ReqType.equals("GetAssignedReaderString")) {
                getAssignedReaderString(myHTTPConn);
            } else if (ReqType.equals("SaveReaderAccess")) {
                saveReaderAccess(myHTTPConn);
            } else {
                myHTTPConn.println("unknownRequest");
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            Logger.getLogger(EmployeeFormServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    private void getRdrListForReaderAccess(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String query = "select RDR_No,Location,SelfIP from readersetting";
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
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getAssignedReaderString(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            int accesslevel = Integer.parseInt(myHTTPConn.readLine());
            String query = "select AccessString from readeraccess where AccessLevel=" + accesslevel;
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    myHTTPConn.println(rs.getString(1));
                } else {
                    myHTTPConn.println("None");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveReaderAccess(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int readerno = Integer.parseInt(myHTTPConn.readLine());
            String accessstring = myHTTPConn.readLine();

            String query = "Update readeraccess set AccessString='" + accessstring + "' where AccessLevel=" + readerno;
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");

        }  catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
