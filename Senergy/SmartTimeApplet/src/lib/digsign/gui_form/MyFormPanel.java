package lib.digsign.gui_form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import lib.gui.MyPanel;
import lib.session.MyApplet;
/**
 * @type     : Java Class
 * @name     : MyFormPanel
 * @file     : MyFormPanel.java
 * @created  : Aug 11, 2010 1:25:56 PM
 * @author   : Gurmeet Singh, Computer Division, BARC, Mumbai.
 */
public class MyFormPanel extends JScrollPane  implements Serializable{
    private static int hGap = 5;
    private static int vGap = 5;
    public int sp_width;
    private MyPanel formPanel;
    private MyPanel[] panels = new MyPanel[50];
    private int numPanels = 0;
    /**
     * Constructor
     */
    public MyFormPanel(MyApplet myAppplet, String name) {
        super();
        this.sp_width = myAppplet.getSize().width - 50 - 2*hGap;
      //  MyUtils.showMessage(sp_width+"  sp_width");
        if(name != null){
            setBorder(BorderFactory.createTitledBorder(name));
        }else{
            setBorder(BorderFactory.createTitledBorder("Form"));
        }
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
        formPanel.setDefaultBG();
        setViewportView(formPanel);
    }
    /**
     *
     * @param p
     */
    public void addMyPanel(MyPanel p){
        p.setPreferredSize(new Dimension(sp_width, p.getPreferredSize().height));
        formPanel.add(p);
        panels[numPanels++] = p;
        validateForm();
    }
    /**
     *
     */
    public void validateForm(){
        int height = 0;
        for(int i=0;i<numPanels;i++){
            height += panels[i].getPreferredSize().height+vGap;
        }
        formPanel.setPreferredSize(new Dimension(sp_width, height));
        formPanel.validate();
        getViewport().validate();
        validate();
        repaint();
    }
}