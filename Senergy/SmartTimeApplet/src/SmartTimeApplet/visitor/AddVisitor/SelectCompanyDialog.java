/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddVisitor;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author ROOP
 */
public class SelectCompanyDialog extends JDialog {

    MyTable table;
    CompanyTableModel tableModel;
    MyTextField txtCompany1;
    MyPanel tabPan;
//    AddNewVisitor AddNewVisitor;
//    AddNewCep AddNewCep;

    public SelectCompanyDialog(MyApplet myApplet, MyTextField txtCompany) {
        super(new JFrame(), "Select Company", true);
        try {
            this.txtCompany1 = txtCompany;
            this.setLayout(new BorderLayout());

            tabPan = new MyPanel(new BorderLayout());
            MyPanel pan = new MyPanel(new GridLayout(1, 5));

            tableModel = new CompanyTableModel();
            getCmpInfo();
            table = new MyTable(tableModel);

            pan.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Company ID:</html>"));
            MyTextField searchTextName = new MyTextField();
            pan.add(searchTextName);
            tabPan.add(pan, BorderLayout.NORTH);
            //create Documents table
            table = new MyTable(tableModel);

            table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
            table.addSearchField(2, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
            tabPan.add(table.getGUI());
            this.add(tabPan, BorderLayout.CENTER);



            MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));

            MyButton btnStartUpld = new MyButton("Select Company", 2) {

                @Override
                public void onClick() {
                    String strcmp = getSelectedCmpId();
                    String delimiter = " ";
                    strcmp.split(delimiter);
                    txtCompany1.setText(strcmp);
                    //txtCompany1.setText(getSelectedCmpId());
                    closeDialog();
                }
            };
            panButtons.add(btnStartUpld);

            MyButton btnCancel = new MyButton("Cancel", 2) {

                @Override
                public void onClick() {
                    closeDialog();
                }
            };
            panButtons.add(btnCancel);
            this.add(panButtons, BorderLayout.SOUTH);
            Point p = myApplet.getLocationOnScreen();
//            if (myApplet instanceof AddNewVisitor) {
//               
//            }else if(myApplet instanceof AddNewCep){
//                Point p = myApplet.getLocationOnScreen();
//            }

            setLocation(p.x + 60, p.y + 60);
            setSize(500, 450);
        } catch (Exception ex) {
            MyUtils.showMessage("SelectCompanyDialog : " + ex);
        }
    }

    private void closeDialog() {
        setVisible(false);
    }

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
                //MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage("getCmpInfo response :" + response);
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();
            CompanyClass[] cmps = new CompanyClass[rowCount];
            for (int i = 0; i < rowCount; i++) {
                cmps[i] = new CompanyClass();
                cmps[i].CompanyID = d.getString();
                cmps[i].Name = d.getString();
                cmps[i].City = d.getString();
                cmps[i].Address = d.getString().replace("#", " ");
            }
            tableModel.setData(cmps);
            tabPan.repaint();
            tabPan.validate();
        } catch (Exception e) {
            MyUtils.showException("getCmpInfo", e);
            tableModel.setData(null);
        }

    }

    private String getSelectedCmpId() {
        int totalCmpId = tableModel.getRowCount();
        String getCmpId = "";

        for (int i = 0; i < totalCmpId; i++) {

            if (totalCmpId == -1) {
                MyUtils.showMessage("Please Select One Company");
                return "";
            }

            if (tableModel.isRowChecked(i)) {

                getCmpId = (tableModel.getValueAt(i, 1)).toString();
                return getCmpId;
            }
        }
        return getCmpId;
    }
//    public boolean checkMoreThanOneCompany(){
//       
//    }
}
