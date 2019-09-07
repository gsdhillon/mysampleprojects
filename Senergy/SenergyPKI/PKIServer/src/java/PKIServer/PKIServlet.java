package PKIServer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PKIServlet.java
 * Created on Jun 28, 2009, 10:51:29 AM
 * @author Gurmeet Singh
 */
public class PKIServlet extends HttpServlet {
   /**
     * process request of client programs
     * @param req
     * @param resp
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){
        try{
            /**resp.setContentType("text/plain");
            BufferedReader bufferedReader = req.getReader();
            String request = bufferedReader.readLine();
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(request);*/
            MCC mcc = new MCC(req, resp);
            String msg = mcc.readRequest();
            
            if(msg.equals("list_of_employee")){
                getListOfEmployee(mcc);
            }else if(msg.equals("getcertificate_empno")){
                getCertificateByEmp(mcc);
            }else if(msg.equals("getcertificate_sno")){
                getCertificateBySNO(mcc);
            }else if(msg.equals("update_certificate")){
                updateCertificate(mcc);
            }else if(msg.equals("register_emp")){
                registerEmployee(mcc);
            }else{
                mcc.println("ERROR:unknown request");
            }
            mcc.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * get the certificate of empno
     * @param mcc
     */
    private void getCertificateByEmp(MCC mcc){
        Connection conn = null; 
        try{
            //get connection
            conn = DBConnection.getConnectiion();
            if(conn==null){
                mcc.sendError("Could not get database connection");
                return;
            }
            String empno = mcc.readLine();
            Statement stmt = conn.createStatement();
            ResultSet rs = null;
            String query = 
            "select " +
                    "certificate " +
            "from " +
                    "emp_cert " +
            "where " +
                    "empno = "+empno;
            rs = stmt.executeQuery(query);
            String result;
            if(rs.next()){
                result = rs.getString(1);
            }else{
                result = "ERROR:certificate not found.";
            }
            rs.close();
            stmt.close();
            conn.close();
            mcc.println(result);
        }catch(Exception e){
            if(conn!=null) {
                try{
                    conn.close();
                }catch(Exception e1){
                }
            }
            mcc.sendException(e);
            e.printStackTrace();
        }
    }
    /**
     * get certificate of given sno
     * @param mcc
     */
    private void getCertificateBySNO(MCC mcc){
        Connection conn = null; 
        try{
            //get connection
            conn = DBConnection.getConnectiion();
            if(conn==null){
                mcc.sendError("Could not get database connection");
                return;
            }
            String sno = mcc.readLine();
            Statement stmt = conn.createStatement();
            ResultSet rs = null;
            String query = 
            "select " +
                    "empno, " +
                    "certificate " +
            "from " +
                    "old_cert " +
            "where " +
                    "cert_sno = '"+sno+"'";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                String empno = rs.getString("empno");
                String certificate = rs.getString("certificate");
                mcc.println(certificate);
                mcc.println(empno);
            }else{
                mcc.println("ERROR:certificate not found.");
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(Exception e){
            if(conn!=null) {
                try{
                    conn.close();
                }catch(Exception e1){
                }
            }
            mcc.sendException(e);
            e.printStackTrace();
        }
    }
    /**
     * update the certificate of empno
     * @param mcc
     */
    private void updateCertificate(MCC mcc){
        Connection conn = null; 
        try{
            //get connection
            conn = DBConnection.getConnectiion();
            if(conn==null){
                mcc.sendError("Could not get database connection");
                return;
            }
            conn.setAutoCommit(false);
            String empno = mcc.readLine();
            String cert_sno = mcc.readLine();
            //required date format dd/mm/yyyy hh:mm:ss
            String exp_date = mcc.readLine();
            //System.out.println("++++++++++exp_date = "+exp_date);
            String certificate = mcc.readLine();
            Statement stmt = conn.createStatement();
            String query = 
            "insert into old_cert (" +
                    "CERT_SNO, " +
                    "EMPNO, " +
                    "EXP_DATE, " +
                    "CERTIFICATE " +
            ") values (" +
                    "'"+cert_sno+"', "+
                    empno+", "+
                    "str_to_date('"+exp_date+"', '%d/%m/%Y %T'), "+
                    "'"+certificate+"' "+
            ")";
            if(stmt.executeUpdate(query)!=1){
                conn.rollback();
                stmt.close();
                conn.close();
                mcc.println("ERROR:INSERT INTO OLD_CERT FAILED.");
                return;
            }
            query = 
            "update " +
                    "emp_cert " +
            "set " +
                    "cert_sno = '"+cert_sno+"', "+
                    "exp_date = str_to_date('"+exp_date+"', '%d/%m/%Y %T'),"+
                    "certificate = '"+certificate+"' "+
            "where "+
                    "empno = "+empno;
            if(stmt.executeUpdate(query)!=1){
                stmt.close();
                conn.close();
                mcc.println("ERROR:UPDATE EMP_CERT FAILED.");
                return;
            }
            conn.commit();
            stmt.close();
            conn.close();
            //return packet
            mcc.println("OK:CERTIFICATE UPDATED SUCCEESSFULLY.");
        }catch(Exception e){
            if(conn!=null) {
                try{
                    conn.rollback();
                    conn.close();
                }catch(Exception e1){
                }
            }
            mcc.sendException(e);
            e.printStackTrace();
        }
    }
    /**
     * @param mcc
     */
    private void getListOfEmployee(MCC mcc){
        Connection conn = null; 
        try{
            //get connection
            conn = DBConnection.getConnectiion();
            if(conn==null){
                mcc.sendError("Could not get database connection");
                return;
            }
            Statement stmt = conn.createStatement();
            String query = 
            "select " +
                    "empno, " +
                    "name, " +
                    "cert_sno, "+
                    "DATE_FORMAT(exp_date, '%d/%m/%Y %T') exp_date " +
            "from "+
                    "emp_cert";
            
            ResultSet rs = stmt.executeQuery(query);
            String result="List of Registered Employee: -;";
            int count = 0;
            while(rs.next()){
                result+=
                rs.getString("empno")+" - "+
                rs.getString("name")+" - "+
                rs.getString("cert_sno")+" - "+
                rs.getString("exp_date")+";";
                count++;
            }
            result += "Total = "+count;
            rs.close();
            stmt.close();
            conn.close();
            //return packet
            mcc.println(result);
        }catch(Exception e){
            if(conn!=null) {
                try{
                    conn.close();
                }catch(Exception e1){
                }
            }
            mcc.sendException(e);
            e.printStackTrace();
        }
    }
    /**
     * insert new record in emp_cert table with empno and name
     * @param mcc
     */
    private void registerEmployee(MCC mcc){
        Connection conn = null; 
        try{
            //get connection
            conn = DBConnection.getConnectiion();
            if(conn==null){
                mcc.sendError("Could not get database connection");
                return;
            }
            String empno = mcc.readLine();
            String name = mcc.readLine();
            String query = 
            "insert into emp_cert (" +
                    "empno, " +
                    "name " +
            ") values (" +
                    empno+","+
                    "?"+
            ")";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            String result;
            if(ps.executeUpdate()==1){
                result = "OK:user "+name+" registered successfully.";
            }else {
                result = "ERROR:Could not register user.";
            }
            ps.close();
            conn.close();
            //return packet
            mcc.println(result);
        }catch(Exception e){
            if(conn!=null) {
                try{
                    conn.close();
                }catch(Exception e1){
                }
            }
            mcc.sendException(e);
            e.printStackTrace();
        }
    }
    /**
     * return "Error: this servlet does not support the GET method!"
     * @param req
     * @param resp
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){
        String remoteAddr = req.getRemoteAddr();
        try{
            resp.setContentType("text/plain");
            PrintWriter out = resp.getWriter();
            out.println("Error: this servlet does not support the GET method!");
            out.close();
        }catch(Exception e){
            System.out.println("GET request from "+remoteAddr);
            e.printStackTrace();
        }
    }
}