package lib.gui;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 */
public class MyPanel extends JPanel{
    private Color defaultBG;
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout, String name) {
        super();
        setBG();
        if(layout != null){
            setLayout(layout);
        }
        if(name != null){
            setBorder(BorderFactory.createTitledBorder(name));
        }
    }
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout) {
        super();
        if(layout != null){
            setLayout(layout);
        }
        setBG();
    }
    /**
     * Constructor
     */
    public MyPanel() {
        super();
        setBG();
    }

    /**
     *
     */
    private void setBG() {
        defaultBG = getBackground();
        setBackground(MyConstants.BG_COLOR);
    }

    public void setDefaultBG(){
        setBackground(defaultBG);
    }

    /**
     * override this method if you wants to
     * release some resources before this panel
     * is removed from the container
     */
    public void close(){

    }
}
