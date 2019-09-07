package mytable.cellrenderers;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyCheckBoxCellRenderer extends MyDataCellRenderer{
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
        if( value instanceof Boolean){
            JCheckBox cb = new JCheckBox();
            cb.setOpaque(true);
            cb.setSelected(((Boolean)value).booleanValue());
            if(table.isRowSelected(row)){
                cb.setForeground(table.getSelectionForeground());
                cb.setBackground(table.getSelectionBackground());
            }else{
                cb.setForeground(table.getForeground());
                cb.setBackground(row%2==0?bgEven:bgOdd);//table.getBackground()
            }
            return cb;
        } else {//in case it is not check box
            return getComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
