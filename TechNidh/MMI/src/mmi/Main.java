package mmi;

import gui.MyButton;
import gui.MyConstants;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import mmi.gui.MainFrame;

/**
 * @type     : Java Main Class
 * @name     : Main
 * @file     : Main.java
 * @created  : Jan 11, 2011 4:11:53 PM
 * @version  : 1.2
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean lookAndFeelSet = false;
        //***set nimbusLookAndFeel = true if needed.
       // boolean nimbusLookAndFeel = true;
        boolean nimbusLookAndFeel = true;
        //For Nimbus Look and feel
        if(nimbusLookAndFeel){
            try{
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    String name = info.getName();
                    System.out.println("Look&Feel = "+name);
                    if ("Nimbus".equals(name)) {
                        UIManager.setLookAndFeel(info.getClassName());
                        lookAndFeelSet = true;
                        System.out.println(name+" has been set");
                        break;
                    }
                }
            }catch(Exception e){
                System.out.println("Nimbus LookAndFeel Not found!");
            }
        }

        //SetDefaultLookAndFeelDecorated
        if(!lookAndFeelSet){
            try{
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                System.out.println("SetDefaultLookAndFeelDecorated is done.");
                lookAndFeelSet = true;
                MyConstants.BG_COLOR_INNER = new Color(204,227,249);
                MyConstants.BG_COLOR_OUTER = new Color(184,207,229);
                MyButton.setInset(1, 2, 1, 2);
            }catch(Exception e){
                System.out.println("Failed to SetDefaultLookAndFeelDecorated!");
            }
        }

        //set SystemLookAndFeelClassName
        if(!lookAndFeelSet){
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("SystemLookAndFeelClassName has been set.");
            }catch(Exception e){
                System.out.println("Failed to set SystemLookAndFeelClassName!");
            }
        }

        //notice the use of invokeLater() method
        //from event-dispatching thread
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
     }
}

