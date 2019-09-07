package gui.mytable;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Class MyCheckBoxCellEditor
 * Created on Jun 30, 2012
 * @version 1.0.0
 * @author
 */
public class MyCheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    //private boolean selected = false;
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        if (value instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) value;
            //selected = cb.isSelected();
            //cb.setBackground(Color.WHITE);
            return cb;
        }else{
            return new JLabel("NoCheckBox");
        }
        
    }
    @Override
    public Object getCellEditorValue() {
        //return selected;
        return new Object();
    }
}