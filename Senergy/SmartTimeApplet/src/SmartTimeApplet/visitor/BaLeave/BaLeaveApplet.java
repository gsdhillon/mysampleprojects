/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.BaLeave;

import lib.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Container;
import lib.gui.MyPanel;

/**
 *
 * @author nbpatil
 */
public class BaLeaveApplet extends MyApplet {
    private Container contentPane;
    private MyPanel HomePanel;

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        try {
            super.init();
            contentPane=getContentPane();
         
        } catch (Exception e) {
        }
        // TODO start asynchronous download of heavy resources
    }
    // TODO overwrite start(), stop() and destroy() methods

    public MyPanel CreateHomePanel() throws Exception{
    MyPanel Mainpan=new MyPanel(new BorderLayout());
    
    return Mainpan;
        
    }
}
