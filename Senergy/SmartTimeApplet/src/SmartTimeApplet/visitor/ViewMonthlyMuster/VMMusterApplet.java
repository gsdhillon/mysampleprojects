/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.ViewMonthlyMuster;

import lib.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import lib.gui.MyPanel;

/**
 *
 * @author nbpatil
 */
public class VMMusterApplet extends MyApplet implements ActionListener{

    private Container contentPane;
    private MyPanel HomMyPanel;

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
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
    // TODO overwrite start(), stop() and destroy() methods

  public MyPanel CreateHomePanel() throws Exception
  {
    MyPanel Mainpan=new MyPanel(new BorderLayout());
            
    return Mainpan;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
