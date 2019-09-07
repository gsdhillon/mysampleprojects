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
public class EmployeeFormServlet extends HttpServlet {

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

            if (ReqType.equals("InitValues")) {
                InitValues(myHTTPConn, loginSession);
            } else if (ReqType.equals("DivCode")) {
                DivCode(myHTTPConn, loginSession);
            } else if (ReqType.equals("CatCode")) {
                CatCode(myHTTPConn, loginSession);
            } else if (ReqType.equals("SecName")) {
                SecName(myHTTPConn, loginSession);
            } else if (ReqType.equals("DeleteEmployee")) {
                DeleteEmployee(myHTTPConn, loginSession);
            } else if (ReqType.equals("FormDetails")) {
                FormDetails(myHTTPConn, loginSession);
            } else if (ReqType.equals("AddEmployee")) {
                AddEmployee(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateEmployee")) {
                UpdateEmployee(myHTTPConn, loginSession);
            } else {
                myHTTPConn.println("Unknown Request" + ReqType);
            }
        } catch (Exception ex) {
            myHTTPConn.println("ERROR: " + ex.getMessage());
            AppContext.log(loginSession, ex);
        } finally {
            AppContext.close(myHTTPConn);
        }


    }

    private void AddEmployee(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String Packet = myHTTPConn.readLine();

            Depacketizer dp = new Depacketizer(Packet);

            String ccnumber = dp.getString();
            String employeecode = dp.getString();
            String employeename = dp.getString();
            String desig = dp.getString();
            int sex = dp.getInt();
            String dob = dp.getString();
            String doj = dp.getString();
            String dol = dp.getString();
            String DivName = dp.getString();
            String Workl = dp.getString();
            String div_Code = dp.getString();
            String SecCode = dp.getString();
            String DShift = dp.getString();
            String CatCode = dp.getString();
            String LCode = dp.getString();
            int WOff1 = dp.getInt();
            int WOff2 = dp.getInt();
            int SSwipe = dp.getInt();
            String Addr = dp.getString();
            String report_to = dp.getString();
            int prebalLeave = dp.getInt();
            int accesslevel = dp.getInt();
            int postid = dp.getInt();

            query = "INSERT INTO `employeemaster` (`"
                    + "EmpName`, "
                    + "`EmpCode`, "
                    + "`CCNo`, "
                    + "`UnitCode`, "
                    + "`Sex`,"
                    + "`Password`, "
                    + "`Pin`, "
                    + "`DOB`, "
                    + "`Validity`, "
                    + "`Joining`, "
                    + "`Division`, "
                    + "`UID`, "
                    + "DesigID, "
                    + "`wlocation_code`"
                    + ",`Reader`, "
                    + "`PinCheck`, "
                    + "`EnableBio`, "
                    + "`AccessLevel`, "
                    + "`ZoneTime`, "
                    + "`currentZone`, "
                    + "`DivCode` ,"
                    + "`SecCode`,"
                    + "`ShiftCode`, "
                    + "`CatCode`, "
                    + "`LeaveCode`,"
                    + "`WeekOff1`,"
                    + "`WeekOff2`,"
                    + "`SingSwAllowed`,"
                    + "`Address`,"
                    + "`PreLeaveBal`,"
                    + "PostCode,"
                    + "`reportingto`)"
                    + " VALUES ("
                    + "'" + employeename + "',"
                    + "'" + employeecode + "',"
                    + "'" + ccnumber + "',"
                    + "'66',"
                    + "'" + sex + "',"
                    + "'" + employeecode + "',"
                    + "'1234',"
                    + "'" + dob + "',"
                    + "'" + dol + "',"
                    + "'" + doj + "',"
                    + "'" + DivName + "',"
                    + "'0',"
                    + "'" + desig + "',"
                    + "'" + Workl + "',"
                    + "'0',"
                    + "'0',"
                    + "'0',"
                    + "'" + accesslevel + "',"
                    + "'2012-05-29 16:55:18',"
                    + "0,"
                    + "'" + div_Code + "',"
                    + "'" + SecCode + "',"
                    + "'" + DShift + "',"
                    + "'" + CatCode + "',"
                    + "'" + LCode + "',"
                    + "'" + WOff1 + "',"
                    + "'" + WOff2 + "',"
                    + "'" + SSwipe + "',"
                    + "'" + Addr + "',"
                    + "'" + prebalLeave + "',"
                    + "'" + postid + "',"
                    + "'" + report_to + "')";
            int executeUpdate = stmt.executeUpdate(query);
            myHTTPConn.println("Inserted");

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
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
            String EmpCode = myHTTPConn.readLine();
            //String query = "Select * FROM employeemaster where EmpCode='" + EmpCode + "'";
            String query = "select "
                    + "EmpName,"
                    + "EmpCode,"
                    + "CCNo,"
                    + "Sex,"
                    + "DOB,"
                    + "Validity,"
                    + "Joining,"
                    + "Division,"
                    + "DesigID,"
                    + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                    + "wlocation_code,"
                    + "(select WLocation from worklocation where worklocation.wlocation_code=employeemaster.wlocation_code) as workplace,"
                    + "AccessLevel,"
                    + "DivCode,"
                    + "SecCode,"
                    + "ShiftCode,"
                    + "CatCode,"
                    + "LeaveCode,"
                    + "WeekOff1,"
                    + "WeekOff2,"
                    + "SingSwAllowed,"
                    + "PreLeaveBal,"
                    + "reportingto,"
                    + "PostCode,"
                    + "(select PostStatus from postmaster where postmaster.PostCode=employeemaster.PostCode) as post,"
                    + "Address "
                    + "from "
                    + "employeemaster "
                    + "where "
                    + "EmpCode='" + EmpCode + "'";
            Packetizer p;
            try (ResultSet rs = stmt.executeQuery(query)) {
                p = new Packetizer();
                rs.next();
                for (int i = 1; i <= 26; i++) {
                    if ((rs.getString(i) != null) & (!"".equals(rs.getString(i)))) {
                        p.addString(rs.getString(i));
                    } else {
                        p.addString("");
                    }
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

    private void UpdateEmployee(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String Packet = myHTTPConn.readLine();

            Depacketizer dp = new Depacketizer(Packet);

            String ccnumber = dp.getString();
            String employeecode = dp.getString();
            String employeename = dp.getString();
            String desig = dp.getString();
            int sex = dp.getInt();
            String dob = dp.getString();
            String doj = dp.getString();
            String dol = dp.getString();
            String DivName = dp.getString();
            String Workl = dp.getString();
            String Div_Code = dp.getString();
            String SecCode = dp.getString();
            String DShift = dp.getString();
            String CatCode = dp.getString();
            String LCode = dp.getString();
            int WOff1 = dp.getInt();
            int WOff2 = dp.getInt();
            int SSwipe = dp.getInt();
            String Addr = dp.getString();
            String report_to = dp.getString();
            int prebalLeave = dp.getInt();
            int readeraccess = dp.getInt();
            int postcode = dp.getInt();

            query = "UPDATE `employeemaster` "
                    + "SET "
                    + "`EmpName`='" + employeename + "',"
                    + " `CCNo`='" + ccnumber + "', "
                    + "`Sex`='" + sex + "'"
                    + ",`DOB`='" + dob + "', "
                    + "`Validity`='" + dol + "', "
                    + "`Joining`='" + doj + "', "
                    + "`Division`='" + DivName + "',"
                    + "`DesigID`='" + desig + "',"
                    + "`wlocation_code`='" + Workl + "', "
                    + "`AccessLevel`='" + readeraccess + "', "
                    + "`DivCode`='" + Div_Code + "',"
                    + "`SecCode`='" + SecCode + "',"
                    + "`ShiftCode`='" + DShift + "',"
                    + "`CatCode`='" + CatCode + "', "
                    + "`LeaveCode`='" + LCode + "',"
                    + "`WeekOff1`='" + WOff1 + "',"
                    + "`WeekOff2`='" + WOff2 + "',"
                    + "`SingSwAllowed`='" + SSwipe + "',"
                    + "`PreLeaveBal`='" + prebalLeave + "',"
                    + "`reportingto`='" + report_to + "',"
                    + "`PostCode`='" + postcode + "',"
                    + "`Address`='" + Addr + "' "
                    + "WHERE "
                    + "`EmpCode` ='" + employeecode + "'";
            stmt.executeUpdate(query);
            myHTTPConn.println("Updated");

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void SecName(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SecCode = myHTTPConn.readLine();
            query = "SELECT SecHeadName FROM sectionmaster where SecCode ='" + SecCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                myHTTPConn.println(rs.getString(1));
            } else {
                myHTTPConn.println("");
            }

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void CatCode(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String CatCode = myHTTPConn.readLine();
            query = "SELECT CatName FROM category where CatCode = '" + CatCode + "'";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs.next()) {
                p.addString(rs.getString(1));
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
            String DivCode = myHTTPConn.readLine();
            query = "SELECT DivName,DivHeadName FROM division where DivCode='" + DivCode + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            while (rs1.next()) {
                p.addString(rs1.getString(1));
                p.addString(rs1.getString(2));
            }
            query = "Select SecCode,SecName FROM sectionmaster where DivCode ='" + DivCode + "'";
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {
                p.addString(rs2.getString(1) + " " + rs2.getString(2));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void InitValues(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            query = "SELECT CatCode FROM category ";
            ResultSet rs1 = stmt.executeQuery(query);
            while (rs1.next()) {
                p.addString(rs1.getString(1));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();

            query = "SELECT DivCode FROM division";
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {
                p.addString(rs2.getString(1));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();
            query = "SELECT wlocation_code,WLocation FROM worklocation ";
            ResultSet rs3 = stmt.executeQuery(query);
            while (rs3.next()) {
                p.addString(rs3.getString(1) + " " + rs3.getString(2));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();
            query = "Select ShiftCode From shiftmaster";
            ResultSet rs4 = stmt.executeQuery(query);
            while (rs4.next()) {
                p.addString(rs4.getString(1));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();
            query = "SELECT LeaveCode FROM leaveconfig";
            ResultSet rs5 = stmt.executeQuery(query);
            while (rs5.next()) {
                p.addString(rs5.getString(1));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();
            query = "SELECT DesigID,DesigName FROM designationmaster";
            ResultSet rs6 = stmt.executeQuery(query);
            while (rs6.next()) {
                p.addString(rs6.getString(1) + " " + rs6.getString(2));
            }
            myHTTPConn.println(p.getPacket());
            p = new Packetizer();
            query = "SELECT PostCode,PostStatus FROM postmaster";
            ResultSet rs7 = stmt.executeQuery(query);
            while (rs7.next()) {
                p.addString(rs7.getString(1) + " " + rs7.getString(2));
            }
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteEmployee(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String id = myHTTPConn.readLine();
            query = "DELETE FROM `employeemaster` WHERE `EmpCode`='" + id + "'";
            int executeUpdate = stmt.executeUpdate(query);
            myHTTPConn.println("Deleted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private String getDivision(MyHTTPConnection myHTTPConn, LoginSession loginSession, String DivCode) {
        DBConnection conn = null;
        Statement stmt = null;
        String divname = "";
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return "";
            }
            String query;
            query = "SELECT DivName FROM division where DivCode='" + DivCode + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            if (rs1.next()) {
                divname = rs1.getString(1);
            } else {
                divname = "";
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
        return divname;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
