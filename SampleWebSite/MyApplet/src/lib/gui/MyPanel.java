package lib.gui;



import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 */
public class MyPanel extends JPanel{
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout, String name) {
        super();
        setBackground(MyConstants.BG_COLOR);
        if(layout!=null){
            setLayout(layout);
        }
        if(name!=null){
            setBorder(BorderFactory.createTitledBorder(name));
        }
    }
    /**
     * Constructor
     */
    public MyPanel(LayoutManager layout) {
        super();
        if(layout!=null){
            setLayout(layout);
        }
        setBackground(MyConstants.BG_COLOR);
    }
    /**
     * Constructor
     */
    public MyPanel() {
        super();
        setBackground(MyConstants.BG_COLOR);
    }
    /**
     * override this method if you wants to
     * release some resources before this panel
     * is removed from the container
     */
    public void close(){
        
    }
}
