package MyOnlineExam_Package.UploadPaper;

import java.io.File;
import javax.swing.JLabel;
/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class UploadQuestion { 
    public int questionNo = 0;
    public File questionFile = null;
    private UploadPaperImage image = null;
    private JLabel statusLabel;
    public UploadQuestion(int questionNo, File questionFile, JLabel statusLabel){
        this.statusLabel = statusLabel;
        this.questionNo = questionNo;
        this.questionFile = questionFile;
    }
    public boolean fetchQuestion(){
        try{
            image =  new UploadPaperImage(questionFile, statusLabel);
            return image.found();
        }catch(Exception e){
            return false;
        }
    }
    
    
    
    
    /**
     * 
     * @return 
     */
    public UploadPaperImage getQuationImage() {
        return image;
    }

    public String getQuestionNoString(){
        return "Question number "+questionNo;
    }
    
    public void setStatusString(){
        image.setStatusString();
    }

//    public void setCorrectOption(String option) {
//        image.setCorrectOption(option);
//    }

    public boolean saveData() {
        return image.saveData();
    }

    public void clearAnswerData() {
        image.clearAnswerData();
    }
    
}