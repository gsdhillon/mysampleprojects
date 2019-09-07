package signerapp;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import javax.swing.JFrame;
/**
 * 
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class SignerApp {
    
    public static JFrame mainFrame = new JFrame();
    
    
    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/SignerApp", new MyHttpHandler());
//        server.createContext("/", new MyHttpHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        
        System.out.println("http://127.0.0.1:8000/SignerApp running... ");
//        System.out.println("http://127.0.0.1:8000/ running... ");
    }
}