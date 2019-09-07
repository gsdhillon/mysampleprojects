package javacode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * MCC.java
 * Created on Mar 26, 2008, 12:55:39 PM
 * @author 
 */
public class MCC {
    private HttpServletRequest req;
    private HttpServletResponse resp;
    //
    private InputStream is = null;  
    private BufferedReader br = null;
    private ObjectInputStream objIS = null;
    //
    private OutputStream os = null;  
    private PrintWriter pw = null;
    private ObjectOutputStream objOS = null;
    /**
     * 
     * @param req
     * @param resp
     * @throws Exception 
     */
    public MCC(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        this.req = req;
        this.resp = resp;    
        is = req.getInputStream();
        os = (OutputStream) resp.getOutputStream();
    }
    
    /**
     * 
     * @param packet
     */
    public void println(String packet) {
        try{
            if(pw == null){
                pw = new PrintWriter(os);  
            }
            //System.out.println("************"+packet);
            pw.println(packet);
            pw.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @throws Exception 
     */
    public Object readObject() throws Exception{
        if(objIS == null){
            objIS = new ObjectInputStream(is);
        }
        return objIS.readObject();
    }
    
    /**
     * 
     * @param obj
     * @throws Exception 
     */
    public void writeObject(Object obj) throws Exception{
        if(objOS == null){
            objOS = new ObjectOutputStream(os);
        }
        objOS.writeObject(obj);
        objOS.flush();
    }
    
    
    /**
     * 
     * @return ''
     * @throws Exception 
     */
    public String readLine() throws Exception{
        if(br == null){
            br = new BufferedReader(new InputStreamReader(is));
        }
        return br.readLine();
    }

    
    /**
     * 
     * @param packet
     */
    public void write(byte[] buffer, int off, int len) throws Exception{
        os.write(buffer, off, len);
    }
    
   /**
     * 
     * @throws Exception 
     */
    public void setRespContentType(String type) throws Exception{
        resp.setContentType(type);
    }
    
    /**
     * 
     * @throws Exception 
     */
    public OutputStream getOS() throws Exception{
        return os;
    }
    /**
     * 
     * @throws Exception 
     */
    public InputStream getIS() throws Exception{
        return is;
    }    
    
    /**
     * 
     * @param br
     * @param pw 
     */
    public void close() {
        try{
            if(is != null) is.close();
            if(br != null) br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            if(os != null) os.close();
            if(pw != null) pw.close();
            if(objOS != null) objOS.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}