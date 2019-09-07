package mytreeview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
/**
 * Class MainFrame
 * Created on Aug 15, 2013
 * @version 1.0.0
 * @author
 */
public class MainFrame extends JFrame{
    private final String TOP = BorderLayout.NORTH;
    private final String CENTER = BorderLayout.CENTER;
    private static MainFrame mainFrame = null;
    MyTree tree = new MyTree();
    public static void createAndShow() throws Exception{
        if(mainFrame != null){
            throw new Exception("MainFrame_ALREADY_OPENED");
        }
        try{
            boolean set = false;
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                System.out.println("LookAndFeel Name = '"+info.getName()+"'");
//            }
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                String name = info.getName();
//                if ("Nimbus".equals(name)) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    System.out.println(name+" has been set");
//                    set = true;
//                    break;
//                }
//            }
            if(!set){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }catch(Exception e){
            System.out.println("Nimbus LookAndFeel Not found!");
        }
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainFrame.exit();
                    }
                });
            }
        });
    }
    private MainFrame(){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(d.width, d.height-30));
        setLayout(new BorderLayout());
        add(bp(), TOP);
        add(tree, CENTER);
        tree.populate();
    }
    private JPanel bp() {
       JPanel p = new JPanel(new GridLayout(1,4));
       JButton b = new JButton("Add Node");
       b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNode();
            }
       });
       p.add(b);
       b = new JButton("Remove Node");
       b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeNode();
            }
        });
       p.add(b);
       b = new JButton("Exit");
       b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
       p.add(b);
       return p;
    }
    private void addNode(){
        tree.addNode();
    }
    private void removeNode(){
        tree.removeNode();
    }
    private void exit(){
        setVisible(false);
        dispose();
        mainFrame = null;
        System.exit(0);
    }
}