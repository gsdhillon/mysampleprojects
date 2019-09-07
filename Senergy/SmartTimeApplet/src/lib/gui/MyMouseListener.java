package lib.gui;



import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 */
public abstract class MyMouseListener implements MouseListener {
    abstract public void mouseClicked(MouseEvent e);

    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}