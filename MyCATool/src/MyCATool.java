
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mykeystores.MyKeyGUI;
import security_providers.MyCertUtil;
import security_providers.MyKeyStore;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@SuppressWarnings("CallToThreadDumpStack")
public class MyCATool extends JFrame {
    /**
     * 
     */
    public MyCATool() {
        setLayout(new BorderLayout());
        //
        String storePath = home+"/keystore";
        JTabbedPane tp = new JTabbedPane();
        tp.addTab("ROOT CA", new MyKeyGUI(storePath, MyKeyGUI.TYPE_ROOT_CA));
        tp.addTab("INTERMEDITAE CA", new MyKeyGUI(storePath, MyKeyGUI.TYPE_INTERMEDIATE_CA));
        tp.addTab("END ENTITY", new MyKeyGUI(storePath, MyKeyGUI.TYPE_END_ENTITY));
        //
        add(tp, BorderLayout.CENTER);
    }
    /**
     * 
     * @throws Exception 
     */
    private static void testSignVerify() throws Exception {
        MyKeyStore key = new MyKeyStore("24196");
        key.loadKeyStorePKCS11("SafeNetToken", System.getenv("windir") + "\\system32\\dkck201.dll");
//        key.loadKeyStoreJKS(new File(home+"/keystore/user_24196.jks"));
        key.loadPrivateKeyEntry();
        byte[] in = new byte[512];
        for(int i=0;i<in.length;i++) in[i] = 'A';
        String base64SignedData = key.signAndPack(in);
        byte[] data = MyCertUtil.verifyAndUnpack(base64SignedData);
        System.out.println(new String(data));
    }
    /**
     * 
     * @param args
     * @throws Exception 
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) throws Exception {
        initialize(args);
//        testSignVerify();
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                MyCATool frame = new MyCATool();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setSize(screenSize.width, screenSize.height-35);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    private static final String applicationName = "MyCATool";
    private static String home = "MyCATool";
    /**
     *
     * @return
     */
    @SuppressWarnings("UseSpecificCatch")
    private static void initialize(String[] args) {
        try{
            if(args != null && args.length > 0 && args[0].equalsIgnoreCase("debug")){
            }
        }catch(Exception e){
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
        }
        try{
            String s = MyCATool.class.getResource("MyCATool.class").getPath();
            s = s.replaceAll("%20", " ");
            home = s.substring(s.indexOf('/')+1,s.indexOf(applicationName)+applicationName.length());
            System.out.println(home);
        }catch(Exception e){
            home = ".";
        }
    }
}
