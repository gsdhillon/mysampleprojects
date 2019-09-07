package javacodes.reports;

import java.sql.ResultSet;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javacodes.connection.MyHTTPConnection;
import javacodes.login.LoginSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 */
public class UserMasterServlet extends HttpServlet {

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MyHTTPConnection myHTTPConn = null;
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
            LoginSession loginSession = LoginSession.getLoginSession(session, reqTypes);
            /*
             * Read in the message from the servlet
             */
            String msg = myHTTPConn.readLine();
            /*
             * Write the message back to the applet
             */
            switch (msg) {
                case "getSelectedEmpList":
                    getSelectedEmpList(myHTTPConn);
                    break;
                case "getUserList":
                    getUserwiseEmployeeList(myHTTPConn, loginSession);
//                    getEmployeeList(myHTTPConn, loginSession);
                    break;
                case "getAllUserList":
                    getAllEmployeeList(myHTTPConn);
                    break;
                case "Division":
                case "Section":
                    getComboList(myHTTPConn, loginSession, msg);
                    break;
                default:
                    myHTTPConn.println("ERROR:unknown request");
                    break;
            }
        } catch (Exception e) {
            try {
                if (myHTTPConn != null) {
                    myHTTPConn.println("ERROR:" + e.getMessage());
                }
                e.printStackTrace();
            } catch (Exception ex) {
            }
        } finally {
            AppContext.close(myHTTPConn);
        }
    }

    /**
     *
     * @param myHTTPConn
     */
    private void getSelectedEmpList(MyHTTPConnection myHTTPConn) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            String packet = myHTTPConn.readLine();
            String div_code, sec_code;
            String query;

            Depacketizer dp = new Depacketizer(packet);
            div_code = dp.getString();
            sec_code = dp.getString();
            String division = "", section = "";

            if (!"".equals(div_code)) {
                division = " DivCode='" + div_code + "'";
            }
            if (!"".equals(sec_code)) {
                section = " AND SecCode='" + sec_code + "'";

                stmt = conn.createStatement();
                query =
                        "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                        + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, "
                        + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                        + "from "
                        + "employeemaster where " + division + section;
            } else {
                stmt = conn.createStatement();
                query =
                        "select "
                        + "EmpCode, "
                        + "EmpName, "
                        + "(select DesigName from designationmaster where designationmaster.DesigID=employeemaster.DesigID) as designation,"
                        + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, "
                        + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                        + "from "
                        + "employeemaster where " + division;
            }
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

    public boolean checkHead(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        boolean checkHead = false;
        try {
            conn = AppContext.getDBConnection(); //get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            String userid = loginSession.getUserID();
            stmt = conn.createStatement();
            String query = "select EmpStatus from employeemaster where EmpCode='" + userid + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String status = rs.getString(1);
                if ("3".equals(status)) {
                    checkHead = true;
                }
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
        return checkHead;
    }

    public String getDivAndSection(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        String divSec = "";
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            stmt = conn.createStatement();
            Packetizer p = new Packetizer();
            Packetizer div = new Packetizer();
            Packetizer sec = new Packetizer();
            String query = "select DivCode from division where DivHeadCode='" + loginSession.getUserID() + "'";
            ResultSet rs1 = stmt.executeQuery(query);
            div.setCounter();
            while (rs1.next()) {
                div.addString(rs1.getString(1));
                div.incrCounter();
            }
            rs1.close();

            query = "select SecCode from sectionmaster where SecHeadCode='" + loginSession.getUserID() + "'";
            ResultSet rs2 = stmt.executeQuery(query);
            sec.setCounter();
            while (rs2.next()) {
                sec.addString(rs2.getString(1));
                sec.incrCounter();
            }
            rs2.close();
            p.addString(div.getPacket());
            p.addString(sec.getPacket());
            divSec = p.getPacket();
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
        return divSec;
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

    private void getComboList(MyHTTPConnection myHTTPConn, LoginSession loginSession, String ReqType) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();
            stmt = conn.createStatement();
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
                return;
            }
            //get connection
            String query = "";
            switch (ReqType) {
                case "Division":
//                    String divcode = myHTTPConn.readLine();
                    query = "SELECT DivCode,"
                            + "DivName "
                            + "FROM "
                            + "division ";

                    break;
                case "Section":
                    String divcode = myHTTPConn.readLine();
                    query = "select sectionmaster.SecCode,"
                            + "sectionmaster.SecName "
                            + "from "
                            + "sectionmaster,"
                            + "division "
                            + "where "
                            + "division.DivCode=" + "'" + divcode + "'" + " "
                            + "and "
                            + "sectionmaster.DivCode=division.DivCode";
                    break;

            }
            Packetizer p;
            ResultSet rs = stmt.executeQuery(query);
            p = new Packetizer();
            p.setCounter();
            while (rs.next()) {
                p.addString(rs.getString(1));
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

    /**
     * function used in muster servlet for
     *
     * @param myHTTPConn
     * @param loginSession
     */
    public void getEmployeeList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        DBConnection conn = null;
        Statement stmt = null;
        try {
            conn = AppContext.getDBConnection();//get connection
            if (conn == null) {
                myHTTPConn.println("ERROR:Could not get database connection");
            }
            if (checkHead(myHTTPConn, loginSession)) {
                String packet = getDivAndSection(myHTTPConn, loginSession);
                String div_packet, sec_packet;

                Depacketizer dp = new Depacketizer(packet);
                div_packet = dp.getString();
                sec_packet = dp.getString();
                String division = "", section = "";

                Depacketizer div = new Depacketizer(div_packet);
                int deptsize = div.getInt();

                if (deptsize > 0) {
                    for (int i = 0; i < deptsize; i++) {
                        if (i == 0) {
                            division = " DivCode='" + div.getString() + "'";
                        } else {
                            division = division + " OR DivCode='" + div.getString() + "'";
                        }
                    }
                }
                Depacketizer sec = new Depacketizer(sec_packet);
                int secsize = sec.getInt();

                if (secsize > 0) {
                    for (int i = 0; i < secsize; i++) {
                        if (i == 0) {
                            section = " OR SecCode='" + sec.getString() + "'";
                        } else {
                            section = section + " OR SecCode='" + sec.getString() + "'";
                        }
                    }
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
                        + "employeemaster where " + division + section;
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    public void getOfficeEmployeeList(MyHTTPConnection myHTTPConn, String username, int usertype) {
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
                        + "(select DivName from division where division.DivCode=employeemaster.DivCode) as Division, " + "Division, "
                        + "(select SecName from sectionmaster where sectionmaster.SecCode=employeemaster.SecCode) as Section "
                        + "from "
                        + "employeemaster where " + division + section;
                Packetizer p;
                try (ResultSet rs1 = stmt.executeQuery(query)) {
                    p = new Packetizer();
                    p.setCounter();
                    while (rs1.next()) {
                        p.addString(rs1.getString("EmpCode"));
                        p.addString(rs1.getString("EmpName"));
                        p.addString(rs1.getString("designation"));
                        p.addString(rs1.getString("Division"));
                        p.addString(rs1.getString("Section"));
                        p.incrCounter();
                    }
                }
                myHTTPConn.println(p.getPacket());
            }
        } catch (Exception e) {
            e.printStackTrace();
            myHTTPConn.println("ERROR: " + e.getMessage());
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    private void getUserwiseEmployeeList(MyHTTPConnection myHTTPConn, LoginSession loginSession) {
        int usertype = loginSession.getLoginType();
        String username = loginSession.getUserID();
        if (usertype == 0) {
            getEmployeeList(myHTTPConn, loginSession);
        } else if (usertype == 1) {
            getAssignedEmpList(myHTTPConn, username, usertype);
        }
    }

    public void getAssignedEmpList(MyHTTPConnection myHTTPConn, String username, int usertype) {
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
