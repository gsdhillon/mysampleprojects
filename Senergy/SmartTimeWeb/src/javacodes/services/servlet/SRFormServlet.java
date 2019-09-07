/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacodes.services.servlet;

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
import lib.utils.Depacketizer;
import lib.utils.Helper.Tuple;
import lib.utils.Packetizer;

/**
 *
 * @author GAURAV
 */
public class SRFormServlet extends HttpServlet {

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
                LoginSession.TYPE_USER,
                LoginSession.TYPE_OFFICE,
                LoginSession.TYPE_ADMIN
            };
            loginSession = LoginSession.getLoginSession(session, reqTypes);

            String ReqType = myHTTPConn.readLine();

            if (ReqType.equals("DeleteRoster")) {
                DeleteRoster(myHTTPConn, loginSession);
            } else if (ReqType.equals("CheckRoster")) {
                CheckRoster(myHTTPConn, loginSession);
            } else if (ReqType.equals("ShiftPatternList")) {
                getSPatternList(myHTTPConn, loginSession);
            } else if (ReqType.equals("UpdateRoster")) {
                UpdateRoster(myHTTPConn, loginSession);
            } else if (ReqType.equals("ShiftList")) {
                ShiftList(myHTTPConn, loginSession);
            } else if (ReqType.equals("FillFormAdd")) {
                FillFormAdd(myHTTPConn, loginSession);
            } else if (ReqType.equals("FillFormUpdate")) {
                FillFormUpdate(myHTTPConn, loginSession);
            } else if (ReqType.equals("SPatternDetails")) {
                PatternDetails(myHTTPConn, loginSession);
            } else if (ReqType.equals("AddRoster")) {
                AddRoster(myHTTPConn, loginSession);
            } else if (ReqType.equals("getEmployeeList")) {
                getEmployeeList(myHTTPConn, loginSession);
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

    private void UpdateRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String UserID = dp.getString();
            String Month = dp.getString();
            String Year = dp.getString();
            String query = "UPDATE shiftroster SET ";
            for (int i = 1; i <= 31; i++) {
                String columnName = "0" + i;
                query += "d" + columnName.substring(columnName.length() - 2) + "='" + dp.getString() + "', ";
            }
            query = query.substring(0, query.length() - 2);
            query += " WHERE EmpCode='" + UserID + "' AND Month='" + Month + "' AND Year='" + Year + "'";

            int update = stmt.executeUpdate(query);
            if (update != 0) {
                myHTTPConn.println("Update");
            } else {
                myHTTPConn.println("NotUpdates");
            }
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void FillFormUpdate(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userID = myHTTPConn.readLine();
            String query = "Select * From shiftroster WHERE EmpCode='" + userID + "' ORDER BY Year,Month";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                for (int i = 1; i < 37; i++) {
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

    private void AddRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String SPCode = dp.getString();
            String Date = dp.getString();

            String query = "SELECT * FROM shiftpattern WHERE PatternCode='" + SPCode + "' ORDER BY position";
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            int rowCount = rs.getRow();

            rs.first();
            Tuple[] tuple = new Tuple[rowCount];
            int position, days, Pdays = 0;
            String shift;
            for (int i = 0; i < rowCount; i++) {

                position = rs.getInt(2);
                shift = rs.getString(3);
                days = rs.getInt(4);
                tuple[i] = new Tuple(position, shift, days);
                Pdays += days;
                rs.next();
            }
            String[] patternList = new String[Pdays];
            int i = 0, j = 0, k = 0;
            while (i < Pdays) {
                if (j < rowCount) {
                    if (k < tuple[j].days) {
                        patternList[i] = tuple[j].shift;
                        i++;
                        k++;
                    } else {
                        j++;
                        k = 0;
                    }
                }
            }

            Calendar a = Calendar.getInstance();

            a.set(Integer.parseInt(Date.substring(0, 4)), Integer.parseInt(Date.substring(5, 7)) - 1, Integer.parseInt(Date.substring(8, 10)));
            int finaldate = a.get(Calendar.DATE);
            int l, m;
            String EmpCode;
            while (!dp.isEmpty()) {
                j = finaldate;
                k = 0;
                l = 1;
                EmpCode = dp.getString();
                String insertQuery = "INSERT INTO `shiftroster` (`EmpCode`, `Month`, `Year`, `ShiftStartAt`, `PatternCode`, `d01`, `d02`, `d03`, `d04`, `d05`,"
                        + " `d06`, `d07`, `d08`, `d09`, `d10`, `d11`, `d12`, `d13`, `d14`, `d15`, `d16`, `d17`, `d18`, `d19`, `d20`, `d21`, `d22`,"
                        + " `d23`, `d24`, `d25`, `d26`, `d27`, `d28`, `d29`, `d30`, `d31`) Values('";
                for (i = 0; i < 12; i++) {

                    query = insertQuery;
                    query += EmpCode + "','";
                    int month = a.get(Calendar.MONTH) + 1;
                    int year = a.get(Calendar.YEAR);
                    query += month + "','";
                    query += year + "','";
                    query += Date + "','";
                    query += SPCode + "','";
                    m = k;
                    while (l < j) {
                        query += "','";
                        l++;
                    }
                    while (j <= a.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        if (k < patternList.length) {
                            query += patternList[k] + "','";
                            k++;
                            j++;
                        } else {
                            k = 0;
                            query += patternList[k] + "','";
                            k++;
                            j++;

                        }
                    }
                    while (j <= 31) {
                        query += "','";
                        j++;
                    }
                    query = query.substring(0, query.length() - 2);
                    query += ")";

                    a.add(Calendar.MONTH, 1);
                    try {
                        int execute = stmt.executeUpdate(query);
                    } catch (Exception ex) {
                        query = "UPDATE shiftroster SET PatternCode='" + SPCode + "', ";
                        k = m;
                        while (l < a.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            String ColumnName = "0" + l;
                            if (k < patternList.length) {
                                query += "d" + ColumnName.substring(ColumnName.length() - 2) + " ='" + patternList[k] + "', ";
                                l++;
                                k++;
                            } else {
                                k = 0;
                                query += "d" + ColumnName.substring(ColumnName.length() - 2) + " ='" + patternList[k] + "', ";
                                k++;
                                l++;

                            }
                        }
                        query = query.substring(0, query.length() - 2);
                        query += " WHERE EmpCode='" + EmpCode + "' AND Month='" + month + "' AND Year='" + year + "'";

                        stmt.executeUpdate(query);

                    }
                    l = 1;
                    j = 1;
                }

                if (finaldate != 1) {
                    query = insertQuery;
                    query += EmpCode + "','";
                    query += (a.get(Calendar.MONTH) + 1) + "','";
                    query += a.get(Calendar.YEAR) + "','";
                    query += Date + "','";
                    query += SPCode + "','";

                    while (j < finaldate) {
                        if (k < patternList.length) {
                            query += patternList[k] + "','";
                            k++;
                            j++;
                        } else {
                            k = 0;
                            query += patternList[k] + "','";
                            k++;
                            j++;

                        }
                    }
                    while (j <= 31) {
                        query += "','";
                        j++;
                    }
                    query = query.substring(0, query.length() - 2);
                    query += ")";
                    stmt.executeUpdate(query);
                }
            }
            myHTTPConn.println("Inserted");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void PatternDetails(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query = "SELECT * FROM shiftpattern WHERE PatternCode='" + SPCode + "' ORDER BY position";


            Packetizer p = new Packetizer();
            p.setCounter();
            ResultSet rs2 = stmt.executeQuery(query);
            while (rs2.next()) {

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

    private void FillFormAdd(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
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
            String query, userID;
            Packetizer p = new Packetizer();
            p.setCounter();
            while (!dp.isEmpty()) {
                userID = dp.getString();
                query = "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation, "
                        + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, "
                        + "DATE_FORMAT(DOB, '%d/%m/%Y') dob "
                        + "from "
                        + "employeemaster Where EmpCode='" + userID + "'";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    p.addString(rs.getString("EmpCode"));
                    p.addString(rs.getString("EmpName"));
                    p.addString(rs.getString("designation"));
                    p.addString(rs.getString("Division"));
                    p.addString(rs.getString("dob"));
                    p.incrCounter();
                }
                rs.close();
            }
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
            myHTTPConn.println(p.getPacket());
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void CheckRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String userID = myHTTPConn.readLine();
            String query = "SELECT DISTINCT EmpCode FROM shiftroster";
            ResultSet rs = stmt.executeQuery(query);
            boolean a = false;
            while (rs.next()) {
                if (rs.getString(1).equals(userID)) {
                    myHTTPConn.println("Found");
                    return;
                }
            }
            rs.close();
            myHTTPConn.println("NotPresent");
        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void DeleteRoster(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            String Employee = myHTTPConn.readLine();
            String query;

            query = "DELETE FROM `shiftroster` WHERE `EmpCode`='" + Employee + "'";
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
            rs1.close();
            myHTTPConn.println(q.getPacket());

        } catch (Exception e) {
            AppContext.log(loginSession, e);
            myHTTPConn.println("ERROR:" + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getEmployeeList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        int usertype = loginSession.getLoginType();
        String username = loginSession.getUserID();
        if (usertype == 1) {
            getSelectedEmployeeList(myHTTPConn, username, usertype);
        } else if (usertype == 2) {
            getAllEmployeeList(myHTTPConn);
        }
    }

    private void getAllEmployeeList(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection(); //get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String query =
                    "select "
                    + "EmpCode, "
                    + "EmpName, "
                    + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                    + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, "
                    + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                    + "from "
                    + "employeemaster";
            ResultSet rs = stmt.executeQuery(query);
            Packetizer p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString("EmpCode"));
                p.addString(rs.getString("EmpName"));
                p.addString(rs.getString("designation"));
                p.addString(rs.getString("Division"));
                p.addString(rs.getString("Section"));
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

    public void getSelectedEmployeeList(MyHTTPConnection myHTTPConn, String username, int usertype) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection(); //get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            String access_string = "";
            String division = "", section = "";

            String query = "Select "
                    + "AccessString "
                    + "from "
                    + "useraccess "
                    + "where "
                    + "UserName='" + username + "' "
                    + "and "
                    + "usertype=" + usertype;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                access_string = rs.getString(1);
                String acc_arr[] = access_string.split(" ");
                Packetizer div_packet = new Packetizer();
                div_packet.setCounter();
                Packetizer sec_packet = new Packetizer();
                sec_packet.setCounter();
                for (int i = 0; i < acc_arr.length; i++) {
                    char ch = acc_arr[i].charAt(0);
                    if (ch == '#') {
                        div_packet.addString(acc_arr[i].substring(1));
                        div_packet.incrCounter();
                    } else if (ch == '@') {
                        sec_packet.addString(acc_arr[i].substring(1));
                        sec_packet.incrCounter();
                    }
                }
                Depacketizer div = new Depacketizer(div_packet.getPacket());
                Depacketizer sec = new Depacketizer(sec_packet.getPacket());

                int deptsize = div.getInt();
                if (deptsize > 0) {
                    for (int i = 0; i < deptsize; i++) {
                        if (i == 0) {
                            division = " DivCode='" + div.getString() + "'";
                        } else {
                            division = division + " OR DivCode='" + div.getString() + "'";
                        }
                    }
                } else {
                    division = "";
                }
                int secsize = sec.getInt();
                if (secsize > 0) {
                    for (int i = 0; i < secsize; i++) {
                        if (i == 0) {
                            if (!"".equals(division)) {
                                section = " OR SecCode='" + sec.getString() + "'";
                            } else {
                                section = " SecCode='" + sec.getString() + "'";
                            }
                        } else {
                            section = section + " OR SecCode='" + sec.getString() + "'";
                        }
                    }
                } else {
                    section = "";
                }
            }
            if ((!"".equals(division)) || (!"".equals(section))) {
                query =
                        "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                        + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, "
                        + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                        + "from "
                        + "employeemaster where " + division + section;
                ResultSet rs1 = stmt.executeQuery(query);
                Packetizer p = new Packetizer();
                p.setCounter();
                while (rs1.next()) {
                    p.addString(rs1.getString("EmpCode"));
                    p.addString(rs1.getString("EmpName"));
                    p.addString(rs1.getString("designation"));
                    p.addString(rs1.getString("Division"));
                    p.addString(rs1.getString("Section"));
                    p.incrCounter();
                }
                rs1.close();
                myHTTPConn.println(p.getPacket());
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }
}
