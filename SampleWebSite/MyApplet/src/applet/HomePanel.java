package applet;

import java.awt.FlowLayout;
import lib.gui.MyPanel;
/**
 */
public class HomePanel extends MyPanel{
    private HomePage page;
    /**
     */
    public HomePanel(HomePage page){
        this.page = page;
        setLayout(new FlowLayout(FlowLayout.CENTER,0,20));
      //  add(buttonPane());
        
    }
    private void showButtons(){
    }
    private void showEmployeeMuster(){
    }
    private void showEmployeeReport(){
    }
    private void doLogout() {
        page.doLogout();
    }
}