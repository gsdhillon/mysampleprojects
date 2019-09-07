/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reader;

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
public class RSFormServlet extends HttpServlet {

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
                //LoginSession.TYPE_USER,
                // LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("FillForm")) {
                FillForm(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetEntries")) {
                SetEntries(myHTTPConn, loginSession);
            } else if (ReqType.equals("SaveIPSettingsToDB")) {
                saveIPSettingsToDB(myHTTPConn, loginSession);
            } else if (ReqType.equals("SaveMenu1ToDB")) {
                saveMenu1ToDB(myHTTPConn, loginSession);
            } else if (ReqType.equals("SaveMenu2ToDB")) {
                saveMenu2ToDB(myHTTPConn, loginSession);
            } else if (ReqType.equals("SaveMenu3ToDB")) {
                saveMenu3ToDB(myHTTPConn, loginSession);
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

    private void SetEntries(MyHTTPConnection myHTTPConn, LoginSession loginSession)  {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String ReaderNo = myHTTPConn.readLine();
            String query = "Select ReaderNo,"
                    + "BaudRate,"
                    + "ReaderMode,"
                    + "ReaderTypeLevel,"
                    + "UseRdrDB,"
                    + "CheckShiftHoliday,"
                    + "CheckPIN,"
                    + "CheckValidityDOB,"
                    + "AutoOffline,"
                    + "AntiPassBack,"
                    + "APBLevel,"
                    + "StoreAllRec,"
                    + "MultiRejAlarm,"
                    + "DoorDelay,"
                    + "LEDDelay,"
                    + "InvalidAlarm,"
                    + "TamperAlarm,"
                    + "DoorOpenAlarm,"
                    + "OnlineRespTimeout,"
                    + "KeyTmOut,"
                    + "AppID1,"
                    + "AppID2,"
                    + "ReaderString,"
                    + "Location,"
                    + "Division,"
                    + "ReaderType,"
                    + "SelfIP,"
                    + "SubnetMask,"
                    + "GateWayIP,"
                    + "ServerIP1,"
                    + "ServerIP2,"
                    + "ListenPort,"
                    + "ServerPort,"
                    + "Zone,"
                    + "ReaderModel "
                    + "From readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            rs.first();
            for (int i = 2; i <= 35; i++) {
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

    private void FillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession){
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            String query = "Select RDR_No FROM readersetting";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(rs.getString(1));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveIPSettingsToDB(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int reader_no = Integer.parseInt(myHTTPConn.readLine());

            String query = "UPDATE readersetting SET "
                    + "Location='" + dp.getString() + "' ,"
                    + "Division='" + dp.getString() + "' ,"
                    + "SelfIP='" + dp.getString() + "' ,"
                    + "SubnetMask='" + dp.getString() + "' ,"
                    + "GateWayIP='" + dp.getString() + "' ,"
                    + "ServerIP1='" + dp.getString() + "' ,"
                    + "ServerIP2='" + dp.getString() + "' ,"
                    + "ListenPort='" + dp.getString() + "' ,"
                    + "ServerPort='" + dp.getString() + "' "
                    + "where RDR_No='" + reader_no + "'";
            stmt.executeUpdate(query);

            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveMenu1ToDB(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int reader_no = Integer.parseInt(myHTTPConn.readLine());

            String query = "UPDATE readersetting SET "
                    + "ReaderNo='" + dp.getInt() + "' ,"
                    + "BaudRate='" + dp.getInt() + "' ,"
                    + "ReaderMode='" + dp.getInt() + "' ,"
                    + "APBLevel='" + dp.getInt() + "' ,"
                    + "UseRdrDB='" + dp.getInt() + "' ,"
                    + "CheckShiftHoliday='" + dp.getInt() + "' ,"
                    + "CheckPIN='" + dp.getInt() + "' ,"
                    + "CheckValidityDOB='" + dp.getInt() + "' ,"
                    + "AutoOffline='" + dp.getInt() + "' "
                    + "where RDR_No='" + reader_no + "'";
            stmt.executeUpdate(query);

            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveMenu2ToDB(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int reader_no = Integer.parseInt(myHTTPConn.readLine());

            String query = "UPDATE readersetting SET "
                    + "DoorDelay='" + dp.getInt() + "' ,"
                    + "LEDDelay='" + dp.getInt() + "' ,"
                    + "DoorOpenAlarm='" + dp.getInt() + "' ,"
                    + "KeyTmOut='" + dp.getInt() + "' ,"
                    + "OnlineRespTimeout='" + dp.getInt() + "' ,"
                    + "MultiRejAlarm='" + dp.getInt() + "' ,"
                    + "AppID1='" + dp.getInt() + "' ,"
                    + "AppID2='" + dp.getInt() + "' "
                    + "where RDR_No='" + reader_no + "'";
            stmt.executeUpdate(query);

            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void saveMenu3ToDB(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int reader_no = Integer.parseInt(myHTTPConn.readLine());

            String query = "UPDATE readersetting SET "
                    + "ReaderString='" + dp.getString() + "' "
                    + "where RDR_No='" + reader_no + "'";
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
