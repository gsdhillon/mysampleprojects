package lib.digsign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;

/**
 * DigSignAuth.java
 */
public class DigSignAuth  extends DigSignUser implements Serializable {
    public String role = "";
    public String action = "";
    public String remarks = "";
    public String certSerNo = "";
    public String signDate = "";
    public String sign = "";
    public DigSignCertificate cert = null;
    /**
     * who is logged in
     * @throws Exception
     */
    public void getAuthInfo() throws Exception{
        MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
        myHTTP.openOS();
        myHTTP.println("getApplicantInfo");
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
        certSerNo = d.getString();//current certificate at server
    }


    /**
     * 
     * @return '' 
     */
    public MyPanel createGUIPanel(){
        MyPanel panel = new MyPanel(new GridLayout(0, 1, 0, 0));
        int labelWidth = 80;
        //UserID
        MyPanel p = new MyPanel(new BorderLayout());
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, "UserID: ");
        Dimension labelDimension = new Dimension(labelWidth, l.getPreferredSize().height);
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, userID), BorderLayout.CENTER);
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
        //EMailID/PhoneNo
        p = new MyPanel(new BorderLayout());
        l = new MyLabel(MyLabel.TYPE_LABEL, "Phone: ");
        l.setPreferredSize(labelDimension);
        p.add(l, BorderLayout.WEST);
        p.add(new MyLabel(MyLabel.TYPE_DATA, phoneNo), BorderLayout.CENTER);
        panel.add(p);
        return panel;
    }
}