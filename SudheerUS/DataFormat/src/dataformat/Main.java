package dataformat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


/**
 *
 * @author gsdhillon@gmail.com
 */
public class Main extends JFrame{
    public Main(){
        super("DataFormat");
        //
        JPanel view = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        int width = 800;
        int height = 20;
        
        //1 - 03/07/2019
        JPanel p = new DoseIRL4ParamPanel(); 
        height+=p.getPreferredSize().height+20;
        view.add(p);
        //2  - 04/07/2019
        p = new FluenceIRL12ParamPanel();
        height += p.getPreferredSize().height+20;
        view.add(p);
        //3 - 03/08/2019
        p = new DoseIZIX4ParamPanel();
        height+=p.getPreferredSize().height+20;
        view.add(p);
        //4 - 04/08/2019
        p = new FluenceIZIX12ParamPanel();
        height+=p.getPreferredSize().height+20;
        view.add(p);
               
        
        view.setPreferredSize(new Dimension(width, height));
        //
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(view);
        add(sp, BorderLayout.CENTER);
        //
        JPanel actionsPanel = createActionsPanel();
        add(actionsPanel, BorderLayout.SOUTH);
        //
        setSize(920, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel createActionsPanel() {
        //
        JPanel actionsPanel = new JPanel(new BorderLayout());
        actionsPanel.setPreferredSize(new Dimension(700, 35));
        JButton b = new JButton("Exit Program");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        actionsPanel.add(b, BorderLayout.EAST);
        return actionsPanel;
    }
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
