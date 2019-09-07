package javacodes.connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javacodes.login.LoginSession;
import javax.servlet.http.HttpSession;

/**
 * AppContext.java
 */
public class AppContext {
    public static String swipeCollectorStat = "Not Started";
    public static boolean serverStarted = false;
    public static Date server1StartStopTime = new Date();
    public static ArrayList<HttpSession> sessionList = new ArrayList<>();

    public static void addSession(HttpSession session) {
        sessionList.add(session);
    }

    public static void removeSession(HttpSession session) {
        sessionList.remove(session);
    }

    public static boolean isReaderLoacked(String userID, String readerID) {
        for (Object obj : sessionList) {
            HttpSession httpSession = (HttpSession) obj;
            if (httpSession == null) {//|| httpSession.getCreationTime())
                continue;
            }
            LoginSession ls = (LoginSession) httpSession.getAttribute(LoginSession.LOGIN_SESSION_ATTRIBUTE);
            if (ls == null || !ls.isValid()) {
                continue;
            } else {
                if (!ls.getUserID().equals(userID) && ls.getReaderIDLocked().equals(readerID)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getDBConnection without connection pooling
     */
    public static DBConnection getDBConnection() {
        try {
            DBConnection conn = new DBConnection();
            conn.connect();
            return conn;
        } catch (Exception e) {
            log(null, e);
            return null;
        }
    }

    /**
     *
     * @param stmt
     * @param conn
     */
    public static void close(Statement stmt, DBConnection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            log(null, e);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            log(null, e);
        }
    }
   
    /**
     *
     * @param myHTTPConn
     */
    public static void close(MyHTTPConnection myHTTPConn) {
        try {
            if (myHTTPConn != null) {
                myHTTPConn.close();
            }
        } catch (Exception e) {
            log(null, e);
        }
    }

    /**
     *
     * @param myHTTPConn
     */
    public static int countRows(ResultSet rs) throws Exception {
        rs.last();
        int noOfRecords = rs.getRow();
        rs.beforeFirst();
        return noOfRecords;
    }
    /**
     * 
     * @param loginSession
     * @param e 
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void log(LoginSession loginSession, Exception e) {
        if (loginSession != null) {
            System.out.println("LoginSession - " + loginSession.getDisplayName());
        } else {
            System.out.println("LoginSession NULL");
        }
        e.printStackTrace();
    }

    public static void println(String string) {
        System.out.println(string);
    }
}