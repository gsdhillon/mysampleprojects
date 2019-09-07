package lib;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import lib.gui.MyLoginDialog;
import lib.utils.MyFile;
import lib.utils.MyLog;
/**
 */
public class MyData{
    public static boolean debug = false;
    public static final String applicationName = "Applets";
    public static String dbServerIP;
    public static String userName;
    public static String password;
    public static String tokenDefaultPassword;
    public static String home;
    public static JFrame/*Main*/ mainFrame = new JFrame();
    public static int width = 1024;
    public static int height = 738;
    /**
     * 
     * @param mainFrame
     */
    public static void initialize(/*Main mainFrame*/){
     //   MyData.mainFrame = mainFrame;
        if(!setHome()){
            System.exit(0);
        }
        MyLog.setHome(home);
        MyLog.setDebug(debug);
        if(!readAllParameters()){
            System.exit(0);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        MyData.width = screenSize.width;
        MyData.height = screenSize.height-40;
        if(!login()){
            System.exit(0);
        }
        MyLog.login(userName);
    }
    /**
     *
     * @return
     */
    private static boolean login() {
        MyLoginDialog loginPanel = new MyLoginDialog("Login to "+applicationName, userName) {
            @Override
            public boolean login(String user, char[] pass) {
                return true;
            }
            @Override
            public void callCancel() {
                MyLog.showDebugMessage("Login Canceled.");
            }
        };
        return loginPanel.isLoggedIn();
    }
    /**
     *
     * @return
     */
    private static boolean readAllParameters() {
        //
        try{
            dbServerIP = MyFile.readParameter(new File(home+"/settings.txt"), "DBServerIP");
            userName = MyFile.readParameter(new File(home+"/settings.txt"), "UserName");
            if(debug){
                System.out.println("Using DataBase - "+dbServerIP);
            }
            return true;
        }catch(Exception e){
            MyLog.showException("Reading settings", e);
            return false;
        }
    }
    /**
     *
     * @return
     */
    private static boolean setHome() {
        try{
            //get the abselute path of MyIO.class file
            String s = MyData.class.getResource("MyData.class").getPath();
            s = s.replaceAll("%20", " ");
            //System.out.showDebugMessage(s);
            home = s.substring(s.indexOf('/')+1,s.indexOf(applicationName)+applicationName.length());
            home = home.replaceAll("/", "\\\\");
            System.out.println(home);
            return true;
        }catch(Exception e){
            MyLog.showException("SetHome", e);
            return false;
        }
    }
}