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
public class SPatternFormServlet extends HttpServlet {

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

            if (ReqType.equals("DeleteSPattern")) {
                DeleteSPattern(myHTTPConn, loginSession);
            } else if (ReqType.equals("AddSPattern")) {
                AddSPattern(myHTTPConn, loginSession);
            } else if (ReqType.equals("getSPatternList")) {
                getSPatternList(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateSPattern")) {
                UpdateSPattern(myHTTPConn, loginSession);
            } else if (ReqType.equals("ShiftList")) {
                ShiftList(myHTTPConn, loginSession);
            } else if (ReqType.equals("FillForm")) {
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
            String SPCode = myHTTPConn.readLine();
            String query;
            query = "SELECT * FROM shiftpattern WHERE PatternCode='" + SPCode + "'";


            Packetizer p = new Packetizer();
            p.setCounter();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
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

    private void ShiftList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            p.setCounter();
            String query = "Select ShiftCode FROM shiftmaster";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                p.addString(rs.getString(1));
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

    private void UpdateSPattern(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Packet = myHTTPConn.readLine(), query;
            Depacketizer dp = new Depacketizer(Packet);
            int HPosition = -1;
            String SPatternCode = dp.getString(), Position, Shift, Days;
            while (!dp.isEmpty()) {
                Position = dp.getString();
                Shift = dp.getString();
                Days = dp.getString();
                query = "UPDATE `shiftpattern` SET ShiftCode='" + Shift + "',Days='" + Days + "' WHERE PatternCode='"
                        + SPatternCode + "' AND Position='" + Position + "'";
                int result = stmt.executeUpdate(query);
                if (result == 0) {
                    query = "INSERT INTO `shiftpattern` (`PatternCode`, `Position`, `ShiftCode`, `Days`) VALUES"
                            + " ('" + SPatternCode + "','" + Position + "','" + Shift + "','" + Days + "')";
                    stmt.executeUpdate(query);
                }
                if (HPosition < Integer.parseInt(Position)) {
                    HPosition = Integer.parseInt(Position);
                }

            }
            for (int i = HPosition + 1; i < 10; i++) {
                query = "DELETE FROM shiftpattern WHERE PatternCode='" + SPatternCode + "' AND Position='" + i + "'";
                stmt.executeUpdate(query);
            }
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddSPattern(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Packet = myHTTPConn.readLine(), query = "";
            Depacketizer dp = new Depacketizer(Packet);

            String SPatternCode = dp.getString();
            while (!dp.isEmpty()) {
                query = "INSERT INTO `shiftpattern` (`PatternCode`, `Position`, `ShiftCode`, `Days`) VALUES"
                        + " ('" + SPatternCode + "','" + dp.getString() + "','" + dp.getString() + "','" + dp.getString() + "')";
                stmt.executeUpdate(query);
            }

            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteSPattern(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String SPCode = myHTTPConn.readLine();
            String query;

            query = "DELETE FROM `shiftpattern` WHERE `PatternCode`='" + SPCode + "'";
            stmt.executeUpdate(query);

            myHTTPConn.println("Deleted");
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
    private void getSPatternList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            Packetizer q = new Packetizer();
            q.setCounter();

            query = "Select DISTINCT PatternCode FROM shiftpattern";
            ResultSet rs1 = stmt.executeQuery(query);
            while (rs1.next()) {
                q.addString(rs1.getString(1));
                q.incrCounter();
            }
            myHTTPConn.println(q.getPacket());
            rs1.close();
            query = "SELECT * FROM shiftpattern";


            Packetizer p = new Packetizer();
            p.setCounter();
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {
                p.addString(rs2.getString(1));
                p.addString(rs2.getString(2));
                p.addString(rs2.getString(3));
                p.addString(rs2.getString(4));
                p.incrCounter();
            }
            rs2.close();
            myHTTPConn.println(p.getPacket());

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
