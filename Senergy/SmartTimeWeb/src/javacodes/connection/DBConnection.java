package javacodes.connection;

import java.sql.*;

/**
 * DBConnection.java
 */
public class DBConnection {

    private Connection conn = null;

    /**
     */
    public DBConnection() {
    }

    public CallableStatement prepareCall(String nameOfProcedure) throws Exception {
        return conn.prepareCall(nameOfProcedure);
    }

    /**
     */
    public void connect() throws Exception {
        DriverManager.registerDriver(com.mysql.jdbc.Driver.class.newInstance());
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smarthawkattendance",
                "root",
                "sipl");
    }

    /**
     */
    public Statement createStatement() throws SQLException {
        //return conn.createStatement();
        return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    }

    /**
     */
    public PreparedStatement prepareStatement(String query) throws Exception {
        return conn.prepareStatement(query);
    }

    /**
     */
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     */
    public void rollback() throws Exception {
        this.conn.rollback();
    }

    /**
     */
    public void commit() throws Exception {
        this.conn.commit();
    }

    /**
     */
    public void setAutoCommit(boolean flag) throws Exception {
        this.conn.setAutoCommit(flag);
    }
}
