package lib.digsign.gui_form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import lib.gui.MyPanel;
/**
 * show GUI for taking data of multiple records of detail table from the user.
 * programmer use it in combination with MyDetailsPanel class
 * MyDetailsListPanel.java
 */
public abstract class MyDetailsListPanel extends MyPanel implements ActionListener,  Serializable{
    public static int hGap = 5;
    public static int vGap = 5;
    private int initialHeight;
    private MyDetailPanel[] detailsPanel = new MyDetailPanel[50];
    private int numDetailPanels = 0;
    private MyPanel dpList;
    private MyFormPanel formPanel;
    /**
     * Constructor
     */
    public MyDetailsListPanel(MyFormPanel formPanel,  MyDetailPanel dp, String name) {
        super(new BorderLayout(hGap, vGap));
        this.formPanel = formPanel;
        if(name != null){
            setBorder(BorderFactory.createTitledBorder(name));
        }
        dpList = new MyPanel(new GridLayout(0, 1, hGap, vGap));
        dpList.add(dp.getHeaderPanel());
        add(dpList, BorderLayout.CENTER);
        //AddButton
        MyPanel buttonPanel = new MyPanel(new BorderLayout());
        JButton b = new JButton("Add");
        b.addActionListener(this);
        buttonPanel.add(b, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        //add first dp
        addDetailPanel(dp);
        initialHeight = getPreferredSize().height;
        setPreferredSize(new Dimension(formPanel.sp_width - 2*hGap, initialHeight));
    }
    /**
     *
     * @param p
     */
    public void removeDetailPanel(int index){
        if(numDetailPanels <= 1){
            return;
        }
        //remove entry pointed by index and shift up others
        dpList.remove(detailsPanel[index]);
        for (int i = index; i < (numDetailPanels - 1); i++) {
            detailsPanel[i] = detailsPanel[i + 1];
            detailsPanel[i].setIndex(i);
        }
        numDetailPanels--;
        /*dpList.removeAll();
        for (int i = index; i < numDetailPanels; i++) {
            dpList.add(detailsPanel[i]);
        }*/
        validateDetailList();
    }
    /**
     *
     */
    private void validateDetailList(){
        int height = initialHeight;
        for(int i=1; i<numDetailPanels;i++){
            height += detailsPanel[i].getPreferredSize().height + vGap;
        }
        dpList.validate();
        setPreferredSize(new Dimension(formPanel.sp_width - 2*hGap, height));
        validate();
        repaint();
        formPanel.validateForm();
    }
    /**
     *
     * @param detailPanel
     */
    private void addDetailPanel(MyDetailPanel dp){
        dp.setDetailsListPanel(this);
        dp.setIndex(numDetailPanels);
        dp.setGUIComponents(formPanel.sp_width - 2*hGap);
        dpList.add(dp);
        detailsPanel[numDetailPanels++] = dp;
    }
    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        MyDetailPanel dp = getNewMyDetailPanel();
        addDetailPanel(dp);
        validateDetailList();
    }
    /**
     * 
     * @return
     */
    public MyDetailPanel[] getAllDetails(){
        MyDetailPanel[] details = new MyDetailPanel[numDetailPanels];
        System.arraycopy(detailsPanel, 0, details, 0, numDetailPanels);
        return details;
    }
    /**
     *
     * @return
     */
    protected abstract MyDetailPanel getNewMyDetailPanel();
}