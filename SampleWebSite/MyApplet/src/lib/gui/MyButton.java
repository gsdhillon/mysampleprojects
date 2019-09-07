package lib.gui;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
/**
 *
 * @author Administrator
 */
public abstract class MyButton extends JButton implements ActionListener{
    private Color bgColor;
    public static final int BUTTON_FIT_WIDTH = 0;
    public static final int BUTTON_BIG = 1;
    public static final int BUTTON_SMALL = 2;
    /**
     * @param text
     */
    public MyButton(String text){
        super(text);
        set(BUTTON_FIT_WIDTH, MyConstants.BG_COLOR_BUTTON);
    }
    /**
     * @param text
     * @param type if type 1 height = 35 else height = 30
     */
    public MyButton(String text, int type){
        super(text);
        set(type, MyConstants.BG_COLOR_BUTTON);
    }
    /**
     * @param text
     * @param type
     * @param bgColor
     */
    public MyButton(String text, int type, Color bgColor){
        super(text);
        set(type, bgColor);
    }
    /**
     *
     * @param listener
     * @param actionCommand
     * @param type
     * @param bgColor
     */
    private void set(int type, Color bgColor){
        if(type==BUTTON_BIG){
            setPreferredSize(new Dimension(130, 35));
        }else if(type==BUTTON_SMALL){
            setPreferredSize(new Dimension(105, 30));
        }else {//type button BUTTON_FIT_WIDTH
           setPreferredSize(new Dimension(getPreferredSize().width, 30));
        }
        addActionListener(this);
        this.bgColor = bgColor;
    }
    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        Color backColor = bgColor;
        if(!isEnabled()){
            backColor = bgColor.darker().darker();
        }
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = g2d.getRenderingHints ();
        rh.put (RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints (rh);
        g2d.setColor(backColor);
        g2d.fillRoundRect(4,4,getWidth()-8,getHeight()-8,8,8);
        g2d.setColor(backColor);
        g2d.drawRoundRect(4,4,getWidth()-9,getHeight()-9,8,8);
        FontRenderContext frc = new FontRenderContext(null, false, false);
        Rectangle2D r = getFont().getStringBounds(getText(), frc);
        float xMargin = (float)(getWidth()-r.getWidth())/2;
        float yMargin = (float)(getHeight()-getFont().getSize())/2;
        g2d.setColor(MyConstants.FG_COLOR_BUTTON);
        g2d.drawString(getText(), xMargin, (float)getFont().getSize() + yMargin-2);
    }
    /**
     */
    public abstract void onClick();
    /**
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        onClick();
    }
}