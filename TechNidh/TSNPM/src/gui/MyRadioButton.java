package gui;

import javax.swing.JRadioButton;

/**
 * @type     : Java Class
 * @name     : MyPasswordField
 * @file     : MyPasswordField.java
 * @created  : Aug 20, 2010 2:10:34 PM
 * @version  : 1.2
 */
public class MyRadioButton extends JRadioButton{
    public MyRadioButton(String text){
        super(text);
        setFont(MyConstants.FONT_DATA);
        setForeground(MyConstants.FG_COLOR_DATA);
        setBackground(MyConstants.BG_COLOR_INNER);
    }
}
