package mytable.cellrenderers;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyDataCellRenderer implements TableCellRenderer, Serializable {
    public static final Color bgOdd = new Color(243,236,255);
    public static final Color bgEven = new Color(240,226,219);
    /**
     * 
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getComponent(table, value, isSelected, hasFocus, row, column);
    }
    /**
     * 
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return 
     */
    protected Component getComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel l = new JLabel(value.toString());
        l.setOpaque(true);
        l.setFont(table.getFont());
        if(table.isRowSelected(row)){
            l.setForeground(table.getSelectionForeground());
            l.setBackground(table.getSelectionBackground());
        }else{
            l.setForeground(table.getForeground());
            l.setBackground(row%2==0?bgEven:bgOdd);//table.getBackground()
        }
        return l;
    }
}
