package lib.gui;



import lib.gui.MyPanel;
import java.awt.Container;
import java.awt.GridLayout;


/**
 */
public class MyContainer extends Container{
    MyPanel panel = null;
    /**
     * Constructor
     */
    public MyContainer(){
        setLayout(new GridLayout(1, 1));
    }
    /**
     * 
     * @param p
     */
    public void addMyPanel(MyPanel p){
        if(panel != null){
            panel.close();
            removeAll();
        }
        panel = p;
        add(p);
        validate();
        repaint();
    }
    /**
     *
     */
    public void removeMyPanel(){
        if(panel != null){
            panel.close();
            removeAll();
        }
        validate();
        repaint();
    }
}