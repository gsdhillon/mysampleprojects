package gui.mydialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;
/**
 */
public class MyProgressBar extends JDialog{
    private int max = 20;
    private int currentVal = 0;
    private JProgressBar pb = new JProgressBar(0, max);
    private JLabel status;
    private JLabel label;

    public MyProgressBar(JFrame mainFrame, String message){

        super(mainFrame, "", true);
        int width = 600;
        int height = 120;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
        setSize(new Dimension(width, height));
        setLayout(new GridLayout(3,1));
        status = new JLabel(message);
        add(status);
        add(pb);

        label = new JLabel("");
        add(label);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void done() {
        setVisible(false);
    }

    public void msg(String msg){
        status.setText(msg);
    }

    public void setMax(int max){
        this.max = max;
        pb.setMaximum(max);
    }

    public void setValue(int val){
        currentVal = val;
        pb.setValue(val);
        label.setText(val+" out of "+max);
    }

    public void incrValue(){
        currentVal++;
        pb.setValue(currentVal);
        label.setText(currentVal+" out of "+max);
    }
}