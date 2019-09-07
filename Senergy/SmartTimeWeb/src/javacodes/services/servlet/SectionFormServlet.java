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
public class SectionFormServlet extends HttpServlet {

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

            if (ReqType.equals("DeleteSection")) {
                String id = myHTTPConn.readLine();
                DeleteSection(myHTTPConn, id, loginSession);
            } else if (ReqType.equals("AddSection")) {
                AddSection(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateSection")) {
                UpdateSection(myHTTPConn, loginSession);
            } else if (ReqType.equals("empName")) {
                empName(myHTTPConn, loginSession);
            } else if (ReqType.equals("getSectionList")) {
                getSectionList(myHTTPConn, loginSession);
            } else if (ReqType.equals("DivCode")) {
                DivCode(myHTTPConn, loginSession);
            } else if (ReqType.equals("DivName")) {
                DivName(myHTTPConn, loginSession);
            } else if (ReqType.equals("FillForm")) {
                FillForm(myHTTPConn, loginSession);
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

    private void FillForm(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String SecCode = myHTTPConn.readLine();
            Packetizer p = new Packetizer();
            String query = "SELECT * FROM sectionmaster where SecCode='" + SecCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            for (int i = 1; i < 6; i++) {
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

    private void DivName(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String d = myHTTPConn.readLine();
            int DivCode = Integer.parseInt(d);
            query = "SELECT DivName FROM division where DivCode='" + DivCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            myHTTPConn.println(rs.getString(1));
            rs.close();
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DivCode(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            Packetizer p = new Packetizer();

            query = "SELECT DivCode FROM division";
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {
                p.addString(rs2.getString(1));
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

    private void empName(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            query = "Select EmpCode,EmpName From employeemaster";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString((1)));
                p.addString(rs.getString(2));
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

    private void UpdateSection(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SecCode = dp.getString();
            String SecName = dp.getString();
            String SecHeadCode = dp.getString();
            String SecHeadName = dp.getString();
            String DivCode = dp.getString();
            String query = "UPDATE "
                    + "sectionmaster SET SecName='" + SecName + "',SecHeadCode='" + SecHeadCode
                    + "',SecHeadName='" + SecHeadName + "',DivCode='" + DivCode + "' WHERE SecCode='" + SecCode + "'";
            stmt.executeUpdate(query);
            setEmpStatus(myHTTPConn, SecHeadCode, loginSession);
            myHTTPConn.println("Updated");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void AddSection(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SecCode = dp.getString();
            String SecName = dp.getString();
            String SecHeadCode = dp.getString();
            String SecHeadName = dp.getString();
            String DivCode = dp.getString();
            String query = "INSERT INTO "
                    + "sectionmaster (SecCode,SecName,SecHeadCode,SecHeadName,DivCode) Values('"
                    + SecCode + "','"
                    + SecName + "','"
                    + SecHeadCode + "','"
                    + SecHeadName + "','"
                    + DivCode + "')";
            stmt.executeUpdate(query);
            setEmpStatus(myHTTPConn, SecHeadCode, loginSession);
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteSection(MyHTTPConnection myHTTPConn, String id, LoginSession loginSession) {
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

            query = "SELECT SecHeadCode FROM sectionmaster WHERE `SecCode`='" + id + "'";
            ResultSet rs = stmt.executeQuery(query);
            String headcode = "";
            if (rs.next()) {
                headcode = rs.getString(1);
            }
            query = "DELETE FROM `sectionmaster` WHERE `SecCode`='" + id + "'";
            int executeUpdate = stmt.executeUpdate(query);
            removeEmpStatus(myHTTPConn, headcode, loginSession);

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
    private void getSectionList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
                    + "DivCode, "
                    + "DivName, "
                    + "DivHeadName, "
                    + "DivHeadCode "
                    + "FROM "
                    + "division";
            query = "SELECT DivName,SecCode,SecName,SecHeadName,SecHeadCode FROM "
                    + "( SELECT DivCode,DivName FROM division ORDER BY DivCode )"
                    + " division,sectionmaster where sectionmaster.DivCode=division.DivCode";
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

    private void setEmpStatus(MyHTTPConnection myHTTPConn, String HeadCode, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "Update employeemaster SET EmpStatus=3 where EmpCode='" + HeadCode + "'";
            stmt.executeUpdate(query);
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void removeEmpStatus(MyHTTPConnection myHTTPConn, String HeadCode, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query = "Select SecName from sectionmaster where SecHeadCode='" + HeadCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                query = "Select DivName from division where DivHeadCode='" + HeadCode + "'";
                ResultSet rs1 = stmt.executeQuery(query);
                if (!rs1.next()) {
                    query = "Update employeemaster SET EmpStatus=0 where EmpCode='" + HeadCode + "'";
                    rs1.close();
                    rs.close();
                }
                stmt.executeUpdate(query);
                stmt.close();
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
