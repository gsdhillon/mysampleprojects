package pkiadmin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
/**
 * MSC.java
 * Created on Mar 28, 2009, 10:51:29 AM
 * @author  Gurmeet Singh, Computer Division, BARC, Mumbai
 * @version 1.0.0
 * @since   1.0.0
 */
public class MSC {
    public static String appBase = null;
    private URLConnection sc;
    /**
     * create new connection object
     * @param servlet  - name of the servlet
     * @throws java.lang.Exception
     */
    public MSC(String servlet) throws Exception{
        if(MSC.appBase == null){
            throw new Exception("Application Base Not Set");
        }
        URL servletURL = new URL(MSC.appBase+"/"+servlet);
        sc = servletURL.openConnection();
        sc.setDoOutput(true); 
        sc.setUseCaches(false);
    }
    /**
     * set application base
     * @param appBase
     */
    public static void setServerURLBase(String appBase){
        MSC.appBase = appBase;
    }
    /**
     * 
     * @return
     * @throws java.lang.Exception
     */
    public PrintStream getPS() throws Exception{
        return new PrintStream(sc.getOutputStream());
    }
    /**
     * 
     * @return
     * @throws java.lang.Exception
     */
    public  BufferedReader getBR() throws Exception{
        return new BufferedReader(new InputStreamReader(sc.getInputStream()));
    }
}