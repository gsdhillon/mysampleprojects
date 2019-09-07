package applet;

import applet.session.MyApplet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import lib.gui.MyBGLabel;
import lib.gui.MyPanel;
import lib.gui.MyContainer;

/**
 * Class MainApplet
 * Created on Sep 7, 2013
 * @version 1.0.0
 * @author
 */
public class MainApplet  extends MyApplet{
    MyPanel topPanel = new MyPanel(new BorderLayout(), null);
    MyPanel leftPanel = new MyPanel(new GridLayout(1,1), null);
    MyContainer mainPanel = new MyContainer();
    public static int MAIN_PANEL_HEIGHT;
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void init() {
        try{
            super.init();
            MAIN_PANEL_HEIGHT = getBounds().height;
            if(MAIN_PANEL_HEIGHT<500){
                MAIN_PANEL_HEIGHT = 500;
            }
            setLayout(new BorderLayout());
            topPanel.setPreferredSize(new Dimension(getWidth(), 60));
            leftPanel.setPreferredSize(new Dimension(120, getHeight()-60));
            setHeader();
            add(leftPanel, BorderLayout.WEST);
            add(topPanel, BorderLayout.NORTH);
            add(mainPanel, BorderLayout.CENTER);
            showLoginPanel();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void showLoginPanel() {
        mainPanel.addMyPanel(new LoginPage(this));
    }
    public void showHomePanel() {
        mainPanel.addMyPanel(new HomePage(this));
    }
    /**
     * 
     */
    private void setHeader() {
        topPanel.add(new MyBGLabel("Chitragupt ID Card Issuance System", new Font(Font.SANS_SERIF, Font.BOLD, 28), 100, 45, true), BorderLayout.CENTER);
        MyBGLabel l = new MyBGLabel("Gurmeet Singh",  new Font(Font.SANS_SERIF, Font.BOLD, 14), 0, 45, false);
        l.setPreferredSize(new Dimension(200, 60));
        topPanel.add(l, BorderLayout.EAST);
        leftPanel.add(new MyBGLabel());
    }
}