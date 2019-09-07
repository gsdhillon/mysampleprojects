package gui.mycomponents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 *
 * @author Administrator
 */
public abstract  class MyButton extends JButton implements ActionListener{
    private Color bgColor;
    public static final int BUTTON_FIT_WIDTH = 0;
    public static final int BUTTON_BIG = 1;
    public static final int BUTTON_SMALL = 2;
    /**
     * @param text
     */
    public MyButton(String text){
        super(text);
        set(BUTTON_FIT_WIDTH, new Color(240,226,219));
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
    private void set(int type, final Color bgColor){
        if(type==BUTTON_BIG){
            setPreferredSize(new Dimension(130, 35));
        }else if(type==BUTTON_SMALL){
            setPreferredSize(new Dimension(105, 30));
        }else {//type button BUTTON_FIT_WIDTH
           setPreferredSize(new Dimension(getPreferredSize().width, 25));
        }
        addActionListener(this);
        this.bgColor = bgColor;
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        final MyButton button = this;
        getModel().addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isRollover()) {
                button.setBackground(bgColor.darker());
            } else {
                button.setBackground(bgColor);
            }
            button.repaint();
         }
        });
//        addMouseListener(new java.awt.event.MouseAdapter() {  
//            @Override
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                button.setBackground(Color.BLUE);
//                button.repaint();
//                System.out.println("entered");
//            }
//
//            @Override
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                button.setBackground(bgColor);
//                button.repaint();
//                System.out.println("exited");
//            }
//        });
    }
    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if(isEnabled()){
            g.setColor(getBackground());
            g.fill3DRect(0, 0, getWidth(), getHeight(), true);
            Rectangle2D rect = getFont().getStringBounds(getText(), new FontRenderContext(null, true, true));
            int marginX = (int)(getWidth() - rect.getWidth())/2;
            int marginY = (int)(getHeight() - getFont().getSize())/2;
            g.setColor(MyConstants.FG_COLOR_BUTTON);
            g.drawString(getText(), marginX, (int)getFont().getSize() + marginY-2);
        }else{
            super.paint(g);
        }
//        Color backColor = getBackground();
//      //  super.paint(g);
//        Graphics2D g2d = (Graphics2D)g;
//        RenderingHints rh = g2d.getRenderingHints ();
//        rh.put (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHints (rh);
//        g2d.setColor(backColor);
//        g2d.fillRoundRect(4,4,getWidth()-8,getHeight()-8,8,8);
//        g2d.setColor(backColor);
//        g2d.draw3DRect(0, 0, getWidth(), getHeight(), true);
//        g2d.drawRoundRect(4,4,getWidth()-9,getHeight()-9,8,8);
//        FontRenderContext frc = new FontRenderContext(null, false, false);
//        Rectangle2D r = getFont().getStringBounds(getText(), frc);
//        float xMargin = (float)(getWidth()-r.getWidth())/2;
//        float yMargin = (float)(getHeight()-getFont().getSize())/2;
//        g2d.setColor(MyConstants.FG_COLOR_BUTTON);
//        g2d.drawString(getText(), xMargin, (float)getFont().getSize() + yMargin-2);
    }

    /**
     */
    public abstract void onClick();
    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       onClick();
    }
}