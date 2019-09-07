/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbpatil
 */
public class ShiftMasterFormServlet extends HttpServlet {

    private boolean update;
    private String ShiftCode;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
        LoginSession loginSession = null;
        //String Shift_Code;
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

            if (ReqType.equals("DeleteShift")) {
                String Shift_Code = myHTTPConn.readLine();
                DeleteShift(myHTTPConn, Shift_Code, loginSession);
            } else if (ReqType.equals("AddShift")) {
                AddShift(myHTTPConn, loginSession);
            } else if (ReqType.equals("getShiftList")) {
                getShiftList(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateShift")) {
                UpdateShift(myHTTPConn, loginSession);
            } else if (ReqType.equals("FormDetails")) {
                FormDetails(myHTTPConn, loginSession);
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

    private void FormDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String ShiftCode = myHTTPConn.readLine();
            String query = "Select * FROM shiftmaster WHERE "
                    + "ShiftCode='" + ShiftCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            if (rs.next()) {
                for (int i = 1; i < 5; i++) {
                    p.addString(rs.getString(i));
                }
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

    private void AddShift(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SCode = dp.getString();
            String ShiftType = dp.getString();
            String StartTime1 = dp.getString();
            String EndTime2 = dp.getString();
            String WorkingHrs = dp.getString();


            String query = "INSERT INTO "
                    + "shiftmaster (ShiftCode,ShiftType,StartTime1,EndTime1,WorkingHrs) Values('"
                    + SCode + "','"
                    + ShiftType + "','"
                    + StartTime1 + "','"
                    + EndTime2 + "','"
                    + WorkingHrs + "')";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void UpdateShift(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SCode = dp.getString();
            String ShiftType = dp.getString();
            String StartTime = dp.getString();
            String EndTime = dp.getString();
            String WorkingHrs = dp.getString();


            String query = "UPDATE "
                    + "shiftmaster SET ShiftType='" + ShiftType
                    + "', StartTime1='" + StartTime
                    + "', EndTime1='" + EndTime
                    + "',WorkingHrs='" + WorkingHrs
                    + "' WHERE ShiftCode='" + SCode + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteShift(MyHTTPConnection myHTTPConn, String SCode, LoginSession loginSession) {
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

            query = "DELETE FROM `shiftmaster` WHERE `ShiftCode`='" + SCode + "'";
            int executeUpdate = stmt.executeUpdate(query);

            myHTTPConn.println("Deleted" + SCode + "  " + executeUpdate);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getShiftList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query =
                    "SELECT "
                    + "ShiftCode, "
                    + "ShiftType, "
                    + "StartTime1, "
                    + "EndTime1, "
                    + "WorkingHrs "
                    + "FROM "
                    + "shiftmaster";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.addString(rs.getString(5));
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
