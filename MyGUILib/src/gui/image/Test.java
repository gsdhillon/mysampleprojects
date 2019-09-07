package gui.image;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Class Test
 * Created on Sep 4, 2013
 * @version 1.0.0
 * @author
 */
public class Test {
    public static void main(String[] args) throws Exception{
        File in = new File("D:/Gurmeet/Father.bmp");
        File out = new File("D:/Gurmeet/Father1.JPG");
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(ImageTool.scaleImage(in, 40, 60));
        fos.close();
        //
        in = new File("D:/Gurmeet/Brother.PNG");
        out = new File("D:/Gurmeet/Brother1.JPG");
        fos = new FileOutputStream(out);
        fos.write(ImageTool.scaleImage(in, 40, 60));
        fos.close();
        //
        in = new File("D:/Gurmeet/me.JPG");
        out = new File("D:/Gurmeet/me1.JPG");
        fos = new FileOutputStream(out);
        fos.write(ImageTool.scaleImage(in, 40, 60));
        fos.close();
    }
}