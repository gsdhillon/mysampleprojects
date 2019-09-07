/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.reader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.readerComm.RdrCommand;
import lib.readerComm.RdrMemAddr;
import lib.readerComm.ReaderControl;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author GAURAV
 */
public class RSCommServlet extends HttpServlet {

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
                //  LoginSession.TYPE_USER,
                //  LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("GetReaderInfo")) {
                GetReaderInfo(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetReaderIPSettings")) {
                SetReaderIPSettings(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetReaderMenu1")) {
                SetReaderMenu1(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetReaderMenu2")) {
                SetReaderMenu2(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetReaderMenu3")) {
                SetReaderMenu3(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetKey")) {
                SetKey(myHTTPConn, loginSession);
            } else if (ReqType.equals("GetOutputStatus")) {
                GetOutputStatus(myHTTPConn, loginSession);
            } else if (ReqType.equals("SetOutputStatus")) {
                SetOutputStatus(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateConfig")) {
                UpdateConfig(myHTTPConn, loginSession);
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

    private void SetReaderIPSettings(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String packet = myHTTPConn.readLine();
            byte[] data = CreateArrayIPSettings(packet);
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);

            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_IPSettings, 13, (byte) 26, data)) {
                myHTTPConn.println("IPSettings Set");
            } else {
                throw new Exception("Unable to set IPSettings");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void SetReaderMenu1(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }


            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String packet = myHTTPConn.readLine();
            byte[] data = CreateArrayMenu1(packet);
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Menu1, 6, (byte) 12, data)) {
                myHTTPConn.println("Menu 1 Set");
            } else {
                throw new Exception("Unable to set Menu1");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void SetReaderMenu2(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String packet = myHTTPConn.readLine();
            byte[] data = CreateArrayMenu2(packet);
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Menu2, 6, (byte) 12, data)) {
                myHTTPConn.println("Menu 2 Set");
            } else {
                throw new Exception("Unable to set Menu 2");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void SetReaderMenu3(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String packet = myHTTPConn.readLine();
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            ReaderControl.setAddress(ip);
            Depacketizer dp = new Depacketizer(packet);
            boolean recOnS1 = dp.getBool();
            if (recOnS1) {
                byte[] data = {((Integer) dp.getInt()).byteValue(), ((Integer) dp.getInt()).byteValue()};
                if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Pointer + 2, 1, (byte) 2, data)) {
                } else {
                    throw new Exception("Unable to set Record on Server 1");
                }
            }
            boolean recOnS2 = dp.getBool();
            if (recOnS2) {
                byte[] data = {((Integer) dp.getInt()).byteValue(), ((Integer) dp.getInt()).byteValue()};
                if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Pointer + 4, 1, (byte) 2, data)) {
                } else {
                    throw new Exception("Unable to set Record on Server 2");
                }
            }

            boolean rdrStr = dp.getBool();
            if (rdrStr) {
                byte[] data = CreateArrayRdrStr(dp.getString());
                if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_string, 8, (byte) 16, data)) {
                } else {
                    throw new Exception("Unable to set Reader String");
                }
            }

            boolean setDate = dp.getBool();
            if (setDate) {
                byte[] data = CreateArrayDate();
                if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 4, (byte) 8, data)) {
                } else {
                    throw new Exception("Unable to set Date Time ");
                }

            }
            myHTTPConn.println("Set MENU 3");

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void UpdateConfig(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            ReaderControl.setAddress(ip);
            byte[] data = new byte[2];
            data[0] = RdrCommand.rdr_CMD_UpdateConfig;
            data[1] = 0;
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 1, (byte) 2, data)) {
            } else {
                throw new Exception("Unable to Save Config ");
            }

            myHTTPConn.println("Config Save");

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void GetReaderInfo(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int ReaderNo = dp.getInt();
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + (ReaderNo) + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);

            ReaderControl.setAddress(ip);

            int[] menu2 = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_Menu1, 12);
            Packetizer p = new Packetizer();
            for (int i = 0; i < menu2.length; i++) {
                p.addInt(menu2[i]);
            }
            int[] menu1 = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_IPSettings, 13);
            Packetizer q = new Packetizer();
            for (int i = 0; i < menu1.length; i++) {
                q.addInt(menu1[i]);
            }
            int[] rdrString = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_string, 8);

            Packetizer r = new Packetizer();
            for (int i = 0; i < rdrString.length; i++) {
                r.addInt(rdrString[i]);
            }

            int[] recordPointer = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_Pointer, 5);

            Packetizer s = new Packetizer();
            for (int i = 0; i < recordPointer.length; i++) {
                s.addInt(recordPointer[i]);
            }
            byte[] data = new byte[2];
            data[0] = RdrCommand.rdr_CMD_GetTime;
            data[1] = 0;
            Packetizer t = new Packetizer();
            if (ReaderControl.writeFrameCheck((byte) rdrno, RdrMemAddr.rdrAdd_Command, 1, (byte) 2, data)) {
                int[] dateTime = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 4);
                for (int i = 0; i < dateTime.length; i++) {
                    t.addInt(dateTime[i]);
                }
            } else {
                throw new Exception("Datetime not retrieved successfully");
            }
            Packetizer finalPacket = new Packetizer();
            finalPacket.addString(p.getPacket());
            finalPacket.addString(q.getPacket());
            finalPacket.addString(r.getPacket());
            finalPacket.addString(s.getPacket());
            finalPacket.addString(t.getPacket());
            myHTTPConn.println(finalPacket.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private static byte[] CreateArrayMenu1(String packet) throws Exception {
        byte[] data = new byte[12];
        Depacketizer dp = new Depacketizer(packet);
        for (int i = 0; i < 12; i++) {
            data[i] = ((Integer) dp.getInt()).byteValue();
        }
        return data;
    }

    private static byte[] CreateArrayIPSettings(String packet) throws Exception {
        byte[] data = new byte[26];
        Depacketizer dp = new Depacketizer(packet);
        for (int i = 0; i < 26; i++) {
            data[i] = ((Integer) dp.getInt()).byteValue();
        }
        return data;
    }

    private static byte[] CreateArrayDate() throws Exception {

        Calendar c = Calendar.getInstance();
        byte[] data = new byte[8];

        data[0] = RdrCommand.rdr_CMD_ChangeTime;
        data[1] = (byte) c.get(Calendar.DATE);
        data[2] = (byte) (c.get(Calendar.MONTH) + 1);
        data[3] = (byte) (c.get(Calendar.YEAR) - 2000);
        data[4] = (byte) c.get(Calendar.HOUR_OF_DAY);
        data[5] = (byte) c.get(Calendar.MINUTE);
        data[6] = (byte) c.get(Calendar.SECOND);
        data[7] = (byte) (c.get(Calendar.DAY_OF_WEEK) - 1);

        return data;
    }

    private byte[] CreateArrayMenu2(String packet) throws Exception {
        byte[] data = new byte[12];
        Depacketizer dp = new Depacketizer(packet);
        for (int i = 0; i < 12; i++) {
            data[i] = ((Integer) dp.getInt()).byteValue();

        }
        return data;
    }

    private byte[] CreateArrayRdrStr(String rdrStr) throws Exception {
        byte[] data = new byte[16];
        byte[] bytes = rdrStr.getBytes();
        int i = 0;
        while (i < 16) {
            if (i < bytes.length) {
                data[i] = bytes[i];
            } else {
                data[i] = 0x20;
            }
            i++;
        }

        return data;
    }

    private void SetKey(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            int ReaderNo = 0;
            String packet = myHTTPConn.readLine();
            int cntReader = Integer.parseInt(myHTTPConn.readLine());
            String readerPacket = myHTTPConn.readLine();

            Depacketizer dp = new Depacketizer(readerPacket);
            Packetizer p = new Packetizer();
            p.setCounter();
            for (int i = 0; i < cntReader; i++) {
                try {
                    ReaderNo = dp.getInt();
                    String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ((int) ReaderNo) + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                    String ip = rs.getString(1);
                    int rdrno = rs.getInt(2);
                    rs.close();
                    ReaderControl.setAddress(ip);
                    byte[] data = CreateArrayKeySet(packet);
                    if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 5, (byte) 10, data)) {
                        p.addString("OK:" + i + ":" + ReaderNo + ":" + ip);
                    } else {
                        p.addString("FAILED:" + i + ":" + ReaderNo + ":" + ip);
                    }
                } catch (Exception e1) {
                    p.addString("Exception:" + i + ":" + e1.getMessage());
                } finally {
                    p.incrCounter();
                }
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private static byte[] CreateArrayKeySet(String packet) throws Exception {
        byte[] data = new byte[10];
        try {
            Depacketizer d = new Depacketizer(packet);

            data[0] = RdrCommand.rdr_CMD_ChangeKey;
            data[1] = (byte) d.getInt();
            data[2] = (byte) d.getInt();

            String strKeyword = d.getString();
            byte bytes[] = strKeyword.getBytes();

            data[3] = bytes[0];
            data[4] = bytes[1];
            data[5] = bytes[2];
            data[6] = bytes[3];
            data[7] = bytes[4];
            data[8] = bytes[5];

        } catch (Exception e) {
            throw e;
        }
        return data;
    }

    private void GetOutputStatus(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            int readerNo = Integer.parseInt(dp.getString());

            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + readerNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);

            ReaderControl.setAddress(ip);
            int[] outputStatus = ReaderControl.readFrame((byte) rdrno, RdrMemAddr.rdrAdd_OutputStruct, 10);

            Packetizer p = new Packetizer();
            for (int i = 0; i < outputStatus.length; i++) {
                p.addInt(outputStatus[i]);
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void SetOutputStatus(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            int ReaderNo = Integer.parseInt(myHTTPConn.readLine());
            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + ReaderNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);

            String packet = myHTTPConn.readLine();
            byte[] data = CreateArrayOutputStatus(packet);
            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_OutputStruct, 10, (byte) 20, data)) {
                myHTTPConn.println("Output Status set");
            } else {
                throw new Exception("Unable to set OutputStatus");
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private byte[] CreateArrayOutputStatus(String packet) throws Exception {
        byte[] data = new byte[20];
        Depacketizer dp = new Depacketizer(packet);
        for (int i = 0; i < 20; i++) {
            data[i] = ((Integer) dp.getInt()).byteValue();
        }
        return data;
    }
}
