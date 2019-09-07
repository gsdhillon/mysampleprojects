package PKIServer;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * MCC.java
 * Created on Mar 26, 2008, 12:55:39 PM
 * @author Gurmeet Singh
 */
public class MCC {
    /**
     * @param session
     * @param pw
     * @param loginType
     */
    public MCC(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        resp.setContentType("text/plain");
        bufferedReader = req.getReader();
        printWriter = resp.getWriter();
    }
    /**
     * 
     * @return
     * @throws java.lang.Exception
     */
    public String readRequest() throws Exception{
        return bufferedReader.readLine();
    }
    /**
     * 
     * @param packet
     */
    public void println(String packet){
        printWriter.println(packet);
    }
    /**
     * 
     * @param packet
     */
    public void sendException(Exception e){
        sendError(e.toString().replaceAll("\n",";"));
    }
    /**
     * 
     * @param packet
     */
    public void sendError(String msg){
        println("ERROR:"+msg);
    }
    /**
     * 
     * @return
     * @throws java.lang.Exception
     */
    public String readLine() throws Exception{
        return bufferedReader.readLine();
    }
    /**
     * 
     * @throws java.lang.Exception
     */
    public void close() throws Exception{
        printWriter.close();
        bufferedReader.close();
    }
    //object
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
}