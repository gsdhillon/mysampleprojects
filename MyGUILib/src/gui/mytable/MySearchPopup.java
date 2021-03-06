package gui.mytable;

import gui.mycomponents.MyKeyListener;
import gui.mycomponents.MyTextField;
import gui.myeventlisteners.MyMouseListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JPopupMenu;
/**
 */
public abstract class MySearchPopup extends JPopupMenu  implements Serializable{
    private MyTableModel tm;
    private MyTable table;
    private MyTextField tf;
    /**
     *
     * @param tf
     * @param tm
     */
    public MySearchPopup(MyTextField tf, MyTableModel tm){
        try {
            this.tf = tf;
            this.tm = tm;
            tf.addKeyListener(new MyKeyListener() {
                @Override
                public void keyReleased(KeyEvent e) {
                    //System.out.println(e.getKeyChar()+" "+e.getKeyCode());
                    if(e.getKeyCode() == 16 || e.getKeyCode() == 17){
                        return;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_DOWN){
                        nextRow();
                        return;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_UP){
                        previousRow();
                        return;
                    }
                    if(e.isControlDown()){
                        return;
                    }
                    if(e.getKeyChar() == '\n'){
                        getSelected();
                        return;
                    }
                    filter();
                    
                }
                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
            setLayout(new GridLayout(1,1));
            //add table
            table = new MyTable(tm);
            table.setTableHeader(null);
            table.addMouseListener(new MyMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    getSelected();
                }
            });
            add(table.getGUI());
        } catch (Exception exception) {
            //MyLog.showException(exception);
        }
    }
    /**
     * @param args the command line arguments
     */
    private void filter(){
        int width = tf.getWidth();
        int height = 250;
        setPreferredSize(new Dimension(width, height));
        setLocation(tf.getLocationOnScreen().x/*+tf.getWidth()*/, tf.getLocationOnScreen().y+tf.getHeight());
        tm.filter(tf.getText().trim());
        setVisible(true);
    }
    /**
     *
     * @return
     */
    private void getSelected() {
        setVisible(false);
        int row = table.getSelectedRow();
        if(row != -1){
            useSelected(tm.getDataObject(row));
        }
    }
    
    /**
     *
     * @return
     */
    private void nextRow() {
        int row = table.getSelectedRow();
        if(row<table.getRowCount()-1){
            table.setSelectionInterval(row+1, row+1);
        }
    }
    /**
     *
     * @return
     */
    private void previousRow() {
        int row = table.getSelectedRow();
        if(row>0){
            table.setSelectionInterval(row-1, row-1);
        }
    }
    public abstract void useSelected(Object selectedObject);
}