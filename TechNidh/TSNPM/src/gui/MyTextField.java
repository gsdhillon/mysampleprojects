package gui;

import java.awt.Dimension;
import javax.swing.JTextField;


/**
 * @type     : Java Class
 * @name     : MyTextField
 * @file     : MyTextField.java
 * @created  : Aug 20, 2010 1:48:12 PM
 * @version  : 1.2
 */
public class MyTextField extends JTextField{
    /**
     * Constructor
     */
    public MyTextField(){
        super();
        decorate();
    }
    /**
     * Constructor
     */
    public MyTextField(String text){
        super(text);
        decorate();
    }

    /**
     * Constructor
     */
    public MyTextField(int width){
        super(width);
        decorate();
    }

    private void decorate() {
        setPreferredSize(new Dimension(getPreferredSize().width, 24));
        setFont(MyConstants.FONT_DATA);
    }
}