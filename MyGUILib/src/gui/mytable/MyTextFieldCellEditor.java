package gui.mytable;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import gui.mycomponents.MyTextField;

/**
 * Class MyCheckBoxCellEditor
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyTextFieldCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable {
    private MyTextField tf;
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        tf = new MyTextField((String)value);
        return tf;
    }
    @Override
    public Object getCellEditorValue() {
        return tf.getText();
    }
}