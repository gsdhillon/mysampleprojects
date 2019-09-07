package gui;

import java.awt.Dimension;
import javax.swing.JPasswordField;

/**
 * @type     : Java Class
 * @name     : MyPasswordField
 * @file     : MyPasswordField.java
 * @created  : Aug 20, 2010 2:10:34 PM
 * @version  : 1.0.0
 */
public class MyPasswordField extends JPasswordField{
    /**
     * Constructor
     */
    public MyPasswordField(){
        super();
        decorate();
    }
    /**
     * Constructor
     */
    public MyPasswordField(String text){
        super(text);
        decorate();
    }

    /**
     * Constructor
     */
    public MyPasswordField(int width){
        super(width);
        decorate();
    }

    private void decorate() {
        setPreferredSize(new Dimension(getPreferredSize().width, 30));
        setFont(MyConstants.FONT_DATA);
    }
}