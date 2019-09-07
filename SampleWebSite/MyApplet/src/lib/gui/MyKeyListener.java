package lib.gui;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 */
public abstract class MyKeyListener implements KeyListener {
    abstract public void keyTyped(KeyEvent e);
    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
}