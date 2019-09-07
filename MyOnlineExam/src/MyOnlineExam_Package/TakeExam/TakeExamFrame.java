package MyOnlineExam_Package.TakeExam;

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
public class TakeExamFrame extends JFrame implements ActionListener{
    private JLabel label = new JLabel("", JLabel.CENTER);
    private JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private MyWhiteBoardPanel questionPanel = new MyWhiteBoardPanel();//new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JButton submitButton;
    private TakeExamModel model;
    @SuppressWarnings("LeakingThisInConstructor")
    public TakeExamFrame(){
        super(MySettings.APP_NAME);
        //make OuterFrame
        JPanel outerFrame = new JPanel(new BorderLayout());
        outerFrame.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 20));
        outerFrame.setBackground(Color.WHITE);
        
        //add top Label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
        topPanel.setPreferredSize(new Dimension(MySettings.width, 96));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
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
        questionPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        questionPanel.setBackground(Color.WHITE);
       
        outerFrame.add(questionPanel, BorderLayout.CENTER);
        //add bottomPanel - 2 rows
        JPanel bottomPanel = new JPanel(new GridLayout(2,1));
        bottomPanel.setPreferredSize(new Dimension(MySettings.width, 128));
        statusPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        statusPanel.setBackground(Color.WHITE);
        //prepare status label
        statusLabel.setForeground(Color.RED.darker());
        statusLabel.setPreferredSize(new Dimension(MySettings.width - 20, 35));
        statusLabel.setFont(new Font("MONOSPACED", Font.BOLD, 18));
        statusPanel.add(statusLabel);
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
        model = new TakeExamModel(statusLabel);
        model.fetchAllQuestion();
        refreshGUI();
    }

    
    
    
    private JPanel buttonPanel(){
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 10));
        bp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        bp.setBackground(Color.ORANGE);
        //
        bp.add(getButton("Previous", 130, 40));
        //
        bp.add(getButton("Clear Answer", 160, 40));
        //
        bp.add(getButton("Next", 130, 40));
        //
        submitButton = getButton("Submit", 180, 40);
        submitButton.setForeground(Color.BLUE.darker());
        bp.add(submitButton);
        //
        bp.add(getButton("Rub", 80, 40));
        //
        JButton b = getButton("Exit", 120, 40);
        b.setForeground(Color.RED.darker());
        bp.add(b);
        return bp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Clear Answer":
                model.clearCurrentAnswer();
                refreshGUI();
                break;
            case "Previous":
                model.previousQuestion();
                refreshGUI();
                break;
            case "Next":
                model.nextQuestion();
                refreshGUI();
                break;
            case "Submit":
                model.submitPaper();
                submitButton.setText("Show Questions");
                submitButton.setActionCommand("Show Questions");
                refreshGUI();
                break;
            case "Rub":
                questionPanel.rub();
                break;
            case "Show Questions":
                model.setShowResult(false);
                submitButton.setText("Show Result");
                submitButton.setActionCommand("Show Result");
                refreshGUI();
                break;
            case "Show Result":
                model.setShowResult(true);
                submitButton.setText("Show Questions");
                submitButton.setActionCommand("Show Questions");
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
        b.setFont(new Font("MONOSPACED", Font.BOLD, 14));
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
            if(panel.getPreferredSize().height < questionPanel.getHeight()){
                int top = (questionPanel.getHeight() - panel.getPreferredSize().height)/2;
                questionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, top));
                questionPanel.add(panel);
            }else{
                questionPanel.setLayout(new GridLayout(1,1));
                JScrollPane sp = new JScrollPane(panel);
                questionPanel.add(sp);
            }
        }
        questionPanel.validate();
        questionPanel.repaint();
        //
        model.setStatusLabelString();
    }
}