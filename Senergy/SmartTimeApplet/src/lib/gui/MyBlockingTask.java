package lib.gui;


import lib.gui.MyLabel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
/**
 */
public abstract class MyBlockingTask extends JDialog{
    private int n = 0;
    private boolean stoped = false;
    public MyLabel msgLabel = new MyLabel(MyLabel.TYPE_LABEL, "", MyLabel.ALIGN_CENTER);
    private MyLabel progressLabel = new MyLabel(MyLabel.TYPE_LABEL, "", MyLabel.ALIGN_CENTER);
    /**
     *
     * @param frame
     * @param taskName
     */
    public MyBlockingTask(String taskName){
        super(new JFrame(), "", true);
        setBackground(MyConstants.BG_COLOR);
        setLayout(new GridLayout(3,1));
        msgLabel.setText(taskName);
        add(msgLabel);
        add(new MyLabel(MyLabel.TYPE_DATA, "", MyLabel.ALIGN_CENTER));
        add(progressLabel);
        int width = 250;
        int height = 120;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    /**
     *
     */
    public abstract void task();
    /**
     *
     */
    public void performTask() {
        n = 0;
        stoped = false;
        new Thread(){
            @Override
            public void run(){
                while(!stoped){
                    progressLabel.setText("Time taken "+(n++)+" seconds");
                    try{Thread.sleep(1000);}catch(Exception x){}
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run(){
                task();
                stoped = true;
                setVisible(false);
            }
        }.start();
        setVisible(true);
    }
}