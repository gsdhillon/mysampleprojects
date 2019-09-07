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
public class HolidayFormServlet extends HttpServlet {

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
        }
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

            if (ReqType.equals("DeleteHoliday")) {
                String date = myHTTPConn.readLine();
                DeleteHoliday(myHTTPConn, date, loginSession);
            } else if (ReqType.equals("AddHoliday")) {
                AddHoliday(myHTTPConn, loginSession);
            } else if (ReqType.equals("getHolidayList")) {
                getHolidayList(myHTTPConn, loginSession);
            } else if (ReqType.equals("DivCode")) {
                getDivCode(myHTTPConn, loginSession);


            } else if (ReqType.equals("UpdateHoliday")) {
                UpdateHoliday(myHTTPConn, loginSession);
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

    private void UpdateHoliday(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String HolidayName = dp.getString();
            String date = dp.getString();
            String query = "UPDATE "
                    + "Holiday SET HolidayName='"
                    + HolidayName + "' WHERE HolidayDate = '"
                    + date + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddHoliday(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String HolidayName = dp.getString();
            String date = dp.getString();
            String query = "INSERT INTO "
                    + "Holiday (HolidayDate,HolidayName) Values('"
                    + date + "','"
                    + HolidayName + "')";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteHoliday(MyHTTPConnection myHTTPConn, String id, LoginSession loginSession) {
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

            query = "DELETE FROM `Holiday` WHERE `HolidayDate`='" + id + "'";
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
    private void getHolidayList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String query =
                    "SELECT "
                    + "DATE_FORMAT(HolidayDate, '%d/%m/%Y') HolidayDate, "
                    + "HolidayName "
                    + "FROM "
                    + "holiday";
//            query = "SELECT DeptName,SecCode,SecName,SecHeadName,SecHeadCode FROM "
//                    + "( SELECT DeptCode,DeptName FROM department ORDER BY DeptCode )"
//                    + " department,sectionmaster where sectionmaster.DepCode=department.DeptCode";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.incrCounter();
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getDivCode(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            Packetizer p = new Packetizer();

            String query = "SELECT DivCode FROM division";
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {
                p.addString(rs2.getString(1));
            }
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
