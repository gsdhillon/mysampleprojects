/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddCompany;

import SmartTimeApplet.services.employee.Employee;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author nbpatil
 */
public class AddCompany extends MyApplet{
    private Container contentPane;
     private EmployeeTableModel tableModel; 
     private CmpVisitorTableModel CmptableModel;
      private MyPanel homePanel;
    private MyTable table;
    private MyButton b5;
    
    
    
     public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            contentPane.setBackground(Color.cyan);
            homePanel = CreateHomePanel();
            contentPane.add(homePanel);
            contentPane.validate();
            contentPane.repaint();
        } catch (Exception e) {
            MyUtils.showException("UserMaster", e);
        }
    }
    
      private MyPanel CreateHomePanel() throws Exception {
       
       MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
       tableModel = new EmployeeTableModel();

        //MyPanel main = new MyPanel(new BorderLayout());
        MyPanel p = new MyPanel(new GridLayout(1, 5));
      
        getCmpInfo();
           MyButton b2 = new MyButton("Add Company") {

            @Override
            public void onClick() {
                addCompany();
               
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Company") {

            @Override
            public void onClick() {
                updateCmpInfo();
               
            }
        };
        p.add(b3);
//        MyButton b4= new MyButton("Show Details") {
//
//           @Override
//            public void onClick() {
//                ShowDetails();
//                //throw new UnsupportedOperationException("Not supported yet.");
//            }
//       };
//        p.add(b4);
        MyButton b5= new MyButton("Delete Company") {

            @Override
            public void onClick() {
                deleteCompany();
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        b5.setEnabled(false);
        p.add(b5);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Company ID:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    
        //throw new UnsupportedOperationException("Not yet implemented");
    }
  
     private void addCompany() {
        contentPane.removeAll();
        contentPane.add(new AddCompanyMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();

    }

    //Delete Company for CEP
    private void deleteCompany() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No company Selected");
            return;
        }
        String PCOM_ID = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("deleteCompanyInfo");
            myHTTP.println(PCOM_ID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            // MyUtils.showMessage(result);
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getCmpInfo();
        } catch (Exception ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showVisitorInfo() throws Exception {
        //String PVIS_ID="";
        contentPane.removeAll();
        //contentPane.add(new ShowVisitor(this));
        contentPane.validate();
        contentPane.repaint();


    }
    //Dispaly The Informarion of New Company

    private void getCmpInfo() {
        try {

            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getCmpInfo");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage("getCmpInfo response :"+response);
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();

            CompanyClass[] cmps = new CompanyClass[rowCount];
            for (int i = 0; i < rowCount; i++) {

                cmps[i] = new CompanyClass();
                cmps[i].CompanyID = d.getString();
                cmps[i].Name = d.getString();
                cmps[i].City = d.getString();
                cmps[i].Address = d.getString().replace("#", " ");
                // cmps[i].Address.split("Address");

                // cmps[i].Added = d.getString();
            }

            tableModel.setData(cmps);
        } catch (Exception e) {
            MyUtils.showException("getCmpInfo", e);
            tableModel.setData(null);
        }

    }
    //update CompanyDetails

    private void updateCmpInfo() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("Please SelectCompany");
            return;
        }
        String PCOM_ID = (String) table.getValueAt(row, 0);
        

        contentPane.removeAll();
        contentPane.add(new AddCompanyMaster(this, "update", PCOM_ID));
        contentPane.validate();
        contentPane.repaint();

    }
    //Show Details of  Company;
    
    
    
    private void getCmpVisitorInfo(String PCOM_ID) {
        try {

            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("getCmpVisitorInfo");
            myHTTP.println(PCOM_ID);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();

            CmpVisitorClass[] CmpVst = new CmpVisitorClass[rowCount];
            for (int i = 0; i < rowCount; i++) {

                CmpVst[i] = new CmpVisitorClass();
                //CmpVst[i].CompanyID = d.getString();
                CmpVst[i].Name = d.getString();
                CmpVst[i].City = d.getString();
                CmpVst[i].Address = d.getString().replace("$", " ");
                CmpVst[i].VisitorId=d.getString();
                CmpVst[i].VName=d.getString();
                CmpVst[i].VDesing=d.getString();
               
            }

            CmptableModel.setData(CmpVst);
        } catch (Exception e) {
            MyUtils.showException("getCmpVisitorInfo", e);
            CmptableModel.setData(null);
        }

    }
   
    private void ShowDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("Please SelectCompany");
            return;
        }
        
        contentPane.removeAll();
        //getCmpVisitorInfo(null);
        contentPane.validate();
        contentPane.repaint();

    }

   
    public void ShowHomePanel() {

        contentPane.removeAll();
        contentPane.add(homePanel);
        contentPane.validate();
        getCmpInfo();
        contentPane.repaint();
    }
      
      
}
