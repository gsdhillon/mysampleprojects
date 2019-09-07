package gui;

import cpnds.CommPort.MyCommPort;
import cpnds.data.MyData;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
/**
 * @type     : Java Class
 * @name     : MyWaitDialog
 * @file     : MyWaitDialog.java
 * @created  : Aug 18, 2010 1:25:56 PM
 * @version  : 1.2
 */
public class MyWaitDialog extends JDialog{
    public MyLabel status;
    private MyWindowsAdaptor windowAdaptor = new MyWindowsAdaptor() {
        @Override
        public void windowClosing(WindowEvent e) {
            if(MyData.showConfirm("Do you want to cancel the process")){
                MyCommPort.STOPPED = true;
                setMessage("Canceling ...");
                //setVisible(false);
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