/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports;

import lib.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author pradnya
 */
public class ProgressDialog extends JDialog {

    public JProgressBar bar;
    public static boolean barvalue = true;

    public ProgressDialog(MyApplet applet, String title) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setPreferredSize(new Dimension(175, 20));
        pb.setString("Working");
        pb.setStringPainted(true);
        pb.setValue(0);

//        JLabel label = new JLabel("Progress: ");

        MyApplet center_panel = new MyApplet();
//        center_panel.add(label);
        center_panel.add(pb);

        getContentPane().add(center_panel, BorderLayout.CENTER);
        pack();
        setVisible(true);

        setLocationRelativeTo(null); // center on screen
        setLocation(550, 25); // position by coordinates
        toFront(); // raise above other java windows
        pb.setValue(25);
        pb.setStringPainted(true);
    }

    public void disposeProgress() {
        dispose();
    }
}