/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddCompany;

import lib.session.MyHTTP;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author nbp
 */
public class CompanyDetails extends MyPanel {

    private MyButton btOk;
    Container con;
    private AddCompany addCompanyapplet;
    private String Job;
    private String companyid;
    private CommonTableModel tableModel;
    private Container contentPane;
    private MyTable table;
  

    /**
     * Creates new form CEPCompanyDetails
     */
    public CompanyDetails(AddCompany addCompanyapplet, String Job, String companyid) {
        try {
            this.setLayout(new BorderLayout());
            this.addCompanyapplet = addCompanyapplet;
            this.Job = Job;
            this.companyid = companyid;
//            this.add(CreateHomePanel());
            if (Job.equals("show")) {
                CreateHomePanel();
            showCmpVisitor(companyid);
           }
        } catch (Exception ex) {
           // Logger.getLogger(CEPCompanyDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public MyPanel CreateHomePanel() throws Exception {

        MyPanel MainPan = new MyPanel(new BorderLayout(10, 10));
        MyPanel LblPan = new MyPanel(new GridLayout());
        
        tableModel=new CommonTableModel();
        
        btOk = new MyButton("Cancel", 2, Color.WHITE) {
            

            @Override
            public void onClick() {
                addCompanyapplet.ShowHomePanel();
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        LblPan.add(btOk, BorderLayout.CENTER);

        return MainPan;

    }
    //dispaly all  Visitor Information According to Company

     private void showCmpVisitor(String compid){
        try {
            
          MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getCmpVisitorInfo");
            myHTTP.println(compid);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage("Show Visitor response :"+response);
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
             CommonClass[] CmpVstInfo = new CommonClass[rowCount];
             for(int i=0;i <rowCount; i++){
             
                CmpVstInfo[i] = new CommonClass();
                CmpVstInfo[i].CompanyID = d.getString();
                CmpVstInfo[i].CompanyNm = d.getString();
                CmpVstInfo[i].CompanyAdd = d.getString().replace("$", " ");
                CmpVstInfo[i].CompanyCity = d.getString();
                CmpVstInfo[i].VId = d.getString();
                CmpVstInfo[i].VName = d.getString();
                CmpVstInfo[i].Vdesig = d.getString();
                          
                
             }
             
                tableModel.setData(CmpVstInfo);
        } catch (Exception e) {
            MyUtils.showException("showCmpVisitor", e);
            tableModel.setData(null);
        }
    
    }
   
  
    
    
}
