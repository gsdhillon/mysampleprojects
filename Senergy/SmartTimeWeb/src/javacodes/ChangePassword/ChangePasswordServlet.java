/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.ChangePassword;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
public class ChangePasswordServlet extends HttpServlet {

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
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN,
                LoginSession.TYPE_USER
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();
            switch (ReqType) {
                case "CheckOldPass":
                    checkOldPassword(myHTTPConn, loginSession);
                    break;
                case "SaveNewPassword":
                    saveNewPassword(myHTTPConn, loginSession);
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

    private void checkOldPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        try {
            String password = myHTTPConn.readLine();
            if (loginSession.getLoginType() == 0) {
                checkUserOldPassword(myHTTPConn, loginSession, password);
            } else if ((loginSession.getLoginType() == 1) || (loginSession.getLoginType() == 2)) {
                checkOfficeAdminPassword(myHTTPConn, loginSession, password);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        }
    }

    private void checkUserOldPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession, String password) {
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
            String emp_code = loginSession.getUserID();
            ResultSet rs = null;
            String query = "select Password from employeemaster where EmpCode='" + emp_code + "'";

            rs = stmt.executeQuery(query);
            if (rs.next()) {
                    myHTTPConn.println(rs.getString(1));
                } else {
                myHTTPConn.println("Not found");
                }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }

    }

    private void checkOfficeAdminPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession, String password) {
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
            String userid = loginSession.getUserID();
            ResultSet rs = null;
            String query = "select Password from useraccess where UserName='" + userid + "' and Password='" + password + "' and usertype='" + loginSession.getLoginType() + "'";

            rs = stmt.executeQuery(query);
            if (rs.next()) {
                myHTTPConn.println(rs.getString(1));
            } else {
                myHTTPConn.println("Not found");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveNewPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        try {
            String password = myHTTPConn.readLine();
            if (loginSession.getLoginType() == 0) {
                saveUserOldPassword(myHTTPConn, loginSession, password);
            } else if ((loginSession.getLoginType() == 1) || (loginSession.getLoginType() == 2)) {
                saveOfficeAdminPassword(myHTTPConn, loginSession, password);
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        }
    }

    private void saveUserOldPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession, String password) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String emp_code = loginSession.getUserID();
            String query = "Update employeemaster SET Password='" + password + "' where EmpCode='" + emp_code + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveOfficeAdminPassword(MyHTTPConnection myHTTPConn, LoginSession loginSession, String password) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userid = loginSession.getUserID();
            String query = "Update useraccess SET Password='" + password + "' where UserName='" + userid + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
