package mypa;

/**
 *
 * @author ADMIN
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        if(args != null && args.length == 1){
            new MySecretPacker().setVisible(true);
        }else{
            new MySecretUnpacker().setVisible(true);
        }
    }
}
