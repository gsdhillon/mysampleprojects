package mytable.cellrenderers;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import mytable.MyTable;

/**
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyHeaderRenderer extends MyDataCellRenderer {
    public MyHeaderRenderer(){
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel p = new JPanel(new BorderLayout());
       // p.add(searchText);
        p.setBackground(bgEven);
        p.setPreferredSize(new Dimension(50, 22));
        p.setBorder(BorderFactory.createLineBorder(Color.gray));
        String text = (String) value;
        String name = text!=null?text.substring(0, text.length()-1):"";
        String sorting = text!=null?text.substring(text.length()-1, text.length()):"";
        JLabel l = new JLabel(name, JLabel.LEFT);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Calibri", Font.BOLD, 13));
        p.add(l, BorderLayout.CENTER);
        if(column>0){
            l = new JLabel(sorting, JLabel.RIGHT);
            l.setForeground(Color.WHITE);
            l.setFont(l.getFont().deriveFont(Font.BOLD, 13));
            p.add(l, BorderLayout.EAST); 
        }
        
        return p;
    }
}