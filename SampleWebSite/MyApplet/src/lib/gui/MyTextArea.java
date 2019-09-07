package lib.gui;



import java.awt.Font;
import javax.swing.JTextArea;


/**
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
        setFont(new Font("MONOSPACED",Font.PLAIN,12));
    }
}