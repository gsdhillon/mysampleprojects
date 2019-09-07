package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @type     : Java Class
 * @name     : MyMouseAdaptor
 * @file     : MyMouseAdaptor.java
 * @created  : Aug 19, 2010 3:52:07 PM
 * @version  : 1.0.0
 */
public abstract class MyMouseAdaptor implements MouseListener {
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