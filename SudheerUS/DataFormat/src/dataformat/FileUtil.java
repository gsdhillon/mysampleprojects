package dataformat;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 *
 * @author gsdhillon@gmail.com
 */
public class FileUtil {
    static final String homePath = "D:/SudheerData/";

    static void browse(JTextField inFileTxt, JTextField outFileTxt) {
        try{
            JFileChooser fileChooser = new JFileChooser(new File(homePath));
            int option = fileChooser.showOpenDialog(null);
            if(option != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            inFileTxt.setText(path);
            if(path.endsWith(".egslst")){
                outFileTxt.setText(path.substring(0, path.length()-6) + "csv");
            }
        }catch(Exception e){
            
        }
    }
    
}
