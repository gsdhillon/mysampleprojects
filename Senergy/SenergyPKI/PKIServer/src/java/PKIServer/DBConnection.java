package PKIServer;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 * DBConnection.java
 * Created on Sep 15, 2008 at 11:49:45 AM
 * @author Gurmeet Singh
 */
public class DBConnection {
    //public static String cs = "jdbc:oracle:thin:@31.1.1.51:1521:secdb";
    //public static String adminName = "root";
    //public static String adminPass = "fw0r";
    
    public static String cs = "jdbc:mysql:///rpg";
    public static String adminName = "root";
    public static String adminPass = "fw0r";
    
    
    /**
     */
    public static Connection getConnectiion() throws Exception{
        // Load Oracle driver
        //DriverManager.registerDriver(new OracleDriver());
        // Connect to the database
        //return DriverManager.getConnection(cs,adminName,adminPass);
        DriverManager.registerDriver(com.mysql.jdbc.Driver.class.newInstance());
        return DriverManager.getConnection(cs ,adminName, adminPass);
    }
}
