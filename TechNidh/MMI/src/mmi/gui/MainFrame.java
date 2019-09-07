package mmi.gui;

import gui.MyWindowsAdaptor;
import java.awt.event.WindowEvent;
import mmi.data.MyData;
import gui.MyPanel;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * @type     : Java Class
 * @name     : MainFrame
 * @file     : MainFrame.java
 * @created  : Jan 11, 2011 4:46:29 PM
 * @version  : 1.0.0
 */
public final class MainFrame extends JFrame{
    private MyWindowsAdaptor windowAdaptor = new MyWindowsAdaptor() {
        @Override
        public void windowClosing(WindowEvent e) {
            MyData.closeApplication();
        }
    };
    /**
     * Constructor
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public MainFrame(){
        super(MyData.applicationName);
        MyData.initialize(this);
        setIconImage(Toolkit.getDefaultToolkit().getImage(MyData.appHome+"/Icons/Main"));
        getContentPane().setLayout(new GridLayout(1,1));
        addMyPanel(new LoginPanel());
        setSize(MyData.width, MyData.height);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(windowAdaptor);
    }
    /**
     *
     * @param p
     */
    public void addMyPanel(MyPanel p){
        getContentPane().removeAll();
        getContentPane().add(p);
        getContentPane().validate();
        getContentPane().repaint();
    }
}