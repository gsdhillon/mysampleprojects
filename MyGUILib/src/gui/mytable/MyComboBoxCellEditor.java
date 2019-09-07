package gui.mytable;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
/**
 * Class MyCheckBoxCellEditor
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable {
    JComboBox<String> cb;
    public MyComboBoxCellEditor(String[] values){
        cb = new JComboBox<>(values);
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        cb.setSelectedItem((String)value);
        return cb;
    }
    @Override
    public Object getCellEditorValue() {
        return cb.getSelectedItem();
    }
}