package gui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.ImageIcon;


/**
 * @type     : Java Class
 * @name     : MyButton
 * @file     : MyButton.java
 * @created  : Aug 11, 2010 1:49:06 PM
 * @author   : Gurmeet Singh, Computer Division, BARC, Mumbai.
 */
public class MyButton extends JButton{
    public static String iconPath = ".";
    private static int t = 0;
    private static int l = 0;
    private static int b = 0;
    private static int r = 0;
    public static void setInset(int top, int left, int bottom, int right){
        t = top;
        l = left;
        b = bottom;
        r = right;
    }
    public static int horizontalGap = 0;

    public static MyPanel getButtonPanel() {
        MyPanel bp =new MyPanel(new FlowLayout(FlowLayout.CENTER,horizontalGap,1));
        //bp.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //bp.setPreferredSize(new Dimension(600, 30));
        return bp;
    }
    /**
     * 
     * @param listener
     * @param actionCommand
     * @param icon
     */
    public MyButton(ActionListener listener, String actionCommand, String label){
        super(label);
        addActionListener(listener);
        setActionCommand(actionCommand);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(t, l, b, r));
    }
    /**
     *
     * @param listener
     * @param actionCommand
     * @param icon
     */
    public MyButton(ActionListener listener, String actionCommand, String label, String iconName){
        addActionListener(listener);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(t, l, b, r));
        setCommandTextIcon(actionCommand, label, iconName);
    }

    public final void setCommandTextIcon(String actionCommand, String label, String iconName){
        setActionCommand(actionCommand);
        setToolTipText(actionCommand);
        setText(label);
        try{
            File iconFile = new File(iconPath+"/"+iconName);
            if(iconFile.exists()){
                ImageIcon icon = new ImageIcon(iconFile.getAbsolutePath());
                if(icon.getIconHeight()<5){
                    if(label==null||label.length()==0){
                        setText(iconName);
                    }
                }else{
                    setIcon(icon);
                }
            }else{
               if(label==null||label.length()==0){
                    setText(iconName);
               }
            }
        }catch(Exception e){
            if(label==null||label.length()==0){
                setText(iconName);
            }
        }
    }
    /**
     * 
     * @param actionCommand
     * @param label
     */
    public void setCommandText(String actionCommand, String label) {
        setActionCommand(actionCommand);
        setText(label);
    }
}