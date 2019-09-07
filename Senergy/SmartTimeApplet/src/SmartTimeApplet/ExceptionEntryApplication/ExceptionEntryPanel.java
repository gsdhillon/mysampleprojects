/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ExceptionEntryApplication;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import lib.gui.MyLabel;
import lib.gui.MyPanel;

/**
 *
 * @author pradnya
 */
public class ExceptionEntryPanel extends MyPanel {

    public ExceptionEntryPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        MyPanel panMain = new MyPanel(new BorderLayout());

        MyPanel panNorth = new MyPanel(new BorderLayout());

        MyPanel panNorthNorth = new MyPanel(new BorderLayout());
        MyPanel panlbl = new MyPanel(new GridLayout(2, 1));
        MyLabel lblecode = new MyLabel(1, "Emp Code : ");
        panlbl.add(lblecode);

        MyLabel lblename = new MyLabel(1, "Emp Name : ");
        panlbl.add(lblename);

        panNorthNorth.add(panlbl, BorderLayout.WEST);

        MyPanel pantxt = new MyPanel(new GridLayout(2, 1));
        MyLabel lblshowecode = new MyLabel(1, "");
        pantxt.add(lblshowecode);

        MyLabel lblshowename = new MyLabel(1, "");
        pantxt.add(lblshowename);
        panNorthNorth.add(pantxt, BorderLayout.CENTER);
        
        
    }
}
