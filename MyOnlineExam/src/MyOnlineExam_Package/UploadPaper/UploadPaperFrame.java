package MyOnlineExam_Package.UploadPaper;

import MyOnlineExam_Package.MySettings;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Created on Aug 6, 2013
 * @version 1.0.0
 * @author  : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class UploadPaperFrame extends JFrame implements ActionListener{
    private JLabel label = new JLabel("", JLabel.CENTER);
    private JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JButton saveButton;
    private UploadPaperModel model = new UploadPaperModel(statusLabel);
    @SuppressWarnings("LeakingThisInConstructor")
    public UploadPaperFrame(){
        super(MySettings.APP_NAME);
        //make OuterFrame
        JPanel outerFrame = new JPanel(new BorderLayout());
        outerFrame.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 20));
        outerFrame.setBackground(Color.WHITE);
        
        //add top Label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        topPanel.setPreferredSize(new Dimension(MySettings.width, 96));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 2));
        topPanel.setBackground(Color.WHITE);
        //
        JLabel headerLabel = new JLabel(MySettings.examHeader, JLabel.CENTER);
        headerLabel.setForeground(Color.BLUE.darker());
        headerLabel.setPreferredSize(new Dimension(MySettings.width - 20, 45));
        headerLabel.setFont(new Font("MONOSPACED", Font.BOLD, 22));
        topPanel.add(headerLabel);
        //
        label.setForeground(Color.BLUE.darker());
        label.setPreferredSize(new Dimension(MySettings.width - 20, 35));
        label.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        topPanel.add(label);
        outerFrame.add(topPanel, BorderLayout.NORTH);
      
        //add middle pannel to show question answer image
        //questionAnswerPanel.setPreferredSize(new Dimension(MySettings.width, MySettings.height-108));
        questionPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 2));
        questionPanel.setBackground(Color.WHITE);
        outerFrame.add(questionPanel, BorderLayout.CENTER);
        //add bottomPanel - 2 rows
        JPanel bottomPanel = new JPanel(new GridLayout(2,1));
        bottomPanel.setPreferredSize(new Dimension(MySettings.width, 128));
        statusPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 2));
        statusPanel.setBackground(Color.WHITE);
        //prepare status label
        statusLabel.setForeground(Color.RED.darker());
        statusLabel.setPreferredSize(new Dimension(MySettings.width - 20, 35));
        statusLabel.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        statusPanel.add(statusLabel);//optionButtons()
        bottomPanel.add(statusPanel);
        bottomPanel.add(buttonPanel());
        outerFrame.add(bottomPanel, BorderLayout.SOUTH);
        
        //
        getContentPane().setLayout(new GridLayout(1,1));
        getContentPane().add(outerFrame);
        //set dimensions size and show main frame
        setSize(MySettings.width, MySettings.height);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        //
        setVisible(true);
        model.fetchAllQuestion();
        refreshGUI();
    }
//    /**
//     * 
//     * @return 
//     */
//    private JPanel optionButtons(){
//        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
//        //bp.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 2));
//        bp.setBackground(Color.WHITE);
//        //
//        bp.add(getButton("Answer is A", 160, 40));
//        bp.add(getButton("Answer is B", 160, 40));
//        bp.add(getButton("Answer is C", 160, 40));
//        bp.add(getButton("Answer is D", 160, 40));
//        return bp;
//    }
    
    private JPanel buttonPanel(){
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 10));
        bp.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 2));
        bp.setBackground(Color.CYAN.darker());
        //
        bp.add(getButton("Previous", 180, 40));
        //
        bp.add(getButton("Clear", 180, 40));
        //
        bp.add(getButton("Next", 180, 40));
        //
        saveButton = getButton("Save Paper", 180, 40);
        saveButton.setForeground(Color.BLUE.darker());
        bp.add(saveButton);
        //
        JButton b = getButton("Exit", 180, 40);
        b.setForeground(Color.RED.darker());
        bp.add(b);
        return bp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
//            case "Answer is A":
//                model.setCorrectOption("A");
//                refreshGUI();
//                break;
//            case "Answer is B":
//                model.setCorrectOption("B");
//                refreshGUI();
//                break;    
//            case "Answer is C":
//                model.setCorrectOption("C");
//                refreshGUI();
//                break;
//            case "Answer is D":
//                model.setCorrectOption("D");
//                refreshGUI();
//                break;    
            case "Previous":
                model.previousQuestion();
                refreshGUI();
                break;
             case "Clear":
                model.clearCurrentAnswer();
                refreshGUI();
                break;
            case "Next":
                model.nextQuestion();
                refreshGUI();
                break;
            case "Save Paper":
                model.saveData();
//                statusPanel.removeAll();
//                statusPanel.add(statusLabel);
//                statusPanel.validate();
//                statusPanel.repaint();
                saveButton.setEnabled(false);
                refreshGUI();
                break;
            case "Exit":
                setVisible(false);
                dispose();
                System.exit(0);
                break;
        }
    }
    
    private JButton getButton(String command, int w, int h){
        JButton b = new JButton(command);
        b.setPreferredSize(new Dimension(w, h));
        b.setActionCommand(command);
        b.setFont(new Font("MONOSPACED", Font.BOLD, 16));
        b.addActionListener(this);
        return b;
    }
    /**
     */
    private void refreshGUI() {
        //
        label.setText(model.getLabelString());
        //
        questionPanel.removeAll();
        JPanel panel = model.getMiddlePanel();
        if(panel != null){
            int top = (questionPanel.getHeight() - panel.getPreferredSize().height)/2;
            questionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, top));
            questionPanel.add(panel);
        }
        questionPanel.validate();
        questionPanel.repaint();
        //
        model.setStatusString();
    }
}