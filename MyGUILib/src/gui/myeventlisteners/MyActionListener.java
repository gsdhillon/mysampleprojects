package gui.myeventlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 */
public abstract class MyActionListener implements ActionListener{
    @Override
    abstract public void actionPerformed(ActionEvent ae);
}