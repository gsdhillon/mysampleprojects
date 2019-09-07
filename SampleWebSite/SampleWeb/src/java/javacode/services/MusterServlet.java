package javacode.services;

import java.sql.ResultSet;
import java.sql.Statement;
import javacode.*;
import lib.utils.Packetizer;
/**
 */
@SuppressWarnings({"CallToThreadDumpStack"})
public class MusterServlet extends ObjectServlet{
    @Override
    protected int[] getRequiredLoginTypes() {
        return new int[] {
            LoginSession.TYPE_USER
        };
    }
    @Override
    protected void processRequest(String reqMsg, MCC mcc, LoginSession loginSession, DBConnection conn) throws Exception{
        switch (reqMsg) {
        case "getMuster":
            getUserMuster(loginSession, mcc, conn);
            break;
        case "getUserMusterForAll":
            getUserMusterForAll(loginSession, mcc, conn);
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
    private void getUserMusterForAll(LoginSession loginSession, MCC mcc, DBConnection conn) throws Exception{
        String mm = mcc.readLine();
        String yy = mcc.readLine();
        String division = loginSession.getDivision();
        Statement stmt = conn.createStatement();
        String[][] users;
        int count;
        MySQLQuery msq = new MySQLQuery("DIV_EMP_LIST");
        msq.setString("DIVISION", division);
        try (ResultSet rs = stmt.executeQuery(msq.getQuery())) {
            users = new String[100][3];
            count = 0;
            while (rs.next()) {
                users[count][0] = rs.getString("user_id");
                users[count][1] = rs.getString("name");
                users[count][2] = rs.getString("desig");
                count++;
            }
        }
        mcc.println(String.valueOf(count));
        mcc.println(division);
        //send all emps musters
        msq = new MySQLQuery("EMP_MONTHLY_MUSTER");
        for (int i = 0; i < count; i++) {
            msq.reset();
            msq.setNumber("USERID", users[i][0]);
            msq.setString("MMYY", mm + yy);
            Packetizer p;
            try (ResultSet rs1 = stmt.executeQuery(msq.getQuery())){
                p = new Packetizer();
                p.setCounter();
                p.addString(users[i][0]);
                p.addString(users[i][1]);
                p.addString(users[i][2]);
                while (rs1.next()) {
                    p.addString(rs1.getString(1));
                    p.addString(rs1.getString(2));
                    p.addString(rs1.getString(3));
                    p.addString(rs1.getString(4));
                    p.addString(rs1.getString(5));
                    p.addString(rs1.getString(6));
                    p.incrCounter();
                }
            }
            mcc.println(p.getPacket());
        }
    }
    /**
     *
     * @param mcc
     */
    private void getUserMuster(LoginSession loginSession, MCC mcc, DBConnection conn) throws Exception {
        String mm = mcc.readLine();
        String yy = mcc.readLine();
        String userID = loginSession.getUserID();
        Statement stmt = conn.createStatement();
        Packetizer p;
        MySQLQuery msq = new MySQLQuery("EMP_MONTHLY_MUSTER");
        msq.setNumber("USERID", userID);
        msq.setString("MMYY", mm + yy);
        try (ResultSet rs = stmt.executeQuery(msq.getQuery())) {
            p = new Packetizer();
            p.setCounter();
            p.addString(loginSession.getName());
            while (rs.next()) {
                p.addString(rs.getString(1));
                p.addString(rs.getString(2));
                p.addString(rs.getString(3));
                p.addString(rs.getString(4));
                p.addString(rs.getString(5));
                p.addString(rs.getString(6));
                p.incrCounter();
            }
        }
        mcc.println(p.getPacket());
    }
}