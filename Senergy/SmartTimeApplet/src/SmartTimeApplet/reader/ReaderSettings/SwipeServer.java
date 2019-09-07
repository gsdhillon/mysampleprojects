package SmartTimeApplet.reader.ReaderSettings;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.MyTextArea;

/**
 * display swipe server status
 * @author pradnya
 */
public class SwipeServer extends MyApplet {
    MyTextArea ta = new MyTextArea();
    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            add(createButtonPane(), BorderLayout.NORTH);
            add(ta, BorderLayout.CENTER);
        } catch (Exception e) {
            MyUtils.showException("Reader Setting Applet", e);
        }
    }
    
    private void checkSwipeServerStatus() {
        try{
            MyHTTP myHTTP = MyUtils.createServletConnection("SwipeServerServlet");
            myHTTP.openOS();
            myHTTP.println("getSwipeServerStatus");
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if(result.startsWith("ERROR")){
                MyUtils.showMessage(result);
            }else{
                ta.setText(ta.getText()+"\n"+result);
            }
        }catch(Exception e){
            MyUtils.showException("Checking server status", e);
        }
    }

    private Component createButtonPane() {
        MyPanel p = new MyPanel(new GridLayout(1,0, 50, 0), "Action");
        p.add(new MyButton("Check Swipe Server Status", 2, Color.yellow) {

            @Override
            public void onClick() {
                checkSwipeServerStatus();
            }
        });
        
        p.add(new MyButton("Close", 1, Color.yellow) {

            @Override
            public void onClick() {
                closeApplet();
            }
        });
        return p;
    }
}
