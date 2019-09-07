package applet.session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLConnection;

/**
 */
public class MSC {
    private URLConnection conn;
    private InputStream is = null;
    private BufferedReader br = null;
    private ObjectInputStream objIS = null;
    private OutputStream os = null;  
    private PrintStream ps = null;
    private ObjectOutputStream objOS = null;
    /**
     * 
     * @param conn
     * @throws Exception 
     */
    public MSC(URLConnection conn) throws Exception{
        this.conn = conn;  
    }
    /**
     * 
     * @throws Exception 
     */
    public void openOS() throws Exception{
        os = conn.getOutputStream();
    }
    /**
     * 
     * @param p 
     */
    public void println(String packet){
        if(ps == null){
            ps = new PrintStream(os);
        }
        
        ps.println(packet);
        ps.flush();
    }
    
    /**
     * 
     * @param packet
     */
    public void write(byte[] buffer, int off, int len) throws Exception{
        os.write(buffer, off, len);
        os.flush();
    }
    /**
     * 
     * @return
     * @throws Exception 
     */
    public OutputStream getOS() throws Exception{
        if(os==null){
            os = conn.getOutputStream();
        }
        return os;
    }
    
    /**
     * 
     * @throws Exception 
     */
    public void openIS() throws Exception{
        is = conn.getInputStream();
    }
    /**
     * 
     * @return '' 
     */
    public InputStream getIS() throws Exception{
        if(is == null){
            is = conn.getInputStream();
        }
        return is;
    }
    /**
     * 
     * @throws Exception 
     */
    public Object readObject() throws Exception{
        if(is == null){
            throw new Exception("OpenIS not called");
        }
        
        //
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
     * read data upto new line character <BR>
     * return data without new line character 
     * @return '' String
     * @throws java.lang.Exception
     */
    public String readLine() throws Exception{
        if(is == null){
            throw new Exception("OpenIS not called");
        }
        
        //
        if(br == null){
            br  = new BufferedReader(new InputStreamReader(is));
        }
        
        String packet = br.readLine();
        
        if(packet == null){
            packet = "ERROR:Server response NULL";
        }else if(packet.startsWith("ERROR")){
            packet.replaceAll(";", "\n");
        }
        return packet;
    }
    /**
     * 
     * @throws Exception 
     */
    public void closeIS() throws Exception{
        if(is != null){
            is.close();
        }
    }
    /**
     * 
     * @throws Exception 
     */
    public void closeOS() throws Exception{
        if(os != null){
            os.close();
        }
    }
}