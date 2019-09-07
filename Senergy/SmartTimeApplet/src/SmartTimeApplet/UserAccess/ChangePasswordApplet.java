/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.UserAccess;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;

/**
 *
 * @author nbp
 */
public class ChangePasswordApplet extends MyApplet {

    private Container contentsPane;
    JPasswordField txtoldpass, txtnewpass, txtre_enterpass;

    @Override
    public void init() {
        super.init();
        UserAccess();
    }

    public void UserAccess() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 1.2);
        int height = (int) (screenSize.height / 1.3);
        setSize(width, height);
        MyPanel MainPanel = new MyPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        MyPanel loginPane = new MyPanel(new BorderLayout());
        loginPane.setBorder(BorderFactory.createTitledBorder("Change Password"));
        loginPane.setPreferredSize(new Dimension(330, 250));
        MyPanel lblpan = new MyPanel(new GridLayout(6, 1, 0, 5));
        MyLabel lbloldpass = new MyLabel(1, "Old Password : ");
        lblpan.add(lbloldpass);

        MyLabel lblfill = new MyLabel();
        lblpan.add(lblfill);

        MyLabel lblnewpass = new MyLabel(1, "New Password : ");
        lblpan.add(lblnewpass);

        MyLabel lblfill1 = new MyLabel();
        lblpan.add(lblfill1);

        MyLabel lblre_enterpass = new MyLabel(1, "Re-enter password :");
        lblpan.add(lblre_enterpass);

        MyLabel lblfill2 = new MyLabel();
        lblpan.add(lblfill2);

        loginPane.add(lblpan, BorderLayout.WEST);

        MyPanel pantxt = new MyPanel(new GridLayout(6, 1, 0, 5));

        txtoldpass = new JPasswordField();
        pantxt.add(txtoldpass);

        MyLabel lblfill5 = new MyLabel();
        pantxt.add(lblfill5);

        txtnewpass = new JPasswordField();
        pantxt.add(txtnewpass);

        MyLabel lblfill3 = new MyLabel();
        pantxt.add(lblfill3);

        txtre_enterpass = new JPasswordField();
        pantxt.add(txtre_enterpass);

        MyLabel lblfill4 = new MyLabel();
        pantxt.add(lblfill4);

        loginPane.add(pantxt, BorderLayout.CENTER);

        MyPanel panMainsouth = new MyPanel(new FlowLayout(FlowLayout.RIGHT));
        MyButton btnconfirm = new MyButton("Confirm", 0) {

            @Override
            public void onClick() {
                int txt1size = txtoldpass.getPassword().length;
                int txt2size = txtnewpass.getPassword().length;
                int txt3size = txtre_enterpass.getPassword().length;

                if (!checkoldpassword()) {
                    MyUtils.showMessage("Invalid Password ! Enter valid old password");
                    clearAllFeilds();
                } else {
                    if (checkLength(txt2size, 2)) {
                        if (checkLength(txt3size, 3)) {
                            saveNewPassword();
                        }
                    }
                }
            }
        };
        panMainsouth.add(btnconfirm);
        loginPane.add(panMainsouth, BorderLayout.SOUTH);
        MainPanel.add(loginPane);
        contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private boolean checkLength(int size, int textno) {
        if (textno == 3) {//checking whether new password and re-entered password is same or not
            if (!(getPassword(txtnewpass).equals(getPassword(txtre_enterpass)))) {
                MyUtils.showMessage("Re-enter password correctly");
                txtre_enterpass.setText("");
                txtre_enterpass.requestFocus();
                return false;
            } else {
                return true;
            }
        }
        if ((size >= 6) & (size <= 12)) {
            if (textno == 2) {
                if ((getPassword(txtoldpass).equals(getPassword(txtnewpass)))) {
                    MyUtils.showMessage("Enter another new password.");
                    txtnewpass.setText("");
                    txtre_enterpass.setText("");
                    txtnewpass.requestFocus();
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            if (textno == 2) {
                MyUtils.showMessage("Enter new password between 6-12 characters only.");
                txtnewpass.setText("");
                txtre_enterpass.setText("");
                txtnewpass.requestFocus();
            }
            return false;
        }
    }

    private boolean checkoldpassword() {
        boolean isCorrect = false;
        try {
            String password = getPassword(txtoldpass);
            MyHTTP myHTTP = MyUtils.createServletConnection("ChangePasswordServlet");
            myHTTP.openOS();
            myHTTP.println("CheckOldPass");
            myHTTP.println(password);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(result));
            } else {
                if (result.equals(password)) {
                    isCorrect = true;
                } else {
                    isCorrect = false;
                }
            }

        } catch (Exception ex) {
            MyUtils.showMessage("checkoldpassword : " + ex);
        }
        return isCorrect;
    }

    private String getPassword(JPasswordField passfield) {
        String password = "";
        char passwordarray[] = passfield.getPassword();

        for (int i = 0; i < passwordarray.length; i++) {
            password = password + passwordarray[i];
        }
        return password;
    }

    private void saveNewPassword() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ChangePasswordServlet");
            myHTTP.openOS();
            myHTTP.println("SaveNewPassword");
            myHTTP.println(getPassword(txtnewpass));
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("ERROR")) {
                MyUtils.showException("Database ERROR", new Exception(result));
            } else {
                MyUtils.showMessage("New Password saved successfully.");
                clearAllFeilds();
            }

        } catch (Exception ex) {
            MyUtils.showMessage("Save new Password : " + ex);
        }
    }

    private void clearAllFeilds() {
        txtoldpass.setText("");
        txtnewpass.setText("");
        txtre_enterpass.setText("");
        txtoldpass.requestFocus();
    }
}
