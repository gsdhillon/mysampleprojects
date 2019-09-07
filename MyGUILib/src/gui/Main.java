package gui;

import gui.mycomponents.MyButton;
import javax.swing.JOptionPane;

/**
 * Class Main
 * Created on Aug 24, 2013
 * @version 1.0.0
 * @author
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, new MyButton("Test Button") {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}
