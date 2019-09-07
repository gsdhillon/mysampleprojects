package mytable.cellrenderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyRowNumberRenderer extends MyDataCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel l = new JLabel((String)value);
        l.setOpaque(true);
        if(table.isRowSelected(row)){
            l.setForeground(table.getSelectionForeground());
            l.setBackground(table.getSelectionBackground());
        }else{
            l.setForeground(Color.WHITE);
            l.setBackground(bgEven);
        }
        l.setFont(new Font("Calibri", Font.BOLD, 13));
        return l;
    }
}
