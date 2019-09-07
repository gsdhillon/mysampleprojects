package MyOnlineExam_Package.TakeExam;

import MyOnlineExam_Package.MySettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class TakeExamModel {
    public int questionIndex = -1;
    public int numQuestions = 0;
    public TakeExamQuestion[] questions = new TakeExamQuestion[500];
    public boolean submitted = false;
    public boolean showResult = false;
    private String resultStatus = "Not Submited";
    private JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    private final JLabel statusLabel;
    /**
     * 
     * @param statusLabel 
     */
    public TakeExamModel(JLabel statusLabel){
        this.statusLabel = statusLabel;
    }
    /**
     * 
     */
    public void fetchAllQuestion(){
        numQuestions = 0;
        questionIndex = -1;
        File questionFolder = new File(MySettings.home+"/Data/Questions");
        if(!questionFolder.exists()){
            JOptionPane.showMessageDialog(null, "Folder \""+questionFolder.getAbsolutePath()+"\" not found!");
            return;
        }
        File[] allQuestionsFiles = questionFolder.listFiles();
        for(File file : allQuestionsFiles){
            if(file.getName().startsWith("Q")){
                questions[numQuestions] = new TakeExamQuestion(numQuestions+1, file, statusLabel);
                if(questions[numQuestions].fetchQuestion()){
                   numQuestions++; 
                }
            }
        }
        if(numQuestions>0){
            questionIndex = 0;
        }
    }
    
    public String getLabelString(){
        if(showResult){
            return "Result";
        }
        if(questionIndex < 0){
            return "";
        }
        return questions[questionIndex].getQuestionNoString()+" of "+numQuestions;
    }
    
    public void setStatusLabelString(){
        if(showResult){
            statusLabel.setText(resultStatus);
            return;
        }
        //
        if(questionIndex < 0){
            statusLabel.setText("");
            
        }else{
            statusLabel.setText(questions[questionIndex].getStatusString());
        }
        
    }

    public JPanel getMiddlePanel() {
        if(showResult){
            return resultPanel;
        }
        if(questionIndex < 0){
            return null;
        }
        return questions[questionIndex].getQuationImage();
    }

    public void submitPaper() {
        submitted = true;
        showResult = true;
        resultPanel.setBackground(Color.WHITE);
        int totalMarks = 0;
        int marksObtained = 0;
        for(int i = 0;i<numQuestions;i++){
            questions[i].setSubmitted(submitted);
            totalMarks += questions[i].getTotalMarks();
            marksObtained += questions[i].getMarks();
            //
            JLabel l = new JLabel(questions[i].getMarksString(), JLabel.CENTER);
            l.setForeground(Color.BLUE.darker());
            l.setPreferredSize(new Dimension(MySettings.width - 80, 25));
            l.setFont(new Font("MONOSPACED", Font.BOLD, 16));
            resultPanel.add(l);
        }
        resultPanel.setPreferredSize(new Dimension(MySettings.width - 80, numQuestions*25+10));
        resultStatus = "Total marks obtained = "+marksObtained+" out of "+totalMarks;
    }

    public void previousQuestion() {
        if(questionIndex > 0){
            questionIndex --;
        }
    }
    
    public void nextQuestion() {
        if(questionIndex < (numQuestions-1)){
            questionIndex ++;
        }
    }

    public void clearCurrentAnswer() {
        if(questionIndex < 0){
            return;
        }
        questions[questionIndex].clearAnswer();
    }

    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }
}