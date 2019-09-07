package javacode.services;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import javacode.AppContext;
import javacode.DBConnection;
import javacode.LoginSession;
import javacode.MCC;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Packetizer;

/**
 * This is still older version of Servlet
 */
public class MyDataServlet extends HttpServlet {
    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MCC mcc = null;
        try {
            resp.setContentType("text/plain");
            mcc = new MCC(req, resp);
            /********* Get Login Session ******************/
            HttpSession session = req.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            LoginSession loginSession = LoginSession.getLoginSession(session, reqTypes);
            /* Read in the message from the servlet */
            String msg = mcc.readLine();
            /* Write the message back to the applet  */
            if (msg.equals("getCurrentDateTime")) {
                getSysDateFull(mcc);
             } else {
                mcc.println("ERROR:unknown request");
            }
        } catch (Exception e) {
            try{
                if(mcc != null){
                    mcc.println("ERROR:"+e.getMessage());
                }
                e.printStackTrace();
            }catch(Exception ex){
            }
        }finally{
            AppContext.close(mcc);
        }
    }
    /**
     *
     * @param mcc
     */
    private void getSysDateFull(MCC mcc) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                mcc.println("ERROR:Could not get database connection");
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
            mcc.println(p.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            mcc.println("ERROR:" + e.getMessage());
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
