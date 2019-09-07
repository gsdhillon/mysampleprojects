package lib.digsign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import javax.swing.BorderFactory;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;

/**
 * DigSignUser.java
 */
public class DigSignUser  implements Serializable {
    public String hostSNo="";//in host list
    public String userID="0";
    public String name = "";
    public String designation = "";
    public String email = "";
    public String phoneNo = "";
    //
    public String recNeeded = "";
    public String recUserID = "0";
    public String appUserID = "0";
    /**
     *
     * @return
     * @throws Exception
     */
    public boolean isRecNeeded() throws Exception{
        if(recNeeded == null || recNeeded.equals("")){
            throw new Exception("REC_INFO_NOT_SET");
        }
        return recNeeded.equals("Y");
    }
    /**
     * who is logged in
     * @throws Exception
     */
    public void getInfo() throws Exception{
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("getUserInfo");
        myHTTP.closeOS();
        myHTTP.openIS();
        String response = myHTTP.readLine();
        myHTTP.closeIS();
        if (response.startsWith("ERROR")) {
            throw new Exception(response);
        }
        Depacketizer d = new Depacketizer(response);
        userID = d.getString();
        name = d.getString();
        designation = d.getString();
        email = d.getString();
        phoneNo = d.getString();
    }
    /**
     * who is logged in
     * @param recNeededField - name of the field in MySQL table
     * @param recIDField - name of the field in MySQL table
     * @param appIDField - name of the field in MySQL table
     * @throws Exception
     */
    public void getInfo(String recNeededField, String recEmpNoField, String appEmpNoField) throws Exception{
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("GetUserInfoWithRecApp");
        myHTTP.println(recNeededField);
        myHTTP.println(recEmpNoField);
        myHTTP.println(appEmpNoField);
        myHTTP.closeOS();
        myHTTP.openIS();
        String response = myHTTP.readLine();
        myHTTP.closeIS();
        if (response.startsWith("ERROR")) {
            throw new Exception(response);
        }
        Depacketizer d = new Depacketizer(response);
        userID = d.getString();
        name = d.getString();
        designation = d.getString();
        email = d.getString();
        phoneNo = d.getString();
        recNeeded = d.getString();
        recUserID = d.getString();
        appUserID = d.getString();
        if (recNeeded.equals("")) {
            MyUtils.showMessage(
                    "Information about your recommender is not available!"
            );
            throw new Exception("REC_INFO_NOT_FOUND");
        }
        if (recNeeded.equals("Y")) {
            if (recUserID.equals("")) {
                MyUtils.showMessage(
                        "Information about your recommender is not available!"
                );
                throw new Exception("REC_INFO_NOT_FOUND");
            }
        }
        if (appUserID.equals("")) {
            MyUtils.showMessage(
                    "Information about your approver is not available!"
            );
            throw new Exception("APP_INFO_NOT_FOUND");
        }
    }
    /**
     * other user with userID
     * @param userID
     * @throws Exception
     */
    public void getUserInfo(String userID) throws Exception{
        this.userID = userID;
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("getUserInfoWithUserID");
        myHTTP.println(userID);
        myHTTP.closeOS();
        myHTTP.openIS();
        String response = myHTTP.readLine();
        myHTTP.closeIS();
        if (response.startsWith("ERROR")) {
            throw new Exception(response);
        }
        Depacketizer d = new Depacketizer(response);
        name = d.getString();
        designation = d.getString();
        email = d.getString();
        phoneNo = d.getString();
    }
    /**
     *
     * @param title
     * @return
     */
    public MyPanel createGUIPanel(String title) {
        MyPanel panel = new MyPanel(new GridLayout(0, 2, 0, 5));
        int labelWidth = 100;
        //UserID
        MyPanel p = new MyPanel(new BorderLayout());
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "UserID: ");
        Dimension labelDimension = new Dimension(labelWidth, l.getPreferredSize().height);
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, userID), BorderLayout.CENTER);
        panel.add(p);
        //EMailID/PhoneNo
        p = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "EMail/Ph: ");
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, email+"/"+phoneNo), BorderLayout.CENTER);
        panel.add(p);
        //Name
        p = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Name: ");
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, name), BorderLayout.CENTER);
        panel.add(p);
        //Designation
        p = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Desig:");
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, designation), BorderLayout.CENTER);
        panel.add(p);
        if(title != null){
            panel.setBorder(BorderFactory.createTitledBorder(title));
        }
        return panel;
    }
}