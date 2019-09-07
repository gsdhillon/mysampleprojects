package SmartTimeApplet.visitor.vis_app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import javax.swing.JCheckBox;
import lib.gui.MyLabel;
import lib.gui.MyPanel;

/**
 * Company.java
 */
public class Company implements Serializable {
    public JCheckBox checkBox = new JCheckBox();
    public String companyID;
    public String companyName;
    public String address;
    public String type;
    public String city;
    public String state;
    public String pin;
    public String phone1;
    public String phone2;
    public String phone3;
    public String status;
    
    public MyPanel getGUIPanel(String title){
        MyPanel p = new MyPanel(new GridLayout(3, 1), title);
        int labelWidth = 100;
        
        MyPanel p1 = new MyPanel(new GridLayout(1,4));
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "CompanyID:");
        Dimension labelDimension = new Dimension(labelWidth, l.getPreferredSize().height);
        l.setPreferredSize(labelDimension);
        p1.add(l);
        p1.add(new MyLabel(MyLabel.TYPE_DATA, companyID));
        l = new MyLabel(MyLabel.TYPE_LABEL, "Name:");
        l.setPreferredSize(labelDimension);
        p1.add(l);
        p1.add(new MyLabel(MyLabel.TYPE_DATA, companyName));
        p.add(p1);
        
        MyPanel p2 = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Address:");
        l.setPreferredSize(labelDimension);
        p1.add(l, BorderLayout.WEST);
        p2.add(new MyLabel(MyLabel.TYPE_DATA, address), BorderLayout.CENTER);
        p.add(p2);
        
        MyPanel p3 = new MyPanel(new GridLayout(1,4));
        l = new MyLabel(MyLabel.TYPE_LABEL, "City:");
        l.setPreferredSize(labelDimension);
        p1.add(l);
        p3.add(new MyLabel(MyLabel.TYPE_DATA, city));
        l = new MyLabel(MyLabel.TYPE_LABEL, "State:");
        l.setPreferredSize(labelDimension);
        p1.add(l);
        p3.add(new MyLabel(MyLabel.TYPE_DATA, state));
        p.add(p3);
        return p;
    }
}