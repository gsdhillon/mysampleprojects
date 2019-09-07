/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.ViewAllApplied;

import java.awt.BorderLayout;
import java.awt.Container;
import lib.gui.MyPanel;
import lib.session.MyApplet;

/**
 *
 * @author nbpatil
 */
public class ViewAllAppliedForm extends MyApplet {
    private Container contentPane;
    private MyPanel HomMyPanel;


    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    @Override
    public void init() {
        try {
            super.init();
            contentPane=getContentPane();
            HomMyPanel=CreateHomePanel();
            contentPane.add(HomMyPanel);
            contentPane.validate();
            contentPane.repaint();
        } catch (Exception e) {
        }
        
        
        // TODO start asynchronous download of heavy resources
    }
    public MyPanel CreateHomePanel() throws Exception{
    
   MyPanel Mainpan=new MyPanel(new BorderLayout());
   
   return Mainpan;
    }
    
    // TODO overwrite start(), stop() and destroy() methods
}
