package gui.mytreeview;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * Class MyTree Created on Aug 17, 2013
 *
 * @version 1.0.0
 * @author
 */
public class MyTree extends JPanel {

    private JTree tree = null;
    private DefaultMutableTreeNode root;
    private JScrollPane treeScrollableView;

    public MyTree() {
        try {
            setLayout(new BorderLayout());
            root = new DefaultMutableTreeNode("All Employee's under you", true);//makeNode();
            tree = new JTree(root);
            TreeCellRenderer renderer = new MyTreeCellRenderer();
            tree.setCellRenderer(renderer);
            tree.setRowHeight(0);
            treeScrollableView = new JScrollPane(tree);
            add(treeScrollableView, BorderLayout.CENTER);
            tree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    if(SwingUtilities.isRightMouseButton(me)){
                        onMouseRightClicked(me);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMouseRightClicked(MouseEvent me) {
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        if (tp == null) {
            return;
        }
        tree.setSelectionPath(tp);
        Object value = tp.getLastPathComponent();
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            
            Object userObject = node.getUserObject();
            if (userObject instanceof Employee) {
                Employee emp = (Employee) userObject;
                emp.mouseRightClicked(tree);
            }
        }
    }

    public void populate() {
        DefaultMutableTreeNode n = makeNode("Raj Singh");
        DefaultMutableTreeNode n0 = makeNode("Ram Rakhi Kaur");
        DefaultMutableTreeNode n2 = makeNode("Gurmeet Singh");
        DefaultMutableTreeNode n3 = makeNode("Hardeep Singh");
        DefaultMutableTreeNode n1 = makeNode("Ardas Kaur");
        DefaultMutableTreeNode n4 = makeNode("Ishjyot Kaur");
        DefaultMutableTreeNode n5 = makeNode("Ajaybir Singh");
        DefaultMutableTreeNode n6 = makeNode("Akashdeep Singh");
        root.add(n);
        n.add(n0);
        n.add(n2);
        n.add(n3);
        n2.add(n4);
        n2.add(n1);
        n2.add(n5);
        n3.add(n6);
        n3.add(n6);
        tree.expandPath(new TreePath(root.getPath()));
        tree.updateUI();
        tree.repaint();

    }
    private int count = 0;

    public void addNode() {
        Employee emp = new Employee((++count) + " New Node");
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(emp, true);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            showMsg("No selection");
            return;
        }
        int c1 = node.getChildCount();
        node.insert(newNode, node.getChildCount());
        int c2 = node.getChildCount();
        if (c1 == c2) {
            showMsg("Could not add");
        }
        TreePath path = new TreePath(newNode.getPath());
        tree.expandPath(path);
        tree.scrollPathToVisible(path);
        tree.setSelectionPath(path.getParentPath());
        tree.updateUI();
        tree.repaint();
        treeScrollableView.validate();
//        DefaultMutableTreeNode n2 = (DefaultMutableTreeNode) node.getLastChild();
        // showMsg(n2.toString());
    }

    public void removeNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            showMsg("No selection");
            return;
        }
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        if (parent == null) {
            return;
        }
        Object userObject = node.getUserObject();
        if (!(userObject instanceof Employee)) {
            return;
        }
        Employee emp = (Employee) userObject;
        int option = JOptionPane.showConfirmDialog(new JFrame(), "Remove " + emp.toString(), "", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        parent.remove(node);
//        Enumeration<TreeNode> allChilds = node.children();
//        while(allChilds.hasMoreElements()){
//            DefaultMutableTreeNode c = (DefaultMutableTreeNode)allChilds.nextElement();
//            parent.add(c);
//        }
        int numChildren = node.getChildCount();
//        DefaultMutableTreeNode[] allChilds = new DefaultMutableTreeNode[node.getChildCount()];
        for (int i = 0; i < numChildren; i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getFirstChild();
            //showInfo(child);
            parent.add(child);
        }
        TreePath path = new TreePath(parent.getPath());
        tree.setSelectionPath(path);
        tree.updateUI();
        tree.repaint();
//        DefaultMutableTreeNode n2 = (DefaultMutableTreeNode) node.getLastChild();
        // showMsg(n2.toString());
    }

    private void showInfo(DefaultMutableTreeNode node) {
        Object userObject = node.getUserObject();
        if (!(userObject instanceof Employee)) {
            System.out.println("Not Employee");
        } else {
            Employee emp = (Employee) userObject;
            System.out.println("++++" + emp.toString());
            JOptionPane.showMessageDialog(null, "++++" + emp.toString());
        }
    } 

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(tree, msg);
    }
    private DefaultMutableTreeNode makeNode(String name) {
        Employee emp = new Employee(name);
        return new DefaultMutableTreeNode(emp, true);
    }
}