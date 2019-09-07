package javacodes.services.servlet;

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
 */
public class MyDataServlet extends HttpServlet {

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession=null;
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
            if (msg.equals("getCurrentDateTime")) {
                getSysDateFull(myHTTPConn);
            } else {
                myHTTPConn.println("ERROR:unknown request");
            }
        } catch (Exception e) {
            try {
                if (myHTTPConn != null) {
                    myHTTPConn.println("ERROR:" + e.getMessage());
                }
                e.printStackTrace();
            } catch (Exception ex) {
            }
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    /**
     *
     * @param myHTTPConn
     *
    private void getSysDateFull(MyHTTPConnection myHTTPConn,LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            stmt = conn.createStatement();
            //TODO get DD-MM-YYYY-HH24-MI-SS
            //ResultSet rs = stmt.executeQuery("Select * From muster");
            Packetizer p = new Packetizer();
            p.addString("24");
            p.addString("09");
            p.addString("2012");
            p.addString("10");
            p.addString("26");
            p.addString("04");
            // while(rs.next()){
//                p.addString(rs.getString(1));
//                p.addString(rs.getString(2));
//                p.addString(rs.getString(3));
//                p.addString(rs.getString(4));
//                p.addString(rs.getString(5));
//                p.addString(rs.getString(6));
            // }
            //rs.close();
            myHTTPConn.println(p.getPacket());
        }  catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }*/
    /**
     *
     * @param myHTTPConn
     */
    private void getSysDateFull(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            //TODO get DD-MM-YYYY-HH24-MI-SS
            ResultSet rs = stmt.executeQuery("SELECT DATE_FORMAT(SYSDATE(),'%d-%m-%Y-%H-%i-%S')  from dual");
            rs.next();
            String dd_mm_yyyy_hh_mi_ss = rs.getString(1);
            rs.close();
            Packetizer p = new Packetizer();
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(0, 2));
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(3, 5));
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(6, 10));
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(11, 13));
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(14, 16));
            p.addString(dd_mm_yyyy_hh_mi_ss.substring(17));
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR:" + e.getMessage());
        }finally{
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
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        } catch (Exception e) {
            System.out.println("GET request from " + remoteAddr);
            e.printStackTrace();
        }
    }
}
