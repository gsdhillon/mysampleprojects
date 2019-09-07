package javacodes.user;

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
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class UserAccessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
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
                case "getDivisionList":
                    getDivisionList(myHTTPConn, loginSession);
                    break;
                case "getComboList":
                    getComboList(myHTTPConn, loginSession);
                    break;
                case "saveUserType":
                    saveUserType(myHTTPConn, loginSession);
                    break;
                case "updateUserType":
                    updateUserType(myHTTPConn, loginSession);
                    break;
                case "getFormDetails":
                    getFormDetails(myHTTPConn, loginSession);
                    break;
                case "deleteUser":
                    deleteUser(myHTTPConn, loginSession);
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

    private void getDivisionList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "SELECT "
                    + "DivCode,"
                    + "DivName "
                    + "FROM "
                    + "division ";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getComboList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int usertype = Integer.parseInt(myHTTPConn.readLine());
            String query = "Select UserName from useraccess where usertype='" + usertype + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveUserType(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            int usertype = Integer.parseInt(dp.getString());
            String username = dp.getString();
            String pwd = dp.getString();
            String accString = dp.getString();
            String query = "Insert into "
                    + "useraccess "
                    + "(UserName,"
                    + "Password,"
                    + "AccessString,"
                    + "usertype) "
                    + "values ('"
                    + username + "','"
                    + pwd + "','"
                    + accString + "',"
                    + usertype + ")";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void updateUserType(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(packet);
            int usertype = Integer.parseInt(dp.getString());
            String username = dp.getString();
            String pwd = dp.getString();
            String accString = dp.getString();
            String query = "Update "
                    + "useraccess "
                    + "set "
                    + "Password='" + pwd + "',"
                    + "AccessString='" + accString + "' "
                    + "where "
                    + "UserName='" + username + "' "
                    + "and "
                    + "usertype=" + usertype + "";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getFormDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int usertype = Integer.parseInt(myHTTPConn.readLine());
            String username = myHTTPConn.readLine();
            String query = "Select Password,AccessString from useraccess where UserName='" + username + "' and usertype='" + usertype + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }


    private void deleteUser(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int usertype = Integer.parseInt(myHTTPConn.readLine());
            String username = myHTTPConn.readLine();
            String query;

            query = "DELETE FROM useraccess WHERE UserName='" + username + "' and usertype=" + usertype;
            int executeUpdate = stmt.executeUpdate(query);

            myHTTPConn.println("Deleted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
