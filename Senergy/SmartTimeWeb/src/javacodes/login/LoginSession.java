package javacodes.login;

import java.sql.ResultSet;
import java.sql.Statement;
import javacodes.connection.AppContext;
import javacodes.connection.DBConnection;
import javax.servlet.http.HttpSession;

/**
 */
public class LoginSession {

    public static final String LOGIN_SESSION_ATTRIBUTE = "login_session";
    public static final int TYPE_USER = 0;
    public static final int TYPE_OFFICE = 1;
    public static final int TYPE_ADMIN = 2;
    private static final String[] loginTypes = {
        "User",//0
        "Office",//1
        "Administrator"//2
    };
    private int loginType = -1;
    private boolean valid;
    public String lastMsg;
    private String userID;
    private String name;
    private String workplace;
    private String division;
    private String EmpStatus;
    private String readerIDLocked = null;
    private boolean isTeamLeader;

    public String getReaderIDLocked() {
        return readerIDLocked;
    }

    public void setReaderIDLocked(String readerIDLocked) {
        this.readerIDLocked = readerIDLocked;
    }

    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    /**
     *
     * @param loginType
     * @param loginID
     * @param pass
     */
    public LoginSession(HttpSession session, String loginID, String pass, int loginType) {
        try {
            this.loginType = loginType;
            if (authenticateUser(loginID, pass, loginType)) {
                session.setAttribute(LOGIN_SESSION_ATTRIBUTE, this);
                this.valid = true;
                this.lastMsg = "Authentiction successful";
                AppContext.addSession(session);
            } else {
                this.valid = false;
            }
        } catch (Exception e) {
            this.valid = false;
            this.lastMsg = "Authentiction Failed;" + e.getMessage();
        }
    }

    /**
     *
     * @param session
     * @return ''
     */
    public static LoginSession getLoginSession(HttpSession session, int[] reqTypes) throws Exception {
        try {
            if (reqTypes == null || reqTypes.length == 0) {
                throw new Exception("ERROR:LoginType Unidentified!");
            }
            if (session == null) {
                throw new Exception("ERROR:Session Expired. Please Re-Login!");
            }
            LoginSession bs = (LoginSession) session.getAttribute(LOGIN_SESSION_ATTRIBUTE);
            if (bs == null) {
                throw new Exception("ERROR:Session Expired. Please Re-Login!");
            }
            if (!bs.isValid()) {
                throw new Exception("ERROR:Session is not VALID.");
            }
            int loginType = bs.getLoginType();
            for (int i = 0; i < reqTypes.length; i++) {
                if (loginType == reqTypes[i]) {
                    return bs;
                }
            }
            String msg = "ERROR:InvalidLoginType Required [" + loginTypes[reqTypes[0]];
            for (int i = 1; i < reqTypes.length; i++) {
                msg += ", " + loginTypes[reqTypes[i]];
            }
            msg += "]";
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @return ''
     */
    public String getDisplayName() {
        if (loginType == TYPE_OFFICE) {
            return name; //+ "[" + division + "]";
        } else if (loginType == TYPE_ADMIN) {
            return "Administrator";//name + "[Administrator]";
        } else {
            return name;
        }
    }

    /**
     *
     * @param loginID
     * @param pass
     * @param loginType
     * @return
     */
    private boolean authenticateUser(String loginID, String pass, int loginType) {
        DBConnection conn = null;
        Statement stmt = null;
        boolean authenticate = false;
        try {
            conn = AppContext.getDBConnection();
            if (conn == null) {
                lastMsg = "Could not get database connection";
                return false;
            }
            // Query emp table
            if (loginType == 0) {
                stmt = conn.createStatement();
                ResultSet rs = null;
                String query =
                        "select "
                        + "EmpName, "
                        + "(select WLocation from worklocation where worklocation.wlocation_code=employeemaster.wlocation_code) as WorkPlace, "
                        + "Division, "
                        + "Password, "
                        + "EmpStatus "
                        + "from "
                        + "employeemaster "
                        + "where "
                        + "EmpCode = '" + loginID + "'";
                rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    rs.close();
                    lastMsg = "Login name '" + loginID + "' is not present!";
                    return false;
                }
                String usr_pwd = rs.getString("Password");
                if ((usr_pwd != null) & (!"".equals(usr_pwd))) {
                    if (!usr_pwd.equals(pass)) {
                        rs.close();
                        lastMsg = "Login password '" + loginID + "' incorrect!";
                        return false;
                    }
                } else {
                    query = "Update employeemaster SET Password='" + loginID + "' where EmpCode='" + loginID + "'";
                    stmt.executeUpdate(query);
                    return true;
                }
                userID = loginID;
                name = rs.getString("EmpName");
                workplace = rs.getString("WorkPlace");
                division = rs.getString("Division");
                EmpStatus = rs.getString("EmpStatus");
                if ("3".equals(EmpStatus)) {
                    isTeamLeader = true;
                } else {
                    isTeamLeader = false;
                }
                //TODO check login type also
                this.loginType = loginType;
                rs.close();
                authenticate = true;
            } else if (loginType == 1) {
                stmt = conn.createStatement();

                ResultSet rs = null;
                String query = "select "
                        + "UserName,"
                        + "Password "
                        + "from "
                        + "useraccess "
                        + "where "
                        + "UserName='" + loginID + "' "
                        + "and "
                        + "usertype=1";
                rs = stmt.executeQuery(query);

                if (!rs.next()) {
                    rs.close();
                    lastMsg = "Login name '" + loginID + "' is not present!";
                    return false;
                }
                if (!rs.getString("Password").equals(pass)) {
                    rs.close();
                    lastMsg = "Login password '" + loginID + "' incorrect!";
                    return false;
                }
                userID = loginID;
                name = "Office";
                this.loginType = loginType;
                rs.close();
                authenticate = true;

            } else if (loginType == 2) {
                stmt = conn.createStatement();

                ResultSet rs = null;
                String query = "select "
                        + "UserName,"
                        + "Password "
                        + "from "
                        + "useraccess "
                        + "where "
                        + "UserName='" + loginID + "' "
                        + "and "
                        + "usertype=2";
                rs = stmt.executeQuery(query);

                if (!rs.next()) {
                    rs.close();
                    lastMsg = "Login name '" + loginID + "' is not present!";
                    return false;
                }
                if (!rs.getString("Password").equals(pass)) {
                    rs.close();
                    lastMsg = "Login password of " + loginID + " incorrect!";
                    return false;
                }
                userID = loginID;
                name = "Admin";
                this.loginType = loginType;
                rs.close();
                authenticate = true;
            }
            return authenticate;
        } catch (Exception e) {
            e.printStackTrace();
            lastMsg = e.getMessage();
            return false;
        } finally {
            AppContext.close(stmt, conn);
        }
    }

    public String getWorkPlace() {
        return workplace;
    }

    public String getDivision() {
        return division;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getLoginType() {
        return loginType;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    /**
     *
     */
    public boolean isValid() {
        return valid;
    }

    /**
     */
    public void invalidate() {
        valid = false;
    }
}