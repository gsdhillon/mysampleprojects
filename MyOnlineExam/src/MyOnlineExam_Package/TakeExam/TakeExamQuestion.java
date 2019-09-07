package MyOnlineExam_Package.TakeExam;

import java.io.File;
import javax.swing.JLabel;
/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author   : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class TakeExamQuestion {
    public int questionNo = 0;
    public File questionFile = null;
    private TakeExamImage image = null;
    private final JLabel statusLabel;
    public TakeExamQuestion(int questionNo, File questionFile, JLabel statusLabel){
        this.questionNo = questionNo;
        this.questionFile = questionFile;
        this.statusLabel = statusLabel;
    }
    public boolean fetchQuestion(){
        try{
            image =  new TakeExamImage(questionFile, statusLabel);
            return (image.found());
        }catch(Exception e){
            return false;
        }
    }
    /**
     * 
     * @return 
     */
    public TakeExamImage getQuationImage() {
        return image;
    }
    
    public int getMarks(){
        return image.getMarks();
    }
    
    public int getTotalMarks(){
        return image.getTotalMarks();
    }
    
    public String getQuestionNoString(){
        return "Question number "+questionNo;
    }
    public void setSubmitted(boolean submitted){
        image.setSubmitted(submitted);
    }
    public String getStatusString(){
        return image.getStatusString();
    }
    public String getMarksString(){
        String num = String.valueOf(questionNo);
        if(questionNo<10){
            num = " "+num;
        }
        int marks = getMarks();
        if(marks < 0){
            return num+". Marks obtained = "+marks+" out of "+getTotalMarks();
        }else{
            return num+". Marks obtained =  "+marks+" out of "+getTotalMarks();
        }
    }
    public void clearAnswer() {
        image.clearAnswer();
    }
}