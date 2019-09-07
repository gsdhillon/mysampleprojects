package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mmi.CommPort.MyCommPort;
/**
 * @type     : Java Class
 * @name     : MyWaitDialog
 * @file     : MyWaitDialog.java
 * @created  : Aug 18, 2010 1:25:56 PM
 * @version  : 1.0.0
 */
public class MyWaitDialog extends JDialog{
    public MyLabel status;
    private MyWindowsAdaptor windowAdaptor = new MyWindowsAdaptor() {
        @Override
        public void windowClosing(WindowEvent e) {
            if(JOptionPane.YES_OPTION ==
                JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to cancel the process",
                        null,
                        JOptionPane.YES_NO_OPTION)){
                MyCommPort.STOPPED = true;
                setMessage("Canceling ...");
            }
        }
    };
    /**
     *
     */
    public MyWaitDialog(JFrame mainFrame, int width, int height){
        super(mainFrame, "", true);
        getContentPane().setBackground(MyConstants.BG_COLOR_INNER);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(mainFrame);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        status = new MyLabel(MyLabel.TYPE_DATA);
        getContentPane().add(status);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(windowAdaptor);
    }
    /**
     *
     * @param msg
     */
    public void setMessage(String msg){
        status.setText(msg);
    }
}