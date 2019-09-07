package applet;

import applet.session.MSC;
import applet.session.MyUtils;
import java.awt.GridLayout;
import lib.gui.MyContainer;
import lib.gui.MyPanel;
import lib.utils.Packetizer;

/**
 * Class LoginPage
 * Created on Sep 7, 2013
 * @version 1.0.0
 * @author
 */
public class LoginPage extends MyPanel{
    private MainApplet applet;
    private MyContainer mainPanel = new MyContainer();
    /**
     */
    public LoginPage(MainApplet applet){
        this.applet = applet;
        setLayout(new GridLayout(1,1));
        add(mainPanel);
        showLoginPanel();
    }
    /**
     * 
     * @param type
     * @param userID
     * @param password 
     */
    public void doLogin(int type, String userID, String password){
        try {
            Packetizer p = new Packetizer();
            p.addString(type+"");
            p.addString(userID);
            p.addString(password);

            MSC msc = MyUtils.getMSC("LoginServlet");
            msc.openOS();
            msc.println("userlogin");
            msc.println(p.getPacket());
            msc.closeOS();

            msc.openIS();
            String response = msc.readLine();
            msc.closeIS();
            
            if(response==null || !response.startsWith("OK")){
                MyUtils.showMessage("Login Failed:\n"+response);
            }else{
                applet.showHomePanel();
            }
        }catch(Exception e){
            MyUtils.showException("login failed: ", e);
        }
    }
    /**
     * 
     */
    public final void showLoginPanel() {
        mainPanel.addMyPanel(new LoginPanel(this));
    }
}