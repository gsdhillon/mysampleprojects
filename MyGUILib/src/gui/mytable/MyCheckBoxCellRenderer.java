package gui.mytable;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 */
public class MyCheckBoxCellRenderer implements TableCellRenderer, Serializable {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        if( value instanceof JCheckBox){
            JCheckBox cb = (JCheckBox)value;
            cb.setHorizontalAlignment(SwingConstants.LEFT);
            cb.setBackground(Color.WHITE);
            return cb;
        } else {//in case it is not check box
            JLabel l = new JLabel("NotACheckBox");
            return l;
        }
    }
}
