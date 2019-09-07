package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * @type     : Java Class
 * @name     : MyKeyAdaptor
 * @file     : MyKeyAdaptor.java
 * @created  : Aug 19, 2010 3:52:07 PM
 * @version  : 1.2
 */
public abstract class MyKeyAdaptor implements KeyListener {
    abstract public void keyTyped(KeyEvent e);

    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
}