package mytreeview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
/**
 * Class Employee
 * Created on Aug 17, 2013
 * @version 1.0.0
 * @author
 */
public final class Employee{
    public String empno = "24196";
    private String name = "";
    public String desig = "SO/E";
    public String division = "Computer Division";
    public String email = "gsdhill@gmail.com";
    public String phone = "22656";
    public String address = "604, Akashdeep, Anushakti Nagar, Mumbai - 400094.";
    //GUI Panel
    private final int WIDTH = 820;
    private final int HEIGHT_COLLAPSED = 60;
    private final int HEIGHT_EXPANDED = 90;
    private JPanel guiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
    private JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
    private JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
    private boolean expanded = false;
    @SuppressWarnings("LeakingThisInConstructor")
    public Employee(String name){
        this.name = name;
        setInfoPanel();
        setAddressPanel();
        //
        guiPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        guiPanel.setPreferredSize(new Dimension(WIDTH+2, HEIGHT_COLLAPSED+2));
        guiPanel.add(infoPanel);
    }
    private void expand(JTree tree) {
        expanded = true;
        guiPanel.removeAll();
        guiPanel.setPreferredSize(new Dimension(WIDTH+2, HEIGHT_EXPANDED+2));
        guiPanel.add(infoPanel);
        guiPanel.add(addressPanel);
        guiPanel.validate();
        tree.updateUI();
        tree.repaint();
    }
    private void collapse(JTree tree) {
        expanded = false;
        guiPanel.removeAll();
        guiPanel.setPreferredSize(new Dimension(WIDTH+2, HEIGHT_COLLAPSED+2));
        guiPanel.add(infoPanel);
        guiPanel.validate();
        tree.updateUI();
        tree.repaint();
    }
    @Override
    public String toString(){
        return name+", "+desig+", "+division;
    }
    private JLabel getLabel(String text){
        JLabel l = new JLabel(text);
        return l;
    }
    private JLabel lh1, lh2,lh3,lh4,lh5,lh6, lh7;
    private JLabel l1,  l2, l3, l4, l5, l6,  l7;
    private void setForegroundColors(Color color){
        lh1.setForeground(color);
        lh2.setForeground(color);
        lh3.setForeground(color);
        lh4.setForeground(color);
        lh5.setForeground(color);
        lh6.setForeground(color);
        lh7.setForeground(color);
        //
        l1.setForeground(color);
        l2.setForeground(color);
        l3.setForeground(color);
        l4.setForeground(color);
        l5.setForeground(color);
        l6.setForeground(color);
        l7.setForeground(color);
    }
    /**
     * 
     * @param selected
     * @param tree
     * @param level
     * @return 
     */
    public JPanel getGUIPanel(boolean selected, JTree tree, int level){
        guiPanel.setEnabled(tree.isEnabled());
        if (selected) {
            guiPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 1));
            setForegroundColors(Color.WHITE);
            infoPanel.setBackground(Color.BLUE.darker());
            addressPanel.setBackground(Color.BLUE.darker());
        } else {
            guiPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setForegroundColors(Color.BLACK);
            infoPanel.setBackground(Color.WHITE);
            addressPanel.setBackground(Color.WHITE);
        }
        return guiPanel;
    }
    public void mouseRightClicked(JTree tree) {
        if(!expanded){
            expand(tree);
        }else{
            collapse(tree);
        }
    }
    private Font fontH = new Font("Times", Font.BOLD, 13);
    private Font fontD = new Font("Times", Font.PLAIN, 13);
    private void setAddressPanel(){
        //
        addressPanel.setPreferredSize(new Dimension(WIDTH, 30));
        lh7 = getLabel("Address: ");
        lh7.setPreferredSize(new Dimension(120, 30));
        lh7.setFont(fontH);
        addressPanel.add(lh7);
        //
        l7 = getLabel(address);
        l7.setPreferredSize(new Dimension(700, 30));
        l7.setFont(fontD);
        addressPanel.add(l7);
    }
    /**
     * 
     */
    private void setInfoPanel() {
        infoPanel.setPreferredSize(new Dimension(WIDTH, 60));
        //Header
        lh1 = getLabel("Name");
        lh1.setPreferredSize(new Dimension(200, 30));
        lh1.setFont(fontH);
        infoPanel.add(lh1);
        //
        lh2 = getLabel("EmpNo");
        lh2.setPreferredSize(new Dimension(100, 30));
        lh2.setFont(fontH);
        infoPanel.add(lh2);
        //
        lh3 = getLabel("Desig");
        lh3.setPreferredSize(new Dimension(80, 30));
        lh3.setFont(fontH);
        infoPanel.add(lh3);
        //
        lh4 = getLabel("Division");
        lh4.setPreferredSize(new Dimension(140, 30));
        lh4.setFont(fontH);
        infoPanel.add(lh4);
        //
        lh5 = getLabel("EMail");
        lh5.setPreferredSize(new Dimension(160, 30));
        lh5.setFont(fontH);
        infoPanel.add(lh5);
        //
        lh6 = getLabel("Phone");
        lh6.setPreferredSize(new Dimension(80, 30));
        lh6.setFont(fontH);
        infoPanel.add(lh6);
        //DATA
        l1 = getLabel(name);
        l1.setPreferredSize(new Dimension(200, 30));
        l1.setFont(fontD);
        infoPanel.add(l1);
        //
        l2 = getLabel(empno);
        l2.setPreferredSize(new Dimension(100, 30));
        l2.setFont(fontD);
        infoPanel.add(l2);
        //
        l3 = getLabel(desig);
        l3.setPreferredSize(new Dimension(80, 30));
        l3.setFont(fontD);
        infoPanel.add(l3);
        //
        l4 = getLabel(division);
        l4.setPreferredSize(new Dimension(140, 30));
        l4.setFont(fontD);
        infoPanel.add(l4);
        //
        l5 = getLabel(email);
        l5.setPreferredSize(new Dimension(160, 30));
        l5.setFont(fontD);
        infoPanel.add(l5);
        //
        l6 = getLabel(phone);
        l6.setPreferredSize(new Dimension(80, 30));
        l6.setFont(fontD);
        infoPanel.add(l6);
    }
}