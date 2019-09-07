package gui.mycomponents;



import java.awt.Dimension;
import javax.swing.JTextField;

/**
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
        setPreferredSize(new Dimension(getPreferredSize().width, 30));
        setFont(MyConstants.FONT_DATA);
    }
}