package gui;

import java.awt.Font;
import javax.swing.JTextArea;


/**
 * @type     : Java Class
 * @name     : MyTextArea
 * @file     : MyTextArea.java
 * @created  : Oct 24, 2010 4:21:39 PM
 * @version  : 1.0.0
 */
public class MyTextArea extends JTextArea{
    /**
     * Constructor
     */
    public MyTextArea(){
        super();
        setFont(new Font("MONOSPACED",Font.PLAIN,12));
    }

    /**
     * Constructor
     */
    public MyTextArea(int row, int col){
        super(row, col);
        setFont( MyConstants.FONT_DATA);
    }
}