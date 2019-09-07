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
import lib.readerComm.RdrMemAddr;
import lib.readerComm.ReaderControl;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author nbp
 */
public class EmpDataSyncServlet extends HttpServlet {

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
            switch (ReqType) {
                case "sendUID":
                    callWriteFrame(myHTTPConn, loginSession);
                    break;
                case "GetAssignedReader":
                    getAssignedReader(myHTTPConn, loginSession);
                    break;
                case "getEmployeeList":
                    getEmployeeList(myHTTPConn, loginSession);
                    break;
                case "EraseAllEmp":
                    EraseAllEmp(myHTTPConn, loginSession);
                    break;
                default:
                    myHTTPConn.println("unknownRequest");
                    break;
            }

        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());

        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    private void getAssignedReader(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String list = myHTTPConn.readLine();
            list = list.replaceAll("\\[", "");
            list = list.replaceAll("\\]", "");
            String empUID[] = list.split(",");
            String query;
            Packetizer packet = new Packetizer();
            packet.setCounter();
            for (int i = 0; i < empUID.length; i++) {
                Packetizer p = new Packetizer();
                //int UID = Integer.parseInt((empUID[i]).trim());
                query = "select readeraccess.AccessString from readeraccess,employeemaster where employeemaster.EmpCode='" + (empUID[i]).trim() + "' and employeemaster.AccessLevel=readeraccess.AccessLevel";
                //query = "select Reader from employeemaster where UID = '" + (empUID[i]).trim() + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    p.addString(rs.getString(1));
                    p.addString((empUID[i]).trim());
                    packet.addString(p.getPacket());
                    packet.incrCounter();
                }
                rs.close();
            }
            myHTTPConn.println(packet.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getEmployeeList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "select UID,EmpName,EmpCode from employeemaster where UID <>'0'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            int count = 0;
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                count++;
            }
            rs.close();
            myHTTPConn.println(p.getPacket());
            myHTTPConn.println("" + count);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void callWriteFrame(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            Depacketizer d = new Depacketizer(packet);
            byte byteData[] = new byte[12];
            int readerNo = d.getInt();
            int command = d.getInt();
            byteData[0] = (byte) command;
            byteData[1] = (byte) d.getInt();
            byteData[2] = (byte) d.getInt();
            byteData[3] = (byte) d.getInt();
            byteData[4] = (byte) d.getInt();

            byteData[5] = 0;
            byteData[6] = 0;
            byteData[7] = 0;
            byteData[8] = 0;
            byteData[9] = 0;
            byteData[10] = 0;
            byteData[11] = 0;

            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + readerNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            rs.close();
            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 6, (byte) 12, byteData)) {
                //send readframe to validate synchronization.....
                myHTTPConn.println(checkFrame((byte) rdrno));
            } else {
                throw new Exception("Unable to set Record on Server 1");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private String checkFrame(byte readerNo) throws Exception {
        Packetizer p = new Packetizer();
        p.setCounter();
        int[] validateWrite = ReaderControl.readFrame(readerNo, RdrMemAddr.rdrAdd_Command, 5);
        for (int i : validateWrite) {
            p.addInt(i);
            p.incrCounter();
        }
        return p.getPacket();
    }

    private void EraseAllEmp(MyHTTPConnection myHTTPConn, LoginSession loginSession) throws Exception, NullPointerException {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }

            byte byteData[] = new byte[2];
            int readerNo = Integer.parseInt(myHTTPConn.readLine());

            byteData[0] = (byte) 186;
            byteData[1] = 1;


            String query = "Select SelfIP,ReaderNo FROM readersetting WHERE RDR_No='" + readerNo + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String ip = rs.getString(1);
            int rdrno = rs.getInt(2);
            rs.close();
            ReaderControl.setAddress(ip);
            if (ReaderControl.writeFrame((byte) rdrno, RdrMemAddr.rdrAdd_Command, 1, (byte) 2, byteData)) {
                //send readframe to validate synchronization.....
            } else {
                throw new Exception("Unable to set Record on Server 1");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
