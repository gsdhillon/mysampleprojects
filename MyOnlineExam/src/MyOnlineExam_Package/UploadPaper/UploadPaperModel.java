package MyOnlineExam_Package.UploadPaper;

import MyOnlineExam_Package.MySettings;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class UploadPaperModel {
    private int questionIndex = -1;
    private int numQuestions = 0;
    private JLabel statusLabel;
    private UploadQuestion[] questions = new UploadQuestion[500];
    public UploadPaperModel(JLabel statusLabel){
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
                questions[numQuestions] = new UploadQuestion(numQuestions+1, file, statusLabel);
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
        if(questionIndex < 0){
            return "";
        }
        return questions[questionIndex].getQuestionNoString()+" of "+numQuestions;
    }
    
    public void setStatusString(){
        //
        if(questionIndex < 0){
            return;
        }
        questions[questionIndex].setStatusString();
    }

    public JPanel getMiddlePanel() {
        if(questionIndex < 0){
            return null;
        }
        return questions[questionIndex].getQuationImage();
    }
    
//    public void setCorrectOption(String option){
//        if(questionIndex < 0){
//            return;
//        }
//        questions[questionIndex].setCorrectOption(option);
//    }

    public void saveData() {
        int count = 0;
        for(int i = 0;i<numQuestions;i++){
            if(questions[i].saveData()){
                count++;
            }
        }
        JOptionPane.showMessageDialog(null, "Data of "+count+" out of "+numQuestions+" questions have been saved.");
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
        questions[questionIndex].clearAnswerData();
    }
}