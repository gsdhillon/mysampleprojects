package gui.mycomponents;



import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 */
public abstract class MyKeyListener implements KeyListener {
    @Override
                            abstract public void keyTyped(KeyEvent e);

    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}