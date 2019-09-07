package mytreeview;

/**
 * Class Main
 * Created on Aug 15, 2013
 * @version 1.0.0
 * @author
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MainFrame.createAndShow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
