package gui;

import java.awt.Dimension;
import javax.swing.JComboBox;

/**
 * @type     : Java Class
 * @name     : MyComboBox
 * @file     : MyComboBox.java
 * @created  : May 15, 2011 1:48:12 PM
 * @version  : 1.2
 */
public class MyComboBox extends JComboBox{
    /**
     * Constructor
     */
    public MyComboBox(){
        super();
        decorate();
    }

    /**
     * Constructor
     */
    public MyComboBox(String[] data){
        super(data);
        decorate();
    }

    private void decorate() {
        setPreferredSize(new Dimension(getPreferredSize().width, 24));
        setFont(MyConstants.FONT_DATA);
    }
}