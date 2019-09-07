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
public class LConfigFormServlet extends HttpServlet {

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

            if (ReqType.equals("DeleteLConfig")) {
                String Leavecode = myHTTPConn.readLine();
                DeleteLConfig(myHTTPConn, Leavecode, loginSession);
            } else if (ReqType.equals("AddLConfig")) {
                AddLConfig(myHTTPConn, loginSession);
            } else if (ReqType.equals("getLConfigList")) {
                getLConfigList(myHTTPConn, loginSession);
            } else if (ReqType.equals("DivCode")) {
                getDivCode(myHTTPConn, loginSession);

            } else if (ReqType.equals("UpdateLConfig")) {
                UpdateLConfig(myHTTPConn, loginSession);
            } else if (ReqType.equals("FormDetails")) {
                getDetails(myHTTPConn, loginSession);
            } else {
                myHTTPConn.println("unknownRequest");
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
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
            String LeaveCode = myHTTPConn.readLine();
            String query = "SELECT * FROM leaveconfig where LeaveCode = '" + LeaveCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            Packetizer p = new Packetizer();
            for (int i = 1; i < 29; i++) {
                p.addString(rs.getString(i));
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

    private void UpdateLConfig(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String LeaveCode = dp.getString();
            String LeaveName = dp.getString();
            String LeaveDescription = dp.getString();
            String EL = dp.getString();
            String CL = dp.getString();
            String ML = dp.getString();
            String SCL = dp.getString();
            String HPL = dp.getString();
            String EOL = dp.getString();
            String TourLeave = dp.getString();
            String CML = dp.getString();
            String HCL = dp.getString();
            String OtherLeave = dp.getString();
            String MaxNoOfDays = dp.getString();
            String MaxNoOfTimesAllwd = dp.getString();
            String MaxNoOfDaysAtTime = dp.getString();
            String AccumulationAllwd = dp.getString();
            String MaxAccumulationAllwd = dp.getString();
            String IsHalfdayType = dp.getString();
            String IsEncashmentType = dp.getString();
            String IsOffBefore = dp.getString();
            String IsOffAfter = dp.getString();
            String IsOffTrapped = dp.getString();
            String IsHolidayTrapped = dp.getString();
            String IsNegativeBalanceAllwd = dp.getString();
            String Isaccountable = dp.getString();
            String Type = dp.getString();
            String OLRemark = dp.getString();

            String query = "UPDATE leaveconfig SET "
                    + "LeaveName='" + LeaveName
                    + "',LeaveDescription='" + LeaveDescription
                    + "',EL='" + EL
                    + "',CL='" + CL
                    + "',ML='" + ML
                    + "',SCL='" + SCL
                    + "',HPL='" + HPL
                    + "',EOL='" + EOL
                    + "',TourLeave='" + TourLeave
                    + "',CML='" + CML
                    + "',HCL='" + HCL
                    + "',OtherLeave='" + OtherLeave
                    + "',NoOfDays='" + MaxNoOfDays
                    + "',NoOfTimesAllwd='" + MaxNoOfTimesAllwd
                    + "',NoOfDaysAtTime='" + MaxNoOfDaysAtTime
                    + "',AccumulationAllwd='" + AccumulationAllwd
                    + "',MaxAccumulationAllwd='" + MaxAccumulationAllwd
                    + "',IsHalfdayType='" + IsHalfdayType
                    + "',IsEncashmentType='" + IsEncashmentType
                    + "',IsOffBefore='" + IsOffBefore
                    + "',IsOffAfter='" + IsOffAfter
                    + "',IsOffTrapped='" + IsOffTrapped
                    + "',IsHolidayTrapped='" + IsHolidayTrapped
                    + "',IsNegativeLBalanceAllwd='" + IsNegativeBalanceAllwd
                    + "',Isaccountable='" + Isaccountable
                    + "',Type='" + Type
                    + "',OLRemark='" + OLRemark + "' WHERE LeaveCode='" + LeaveCode + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddLConfig(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String middlequery = "'" + dp.getString() + "'";
            while (!dp.isEmpty()) {
                middlequery += ",'" + dp.getString() + "'";
            }
            String query = "INSERT INTO "
                    + "leaveconfig (`LeaveCode`,`LeaveName`, `LeaveDescription`, `EL`, `CL`, `ML`, `SCL`, `HPL`, `EOL`,"
                    + " `TourLeave`, `CML`, `HCL`, `OtherLeave`, `NoOfDays`, `NoOfTimesAllwd`, `NoOfDaysAtTime`,"
                    + " `AccumulationAllwd`, `MaxAccumulationAllwd`, `IsHalfdayType`, `IsEncashmentType`, `IsOffBefore`,"
                    + " `IsOffAfter`, `IsOffTrapped`, `IsHolidayTrapped`, `IsNegativeLBalanceAllwd`,"
                    + " `Isaccountable`, `Type`, `OLRemark`) Values("
                    + middlequery
                    + ")";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteLConfig(MyHTTPConnection myHTTPConn, String Leavecode, LoginSession loginSession) {
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

            query = "DELETE FROM `leaveconfig` WHERE `LeaveCode`='" + Leavecode + "'";
            int executeUpdate = stmt.executeUpdate(query);

            myHTTPConn.println("Deleted" + Leavecode + "  " + executeUpdate);
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
    private void getLConfigList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
                    "SELECT * FROM leaveconfig";
//            query = "SELECT DeptName,SecCode,SecName,SecHeadName,SecHeadCode FROM "
//                    + "( SELECT DeptCode,DeptName FROM department ORDER BY DeptCode )"
//                    + " department,sectionmaster where sectionmaster.DepCode=department.DeptCode";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i <= 28; i++) {
                    p.addString(rs.getString(i));
                }
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
