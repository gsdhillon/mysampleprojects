package lib.digsign.gui_form;

import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
/**
 * show GUI for taking data of one record of detail table from the user.
 * programmer can implement the abstract methods according to the application data
 * and use it in combination with MyDetailsListPanel class
 * MyDetailPanel.java
 */
public abstract class MyDetailPanel extends MyPanel implements ActionListener, Serializable{
    private int index;
    private MyDetailsListPanel detailsListPanel;
    private MyLabel snoLabel = new MyLabel(MyLabel.TYPE_LABEL);
    private int labelWidth = 50;
    private int buttonWidth = 40;
    /**
     * 
     */
    public MyDetailPanel() {
        super(new BorderLayout(0, 4));
    }
    
    /**
     * 
     * @return
     */
    public MyPanel getHeaderPanel() {
        MyPanel p = new MyPanel(new BorderLayout());
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "S.No.");
        l.setPreferredSize(new Dimension(labelWidth, l.getPreferredSize().height));
        p.add(l, BorderLayout.WEST);
        p.add(getHeader(), BorderLayout.CENTER);
        l = new MyLabel(MyLabel.TYPE_LABEL, "  ");
        l.setPreferredSize(new Dimension(buttonWidth, l.getPreferredSize().height));
        p.add(l, BorderLayout.EAST);
        return p;
    }
    /**
     *
     * @param width
     */
    public void setGUIComponents(int width){
        //
        snoLabel.setText((index+1)+".");
        snoLabel.setPreferredSize(new Dimension(labelWidth, snoLabel.getPreferredSize().height));
        JButton b;
        try {
            URL imageURL = MyUtils.getRelativeURL("AppletImages/cross.jpg");
            ImageIcon icon = new ImageIcon(imageURL);
            b = new JButton(icon);
        } catch (Exception e) {
            b = new JButton("Remove");
        }
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setContentAreaFilled(false);
        b.setActionCommand("remove");
        b.addActionListener(this);
        b.setPreferredSize(new Dimension(buttonWidth, b.getPreferredSize().height));
        //
        add(snoLabel, BorderLayout.WEST);
        add(getGUI(), BorderLayout.CENTER);
        add(b, BorderLayout.EAST);
        //
        setPreferredSize(new Dimension(width, getPreferredSize().height));
    }
    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("remove")){
            detailsListPanel.removeDetailPanel(index);
        }
    }
    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
        snoLabel.setText((index+1)+".");
    }
    /**
     */
    public int getIndex() {
        return index;
    }
    /**
     *
     * @param dlp
     */
    public void setDetailsListPanel(MyDetailsListPanel dlp) {
        this.detailsListPanel = dlp;
    }
    /**
     * create Header panel of details and return
     * @return
     */
    public abstract MyPanel getHeader();
    /**
     * create GUIPanel of detail and return
     * @return
     */
    public abstract MyPanel getGUI();
    /**
     * create Object of detail by getting data from
     * the GUI and return
     * @return
     */
    public abstract Object getDataObject();
}