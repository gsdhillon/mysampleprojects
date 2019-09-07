package gui;

import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * @type     : Java Class
 * @name     : MyFormPanel
 * @file     : MyFormPanel.java
 * @created  : Aug 11, 2010 1:25:56 PM
 * @version  : 1.0.0
 */
public class MyPanel extends JPanel{
    /**
     * Constructor
     */
    public MyPanel() {
        super();
        setBackground(MyConstants.BG_COLOR_INNER);
    }
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout) {
        super();
        if(layout!=null){
            setLayout(layout);
        }
        setBackground(MyConstants.BG_COLOR_INNER);
    }
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout, String name) {
        super();
        setBackground(MyConstants.BG_COLOR_INNER);
        if(layout!=null){
            setLayout(layout);
        }
        if(name!=null){
            setBorder(BorderFactory.createTitledBorder(name));
        }
    }
    /**
     * override this method if you wants to
     * release some resources before this panel
     * is removed from the container
     */
    public void close(){
    }
    public void setDefaultBG(){
        setBackground(MyConstants.BG_COLOR_OUTER);
    }
}
