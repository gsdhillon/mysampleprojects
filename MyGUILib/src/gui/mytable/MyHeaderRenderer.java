package gui.mytable;

import java.awt.*;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 */
public class MyHeaderRenderer implements TableCellRenderer, Serializable  {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.YELLOW.brighter().brighter());
        p.setPreferredSize(new Dimension(50, 20));
        p.setBorder(BorderFactory.createEtchedBorder());
        JLabel l = new JLabel((String) value, JLabel.LEFT);
        l.setForeground(Color.RED);
        l.setFont(new Font("MONOSPACED", Font.PLAIN, 11));
        p.add(l, BorderLayout.CENTER);
        return p;
    }
}