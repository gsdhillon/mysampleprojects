package applet;

import java.awt.Color;
import java.awt.FlowLayout;
import lib.gui.MyPanel;
/**
 */
public class HomePage extends MyPanel{
    private MainApplet applet;
    /**
     */
    public HomePage(MainApplet applet){
        this.applet = applet;
        setBackground(new Color(104,55,157));
        try{
            MyPanel homePanel = new HomePanel(this);
            int topMargin = (int)((applet.MAIN_PANEL_HEIGHT-homePanel.getPreferredSize().getHeight())/2);
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, topMargin));
            add(homePanel);
        }catch(Exception e){
        }
    }
    
    public void doLogout() {
        applet.showLoginPanel();
    }
    private void showButtons(){
    }
    private void showEmployeeMuster(){
    }
    private void showEmployeeReport(){
    }
    
}