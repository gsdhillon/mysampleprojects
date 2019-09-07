package mytable.celleditors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
/**
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor, Serializable {
    private JComboBox<String> cb;
    private String[] values;
    public MyComboBoxCellEditor(String[] values){
        this.values = values;
    }
    @Override
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, final int col) {
        cb = new JComboBox<>(values);
        cb.setBackground(Color.YELLOW);
        cb.setSelectedItem((String)value);
        cb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                table.setValueAt(cb.getSelectedItem(), row, col);
            }
        });
        return cb;
    }
    @Override
    public Object getCellEditorValue() {
        return cb.getSelectedItem();
    }
}