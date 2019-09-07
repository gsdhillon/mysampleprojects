package applet.session;

import java.net.URL;
import javax.swing.JApplet;
import javax.swing.UIManager;

/**
 * MyApplet.java
 * Created on Aug, 18, 2011, 13:01:12 PM
 * Modified on Aug, 18, 2011, 13:01:12 PM
 * @author 
 * @version 3.0
 */
public class MyApplet extends JApplet{
    /**
     */
    @Override
    public void init(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            //e.printStackTrace();
        }
        try{
            MyUtils.setServerPath(this);/*Changed on 10-08-11*/
        }catch(Exception e){
            MyUtils.showException("setting servlet path", e);
        }
    }
    /**
     */
    public void showThanks(){
        try{
            stop();
            destroy();
            URL url = MyUtils.getRelativeURL("thanks.html");
            getAppletContext().showDocument(url,"_self");
        }catch(Exception e){
            MyUtils.showException("closing applet", e);
        }
    }
}
