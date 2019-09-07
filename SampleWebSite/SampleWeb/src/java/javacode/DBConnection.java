package javacode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * DBConnection.java
 */
public class DBConnection {
    private Connection conn = null;
    private Statement stmt = null;
    /**
     */
    public DBConnection(){
    }
    /**
     */
    public void connect() throws Exception{
        DriverManager.registerDriver(com.mysql.jdbc.Driver.class.newInstance());
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sampleweb",
                "root",
                "fw0r"
        );
    }
    /**
     */
    public Statement createStatement() throws Exception{
        //return conn.createStatement();
        if(stmt != null){
            try{
                stmt.close();
            }catch(Exception e){
                
            }
        }
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                    ResultSet.CONCUR_READ_ONLY);
        return stmt;
    }
//    /**
//     */
//    public PreparedStatement prepareStatement(String query) throws Exception{
//        return conn.prepareStatement(query);
//    }
    /**
     */
    public void close(){
        if(stmt != null){
            try{
                stmt.close();
            }catch(Exception e){

            }
        }
        if(conn != null){
            try{
                conn.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
        }
    }
    /**
     */
    public void rollback() throws Exception{
        this.conn.rollback();
    }
    /**
     */
    public void commit() throws Exception{
        this.conn.commit();
    }
    /** 
     */
    public void setAutoCommit(boolean flag) throws Exception{
        this.conn.setAutoCommit(flag);
    }
}
