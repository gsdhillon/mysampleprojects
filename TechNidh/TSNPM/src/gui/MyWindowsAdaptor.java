package gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class MyWindowsAdaptor implements WindowListener{

    public void windowActivated(WindowEvent arg0) {
    }

    public void windowClosed(WindowEvent arg0) {
    }

    public void windowDeactivated(WindowEvent arg0) {
    }

    public void windowDeiconified(WindowEvent arg0) {
    }

    public void windowIconified(WindowEvent arg0) {
    }

    public void windowOpened(WindowEvent arg0) {
    }

    public abstract void windowClosing(WindowEvent e);

}
