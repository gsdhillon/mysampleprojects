package lib;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import lib.utils.MyLog;
/**
 */
public class Main extends JFrame{
    /**
     *
     */
    public Main(){
        try {
            setLayout(new BorderLayout());
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        //MyLog.setHome(".");
        MyLog.setDebug(true);
        MyLog.showDebugMessage("Debug is on");
    }
}