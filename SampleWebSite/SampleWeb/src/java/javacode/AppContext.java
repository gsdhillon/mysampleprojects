package javacode;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * AppContext.java
 */
public class AppContext {
    /**
     * getDBConnection without connection pooling
     */
    public static DBConnection getDBConnection(){
        try{
            DBConnection conn = new DBConnection();
            conn.connect();
            return conn;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * @param stmt
     * @param conn 
     */
    public static void close(Statement stmt, DBConnection conn) {
        try{
            if(stmt != null) stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(conn != null){
                conn.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param stmt
     * @param conn 
     */
    public static void close(DBConnection conn) {
        try{
            if(conn != null){
                conn.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param mcc 
     */
    public static void close(MCC mcc) {
        try{
            if(mcc != null) mcc.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param mcc 
     */
    public static int countRows(ResultSet rs) throws Exception {
            rs.last();
            int noOfRecords = rs.getRow();
            rs.beforeFirst();
            return noOfRecords;
    }
}