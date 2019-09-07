package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * @type     : Java Class
 * @name     : MyKeyListener
 * @file     : MyKeyListener.java
 * @created  : Aug 19, 2010 3:52:07 PM
 * @author   : Gurmeet Singh, Computer Division, BARC, Mumbai.
 */
public abstract class MyKeyListener implements KeyListener {
    abstract public void keyTyped(KeyEvent e);

    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
}