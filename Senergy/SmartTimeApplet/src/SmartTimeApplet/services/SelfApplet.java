/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services;

import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import lib.gui.MyPanel;

/**
 *
 * @author pradnya
 */
public class SelfApplet extends MyApplet {

    public Container contentPane;
    private MyPanel homePanel;

    @Override
    public void init() {
        try {
            super.init();
            String empCode = "";
            empCode = getParameter("userID");
            LoadPanel(new MonthlyMuster("AttendanceReport", 0));
        } catch (Exception e) {
            MyUtils.showException("SelfApplet", e);
        }
    }

    public void LoadPanel(MyPanel LoadPanel) throws Exception {
        try {
            contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            homePanel = CreateHomePanel(LoadPanel);
            contentPane.add(homePanel, BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("LoadPanelMusterReport", e);
        }

    }

    public MyPanel CreateHomePanel(MyPanel LoadPanel) throws Exception {
        MyPanel AR = new MyPanel();
        AR.setLayout(new BorderLayout());
        AR.add(LoadPanel, BorderLayout.CENTER);
        return AR;
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        contentPane.validate();
        contentPane.repaint();
    }
}
