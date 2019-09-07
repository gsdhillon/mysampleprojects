/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.services.servlet;

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
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author GAURAV
 */
public class CategoryFormServlet extends HttpServlet {

    private String SecId;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        String Sec_Id;
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

            if (ReqType.equals("DeleteCategory")) {
                String catcode = myHTTPConn.readLine();
                DeleteCategory(myHTTPConn, catcode, loginSession);
            } else if (ReqType.equals("AddCategory")) {
                AddCategory(myHTTPConn, loginSession);
            } else if (ReqType.equals("getCategoryList")) {
                getCategoryList(myHTTPConn, loginSession);
            } else if (ReqType.equals("Update")) {
                UpdateCategory(myHTTPConn, loginSession);
            } else if (ReqType.equals("FormDetails")) {
                getDetails(myHTTPConn, loginSession);

            } else {
                myHTTPConn.println("unknownRequest");
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }


    }

    private void getDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String CatCode = myHTTPConn.readLine();
            String query = "Select * From category where CatCode ='" + CatCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            rs.next();
            for (int i = 1; i < 11; i++) {
                p.addString(rs.getString(i));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void UpdateCategory(MyHTTPConnection myHTTPConn, LoginSession loginSession)  {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(Packet);
            String CatCode = dp.getString();
            String CatName = dp.getString();
            String OverTime = dp.getString();
            String OverTimeLimit = dp.getString();
            String CompOff = dp.getString();
            String CompoffLimit = dp.getString();
            String GraceLateTime = dp.getString();
            String GraceEarlyTime = dp.getString();
            String query = "UPDATE "
                    + "category SET"
                    + " CatName='" + CatName
                    + "',OverTime='" + OverTime
                    + "',OverTimeLimit='" + OverTimeLimit
                    + "',CompOff='" + CompOff
                    + "',CompOffLimit='" + CompoffLimit
                    + "',GraceLateTime='" + GraceLateTime
                    + "',GraceEarlyTime='" + GraceEarlyTime
                    + "' WHERE CatCode = '" + CatCode + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddCategory(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String Packet = myHTTPConn.readLine();
            Depacketizer dp = new Depacketizer(Packet);
            String CatCode = dp.getString();
            String CatName = dp.getString();
            String OverTime = dp.getString();
            String OverTimeLimit = dp.getString();
            String CompOff = dp.getString();
            String CompoffLimit = dp.getString();
            String GraceLateTime = dp.getString();
            String GraceEarlyTime = dp.getString();

            String query = "INSERT INTO `category` (`CatCode`, `CatName`, `OverTime`,"
                    + " `OverTimeLimit`, `CompOff`, `CompOffLimit`, `GraceLateTime`,"
                    + " `GraceEarlyTime`,`LateAllowLimit`,`EarlyAllowLimit`)"
                    + " VALUES ('"
                    + CatCode + "','"
                    + CatName + "','"
                    + OverTime + "','"
                    + OverTimeLimit + "','"
                    + CompOff + "','"
                    + CompoffLimit + "','"
                    + GraceLateTime + "','"
                    + GraceEarlyTime + "','00:00:00','00:00:00')";

            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteCategory(MyHTTPConnection myHTTPConn, String id, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String query;

            query = "DELETE FROM `category` WHERE `CatCode`='" + id + "'";
            int executeUpdate = stmt.executeUpdate(query);

            myHTTPConn.println("Deleted" + id + "  " + executeUpdate);
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
     */
    private void getCategoryList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query = "SELECT CatCode,CatName,OverTime,OverTimeLimit,CompOff,"
                    + "CompOffLimit,GraceLateTime,GraceEarlyTime "
                    + " FROM category ORDER BY CatCode";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.addString(rs.getString(5));
                p.addString(rs.getString(6));
                p.addString(rs.getString(7));
                p.addString(rs.getString(8));

                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
