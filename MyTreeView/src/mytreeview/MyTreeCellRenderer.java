package mytreeview;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * Class MyTreeCellRenderer
 * Created on Aug 17, 2013
 * @version 1.0.0
 * @author
 */
public class MyTreeCellRenderer  implements TreeCellRenderer{
    private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
     @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component returnValue = null;
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            if (userObject instanceof Employee) {
                Employee emp = (Employee) userObject;
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
                p.add(emp.getGUIPanel(selected, tree, node.getLevel()));
                p.setBackground(tree.getBackground());
                returnValue = p;
            }
        }
        if (returnValue == null) {
            returnValue = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
        return returnValue;
    }
}