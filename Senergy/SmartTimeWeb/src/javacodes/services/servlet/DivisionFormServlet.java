/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.services.servlet;

import java.io.IOException;
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
public class DivisionFormServlet extends HttpServlet {

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

            if (ReqType.equals("DeleteDivision")) {
                String id = myHTTPConn.readLine();
                DeleteDivision(myHTTPConn, id, loginSession);
            } else if (ReqType.equals("AddDivision")) {
                addDivision(myHTTPConn, loginSession);
            } else if (ReqType.equals("empName")) {
                empName(myHTTPConn, loginSession);
            } else if (ReqType.equals("getDivisionList")) {
                getDivisionList(myHTTPConn, loginSession);
            } else if (ReqType.equals("FillForm")) {
                FillForm(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateDivision")) {
                updateDivision(myHTTPConn, loginSession);
            } else if (ReqType.equals("GetDivHeadCode")) {
                getDivHeadCode(myHTTPConn, loginSession);
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

            String DivCode = myHTTPConn.readLine();
            Packetizer p = new Packetizer();
            String query = "SELECT * FROM division WHERE DivCode='" + DivCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            for (int i = 1; i < 5; i++) {
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

    private void updateDivision(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query;
            Depacketizer dp = new Depacketizer(Packet);
            String DivCode = dp.getString();
            String DivName = dp.getString();
            String DivHeadCode = dp.getString();
            String DivHeadName = dp.getString();
            query = "UPDATE division SET DivName='" + DivName
                    + "',DivHeadCode='" + DivHeadCode
                    + "',DivHeadName='" + DivHeadName + "' WHERE DivCode='" + DivCode + "'";
            stmt.executeUpdate(query);

            setEmpStatus(myHTTPConn, "" + DivHeadCode, loginSession);
            stmt.close();
            myHTTPConn.println("Updated");
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
            String query = "Select EmpCode,EmpName From employeemaster";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString((1)));
                p.addString(rs.getString(2));
                p.incrCounter();
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void addDivision(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query;
            Depacketizer dp = new Depacketizer(Packet);
            String DivCode = dp.getString();
            String DivName = dp.getString();
            String DivHeadCode = dp.getString();
            String DivHeadName = dp.getString();
            query = "INSERT INTO "
                    + "division(DivCode,DivName,DivHeadCode,DivHeadName) Values('"
                    + DivCode + "','"
                    + DivName + "','"
                    + DivHeadCode + "','"
                    + DivHeadName + "')";
            stmt.executeUpdate(query);
            setEmpStatus(myHTTPConn, "" + DivHeadCode, loginSession);
            stmt.close();
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteDivision(MyHTTPConnection myHTTPConn, String id, LoginSession loginSession) {
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
            query = "SELECT DivHeadCode FROM division WHERE `DivCode`='" + id + "'";
            ResultSet rs = stmt.executeQuery(query);
            String headcode = "";
            if (rs.next()) {
                headcode = rs.getString(1);
            }
            query = "DELETE FROM `division` WHERE `DivCode`='" + id + "'";
            int executeUpdate = stmt.executeUpdate(query);
            removeEmpStatus(myHTTPConn, "" + headcode, loginSession);
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
    private void getDivisionList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String query =
                    "SELECT "
                    + "DivCode, "
                    + "DivName, "
                    + "DivHeadName, "
                    + "DivHeadCode "
                    + "FROM "
                    + "division";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
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
            String query = "Select sectionmaster.SecName,division.DivName from sectionmaster,division where sectionmaster.DivCode=division.DivCode and SecHeadCode=" + "'" + HeadCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                query = "Update employeemaster SET EmpStatus=0 where EmpCode='" + HeadCode + "'";
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getDivHeadCode(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String divheadcode = myHTTPConn.readLine();
            String query = "Select EmpName from employeemaster where EmpCode='" + divheadcode + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String EmpName=rs.getString(1);
                if(!("".equals(EmpName))){
                myHTTPConn.println(EmpName);
                }else{
                    myHTTPConn.println("Not Found");
                }
            } else {
                myHTTPConn.println("Not Found");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
