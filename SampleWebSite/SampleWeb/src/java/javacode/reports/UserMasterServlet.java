package javacode.reports;

import java.sql.ResultSet;
import java.sql.Statement;
import javacode.DBConnection;
import javacode.LoginSession;
import javacode.MCC;
import javacode.ObjectServlet;
import lib.utils.Packetizer;
/**
 */
public class UserMasterServlet extends ObjectServlet {
    @Override
    protected int[] getRequiredLoginTypes() {
        return new int[] {
            LoginSession.TYPE_USER,
            LoginSession.TYPE_OFFICE,
            LoginSession.TYPE_ADMIN
        };
    }
    @Override
    protected void processRequest(String reqMsg, MCC mcc, LoginSession loginSession, DBConnection conn) throws Exception{
        switch (reqMsg) {
        case "getUserList":
            getUserList(mcc, conn);
            break;
        default:
            mcc.println("ERROR:UnknownRequest");
            break;
        }
    }
    /**
     *
     * @param mcc
     */
    private void getUserList(MCC mcc, DBConnection conn) throws Exception{
        Statement stmt = conn.createStatement();
        String query =
        "select "
                + "user_id, "
                + "name, "
                + "desig, "
                + "division, "
                + "DATE_FORMAT(date_of_birth, '%d/%m/%Y') dob " +
        "from " +
            "users";
        Packetizer p;
        try (ResultSet rs = stmt.executeQuery(query)) {
            p = new Packetizer();
            p.setCounter();
            while(rs.next()){
                p.addString(rs.getString("user_id"));
                p.addString(rs.getString("name"));
                p.addString(rs.getString("desig"));
                p.addString(rs.getString("division"));
                p.addString(rs.getString("dob"));
                p.incrCounter();
            }
        }
        mcc.println(p.getPacket());
    }
}