package lib.gui;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableModel;
import lib.utils.MyLog;
/**
 */
public abstract class MySearchPopup extends JPopupMenu{
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
                    if(e.isControlDown()){
                        return;
                    }
                    if(e.getKeyChar() == '\n'){
                        getSelected();
                    }else{
                        filter();
                    }
                }
                @Override
                public void keyTyped(KeyEvent e) {
                }
            });
            setLayout(new GridLayout(1,1));
            //add table
            table = new MyTable(tm);
            table.addMouseListener(new MyMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    getSelected();
                }
            });
            add(table.getGUI());
            int width = 150;
            int height = 250;
            setPreferredSize(new Dimension(width, height));
        } catch (Exception exception) {
            MyLog.showException(exception);
        }
    }
    /**
     * @param args the command line arguments
     */
    private void filter(){
        setLocation(tf.getLocationOnScreen().x+tf.getWidth(), tf.getLocationOnScreen().y);
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
    public abstract void useSelected(Object selectedObject);
}