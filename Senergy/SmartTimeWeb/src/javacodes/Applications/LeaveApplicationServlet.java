/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.Applications;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
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
import lib.Utility.DateUtilities;
import lib.Utility.SimpleUtilities;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class LeaveApplicationServlet extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LeaveApplication</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LeaveApplication at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        try {
            response.setContentType("text/plain");
            myHTTPConn = new MyHTTPConnection(request, response);
            /**
             * ******* Get Login Session *****************
             */
            HttpSession session = request.getSession(false);
            int[] reqTypes = {
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();
            switch (ReqType) {
                case "getApplicationDetailsList":
                    getApplicationDetailsList(myHTTPConn, loginSession);
                    break;
                case "getFillForm":
                    getFillForm(myHTTPConn, loginSession);
                    break;
                case "saveLeaveApplication":
                    saveLeaveApplication(myHTTPConn, loginSession);
                    break;
                case "updateLeaveApplication":
                    updateLeaveApplication(myHTTPConn, loginSession);
                    break;
                case "CancelLeaveApplication":
                    CancelLeaveApplication(myHTTPConn, loginSession);
                    break;
                case "checkLeaveAppAvailable":
                    checkLeaveAppAvailable(myHTTPConn, loginSession);
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void getApplicationDetailsList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String empcode = loginSession.getUserID();
            String strQuery = "Select AppID,LeaveNature,FromDate,ToDate,LeaveGround,status from leaveapplication where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);

            pstm.setString(1, empcode);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 6; i++) {
                    p.addString(rs.getString(i));
                }
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void saveLeaveApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            String tempappid = dp.getString();//only used for update
            String leavetype = dp.getString();
            String reason = dp.getString();
            String fromdate = dp.getString();
            String todate = dp.getString();
            String status = dp.getString();
            String appid = "L" + DateUtilities.getCurrentDateTimeWithoutColon() + SimpleUtilities.generateRandomNumber();

            String strQuery = "Insert into leaveapplication (AppID,LeaveNature,FromDate,ToDate,LeaveGround,status) values (?,?,?,?,?,?)";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, appid);
            pstm.setString(2, leavetype);
            pstm.setString(3, fromdate);
            pstm.setString(4, todate);
            pstm.setString(5, reason);
            pstm.setString(6, status);
            pstm.executeUpdate();
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void getFillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String srno = myHTTPConn.readLine();
            String strQuery = "Select company,place,purpose,odFromdate,odTodate,appdate,approvedate,status from outdoor where srno=?";
            System.out.println("strQuery : " + strQuery);
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, srno);
            Packetizer p = new Packetizer();
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= 8; i++) {
                        p.addString(rs.getString(i));
                    }
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void updateLeaveApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            String tempappid = dp.getString();//only used for update
            String leavetype = dp.getString();
            String reason = dp.getString();
            String fromdate = dp.getString();
            String todate = dp.getString();
            String status = dp.getString();

            String strQuery = "Update leaveapplication set LeaveNature=?,FromDate=?,ToDate=?,LeaveGround=? where AppID=?";
            pstm = conn.prepareStatement(strQuery);

            pstm.setString(1, leavetype);
            pstm.setString(2, fromdate);
            pstm.setString(3, todate);
            pstm.setString(4, reason);
            pstm.setString(5, tempappid);
            pstm.executeUpdate();
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void CancelLeaveApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String appid = myHTTPConn.readLine();

            String strQuery = "Update leaveapplication set status='Canceled' where AppID=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, appid);
            pstm.executeUpdate();
            myHTTPConn.println("Canceled");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void checkLeaveAppAvailable(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String leavetype = myHTTPConn.readLine();
            String leavebalance = "";
            switch (leavetype) {
                case "EL":
                    leavebalance = "Current_EL_Balance";
                    break;
                case "CL":
                    leavebalance = "Current_CL_Balance";
                    break;
                case "ML":
                    leavebalance = "Current_ML_Balance";
                    break;
                case "SCL":
                    leavebalance = "Current_SCL_Balance";
                    break;
                case "Other Leave":
                    leavebalance = "Current_Other_Balance";
                    break;
                case "Tour":
                    leavebalance = "Current_Tour_Balance";
                    break;
                case "HPL":
                    leavebalance = "Current_HPL_Balance";
                    break;
            }
            String strQuery = "select Current_Balance, from leaveapplication where FromDate=? and ToDate=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, leavetype);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                myHTTPConn.println(rs.getString(1));
            } else {
                myHTTPConn.println("Not Found");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }
}
