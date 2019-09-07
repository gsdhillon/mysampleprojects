/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javacodes.services.servlet.EmployeeFormServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbp
 */
public class ReaderMasterFormServlet extends HttpServlet {

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

            if (ReqType.equals("DeleteReader")) {
                String catcode = myHTTPConn.readLine();
                DeleteReader(myHTTPConn, catcode, loginSession);
            } else if (ReqType.equals("AddReader")) {
                AddReader(myHTTPConn, loginSession);
            } else if (ReqType.equals("getReaderList")) {
                getReaderList(myHTTPConn);
            } else if (ReqType.equals("UpdateReader")) {
                UpdateReaderMaster(myHTTPConn, loginSession);
            } else if (ReqType.equals("ReaderDetails")) {
                getReaderDetails(myHTTPConn, loginSession);

            } else {
                myHTTPConn.println("unknownRequest");
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            Logger.getLogger(EmployeeFormServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    private void getReaderDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query = "Select Location,Division,ReaderModel,Zone,SelfIP,ServerIP1,SubnetMask,GateWayIP,ServerPort,ListenPort From readersetting where RDR_No ='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            rs.next();
            for (int i = 1; i < 11; i++) {
                p.addString(rs.getString(i));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void UpdateReaderMaster(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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

            int readerNo = dp.getInt();
            String location = dp.getString();
            String div = dp.getString();
            int readerModel = dp.getInt();
            int readerZone = dp.getInt();
            String selfIP = dp.getString();
            String serverIP = dp.getString();
            String subMask = dp.getString();
            String gateIP = dp.getString();
            String serverPort = dp.getString();
            String listenPort = dp.getString();

            String query = "UPDATE "
                    + "readersetting SET"
                    + " Location='" + location
                    + "',Division='" + div
                    + "',ReaderModel='" + readerModel
                    + "',Zone='" + readerZone
                    + "',SelfIP='" + selfIP
                    + "',ServerIP1='" + serverIP
                    + "',SubnetMask='" + subMask
                    + "',GateWayIP='" + gateIP
                    + "',ServerPort='" + serverPort
                    + "',ListenPort='" + listenPort
                    + "' WHERE RDR_No = '" + readerNo + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddReader(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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

            int readerNo = dp.getInt();
            String location = dp.getString();
            String div = dp.getString();
            int readerModel = dp.getInt();
            int readerZone = dp.getInt();
            String selfIP = dp.getString();
            String serverIP = dp.getString();
            String subMask = dp.getString();
            String gateIP = dp.getString();
            String serverPort = dp.getString();
            String listenPort = dp.getString();

            String ServerIP2 = "192.168.0.0";
            int BaudRate = 0;
            int ReaderMode = 0;
            int ReaderTypeLevel = 0;
            int UseRdrDB = 0;
            int CheckShiftHoliday = 2;
            int CheckPIN = 0;
            int CheckValidityDOB = 0;
            int AutoOffline = 0;
            int AntiPassBack = 0;
            int APBLevel = 2;
            int StoreAllRec = 0;
            int MultiRejAlarm = 0;
            int DoorDelay = 2;
            int LEDDelay = 2;
            int InvalidAlarm = 0;
            int TamperAlarm = 0;
            int DoorOpenAlarm = 0;
            int OnlineRespTimeout = 0;
            int KeyTmOut = 0;
            int AppID1 = 0;
            int AppID2 = 0;
            int ReaderType = 0;


//        String query = "INSERT INTO `readersetting` (`ReaderNo`, `Location`, `Division`,`ReaderModel`, `Zone`, `SelfIP`, `ServerIP1`,`SubnetMask`,`GateWayIP`,`ServerPort`,ListenPort) VALUES (" + readerNo + ",'" + location + "','" + dept + "'," + readerModel + "," + readerZone + ",'" + selfIP + "','" + serverIP + "','" + subMask + "','" + gateIP + "','" + serverPort + "','" + listenPort + "')";
//        String query = "INSERT INTO `readersetting` (`ReaderNo`, `Location`, `Division`,`ReaderModel`, `Zone`, `SelfIP`, `ServerIP1`,`SubnetMask`,`GateWayIP`,`ServerPort`,ListenPort,BaudRate,ReaderMode,ReaderTypeLevel,UseRdrDB,CheckShiftHoliday,CheckPIN,CheckValidityDOB,AutoOffline,AntiPassBack,APBLevel,StoreAllRec,MultiRejAlarm,DoorDelay,LEDDelay,InvalidAlarm,TamperAlarm,DoorOpenAlarm,OnlineRespTimeout,KeyTmOut,AppID1,AppID2,ReaderType) VALUES (" + readerNo + ",'" + location + "','" + dept + "'," + readerModel + "," + readerZone + ",'" + selfIP + "','" + serverIP + "','" + subMask + "','" + gateIP + "','" + serverPort + "','" + listenPort + "','" + BaudRate + "','" + ReaderMode + "','" + ReaderTypeLevel + "','" + UseRdrDB + "','" + CheckShiftHoliday + "','" + CheckPIN + "','" + CheckValidityDOB + "','" + AutoOffline + "','" + AntiPassBack + "','" + APBLevel + "','" + StoreAllRec + "','" + MultiRejAlarm + "','" + DoorDelay + "','" + LEDDelay + "','" + InvalidAlarm + "','" + TamperAlarm + "','" + DoorOpenAlarm + "','" + OnlineRespTimeout + "','" + KeyTmOut + "','" + AppID1 + "','" + AppID2 + "','" + ReaderType + "')";
            String query = "INSERT INTO `readersetting` "
                    + "(`RDR_No`, "
                    + "`Location`, "
                    + "`Division`,"
                    + "`ReaderModel`, "
                    + "`Zone`, "
                    + "`SelfIP`, "
                    + "`ServerIP1`,"
                    + "`SubnetMask`,"
                    + "`GateWayIP`,"
                    + "`ServerPort`,"
                    + "ListenPort,"
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
                    + "ServerIP2,"
                    + "ReaderType) VALUES ("
                    + readerNo + ",'"
                    + location + "','"
                    + div + "',"
                    + readerModel + ","
                    + readerZone + ",'"
                    + selfIP + "','"
                    + serverIP + "','"
                    + subMask + "','"
                    + gateIP + "','"
                    + serverPort + "','"
                    + listenPort + "','"
                    + BaudRate + "','"
                    + ReaderMode + "','"
                    + ReaderTypeLevel + "','"
                    + UseRdrDB + "','"
                    + CheckShiftHoliday + "','"
                    + CheckPIN + "','"
                    + CheckValidityDOB + "','"
                    + AutoOffline + "','"
                    + AntiPassBack + "','"
                    + APBLevel + "','"
                    + StoreAllRec + "','"
                    + MultiRejAlarm + "','"
                    + DoorDelay + "','"
                    + LEDDelay + "','"
                    + InvalidAlarm + "','"
                    + TamperAlarm + "','"
                    + DoorOpenAlarm + "','"
                    + OnlineRespTimeout + "','"
                    + KeyTmOut + "','"
                    + AppID1 + "','"
                    + AppID2 + "','"
                    + ServerIP2 + "','"
                    + ReaderType + "')";
            stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteReader(MyHTTPConnection myHTTPConn, String id, LoginSession loginSession) {
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

            query = "DELETE FROM `readersetting` WHERE `RDR_No`='" + id + "'";
            int executeUpdate = stmt.executeUpdate(query);

            myHTTPConn.println("Deleted" + id + "  " + executeUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    /**
     *
     * @param myHTTPConn
     */
    private void getReaderList(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            //get connection
            conn = AppContext.getDBConnection();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String query = "select RDR_No,Location,Division,ReaderType,SelfIP,ServerIP1,Zone from readersetting";
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
}
