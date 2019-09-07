/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.visitor.servlet;

import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class OtherLeaveServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("addOtherLeave")) {
                addOtherLeave(myHTTPConn, loginSession);
            }
        } catch (Exception e) {
        }
    }

    public void addOtherLeave(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        
        DBConnection conn = null;
        PreparedStatement prstmnt = null;
        String query = "";
        try {
            conn = AppContext.getDBConnection();
            prstmnt = (PreparedStatement) conn.prepareStatement(query);
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;

            }
            String EmpCode = myHTTPConn.readLine();
            query = "SELECT  EmpCode,EmpName,designation,Division FROM employeemaster WHERE EmpCode='" + EmpCode + "'";
             Packetizer p = new Packetizer();
              p.setCounter();
            try (ResultSet rs = prstmnt.executeQuery(query)) {
                if(rs.next()){
                for (int i = 1; i < 5; i++) {
                    p.addString(rs.getString(i));
                    }
                 p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(prstmnt, conn);
        }
    }

}   
   