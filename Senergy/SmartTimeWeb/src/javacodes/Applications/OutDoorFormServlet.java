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
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class OutDoorFormServlet extends HttpServlet {

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
            out.println("<title>Servlet OutDoorFormServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OutDoorFormServlet at " + request.getContextPath() + "</h1>");
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
            switch (ReqType) {
                case "getApplicationDetailsList":
                    getApplicationDetailsList(myHTTPConn, loginSession);
                    break;
                case "getFillForm":
                    getFillForm(myHTTPConn, loginSession);
                    break;
                case "SaveODApplication":
                    saveODApplication(myHTTPConn, loginSession);
                    break;
                case "updateODApplication":
                    updateODApplication(myHTTPConn, loginSession);
                    break;
                case "CancelODApplication":
                    CancelODApplication(myHTTPConn, loginSession);
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
            String strQuery = "Select srno,appdate,odFromdate,odTodate,approvedate,company,purpose,status from outdoor where EmpCode=?";
            ResultSet rs;
            pstm = conn.prepareStatement(strQuery);

            pstm.setString(1, empcode);
            rs = pstm.executeQuery();
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 8; i++) {
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

    private void saveODApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String empcode = dp.getString();
            String place = dp.getString();
            String company = dp.getString();
            String purpose = dp.getString();
            String frmDateTime = dp.getString();
            String toDateTime = dp.getString();
            String srNo = dp.getString();
            String appdate = dp.getString();

            String strQuery = "Insert into outdoor (srno,appdate,odFromdate,odTodate,EmpCode,purpose,place,company,status) values (?,?,?,?,?,?,?,?,'pending')";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, srNo);
            pstm.setString(2, appdate);
            pstm.setString(3, frmDateTime);
            pstm.setString(4, toDateTime);
            pstm.setString(5, empcode);
            pstm.setString(6, purpose);
            pstm.setString(7, place);
            pstm.setString(8, company);
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

    private void updateODApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            String srno = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            String empcode = dp.getString();
            String place = dp.getString();
            String company = dp.getString();
            String purpose = dp.getString();
            String frmDateTime = dp.getString();
            String toDateTime = dp.getString();
            String srNo1 = dp.getString();
            String appdate = dp.getString();

            String strQuery = "Update outdoor set odFromdate=?,odTodate=?,purpose=?,place=?,company=? where srno=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, frmDateTime);
            pstm.setString(2, toDateTime);
            pstm.setString(3, purpose);
            pstm.setString(4, place);
            pstm.setString(5, company);
            pstm.setString(6, srno);
            pstm.executeUpdate();
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }

    private void CancelODApplication(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String srno = myHTTPConn.readLine();

            String strQuery = "Update outdoor set status='Canceled' where srno=?";
            pstm = conn.prepareStatement(strQuery);
            pstm.setString(1, srno);
            pstm.executeUpdate();
            myHTTPConn.println("Canceled");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(pstm, conn);
        }
    }
}
