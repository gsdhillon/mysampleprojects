package MyOnlineExam_Package;

import MyOnlineExam_Package.TakeExam.TakeExamFrame;
import javax.swing.SwingUtilities;

/**
 * Created on Aug 10, 2013
 * @version 1.0.0
 * @author : Gurmeet Singh, EMail-gsdhillon@gmail.com, Ph-9869117976
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MySettings.initialize();
//                MyOnScreenPasswordDialog d = new MyOnScreenPasswordDialog();
//                if(d.mode == 0){
//                    new UploadPaperFrame();
//                }else{
                    new TakeExamFrame();
//                }
            }
        });
    }
}