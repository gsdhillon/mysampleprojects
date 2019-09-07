package lib.session;

import java.awt.Color;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.UIManager;

public class MyApplet extends JApplet {

    /**
     */
    @Override
    @SuppressWarnings({"CallToThreadDumpStack", "UseSpecificCatch"})
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setBackground(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MyUtils.setServerPath(this);
        } catch (Exception e) {
            MyUtils.showException("setting servlet path", e);
        }
    }

    /**
     */
    public void closeApplet() {
        try {
            stop();
            destroy();
            URL url = MyUtils.getRelativeURL("thanks.html");
            getAppletContext().showDocument(url, "_self");
        } catch (Exception e) {
            MyUtils.showException("closing applet", e);
        }
    }
    
    public void loadPage(String page, String target) {
        try {
            stop();
            destroy();
            URL url = MyUtils.getRelativeURL(page);
            getAppletContext().showDocument(url, target);
        } catch (Exception e) {
            MyUtils.showException("closing applet", e);
        }
    }
    
    public void reloadPage(String page, String target) {
        try {
            URL url = MyUtils.getRelativeURL(page);
            getAppletContext().showDocument(url, target);
        } catch (Exception e) {
            MyUtils.showException("closing applet", e);
        }
    }
}
