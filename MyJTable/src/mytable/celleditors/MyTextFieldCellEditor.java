package mytable.celleditors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
/**
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyTextFieldCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable {
    private JTextField tf;
    @Override
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, final int col) {
        tf = new JTextField((String)value);
        tf.setBackground(Color.YELLOW);
        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                table.setValueAt(tf.getText(), row, col);
            }
            @Override
            public void keyPressed(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        return tf;
    }
    @Override
    public Object getCellEditorValue() {
        return tf.getText();
    }
}