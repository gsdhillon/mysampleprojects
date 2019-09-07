package mmi.gui;

import mmi.data.MyData;
import gui.MyPanel;
import java.awt.GridLayout;
import gui.MyConstants;
import javax.swing.JTabbedPane;
import mmi.Channels.ChannelsTab;
import mmi.Home.HomeTab;


/**
 * @type     : Java Class
 * @name     : AllTabsPanel
 * @file     : AllTabsPanel.java
 * @created  : Feb 6, 2011 11:43:39 AM
 * @version  : 1.0.0
 */
public class AllTabsPanel extends MyPanel{
    /**
     * Constructor
     */
    public AllTabsPanel(){
        super(new GridLayout(1,1));
        setDefaultBG();
        JTabbedPane tp = new JTabbedPane();
        tp.setFont(MyConstants.FONT_TABS);
        tp.setForeground(MyConstants.FG_TABS);
        tp.addTab("   Home   ", new HomeTab());
        tp.addTab("   Channels   ", new ChannelsTab());
        add(tp);
        MyData.openDefaultPort();
    }
}