package SmartTimeApplet.visitor.vis_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.session.MyApplet;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;
/**
 *
 * @author nbpatil
 */
public class ShowCompanyListApplet extends MyApplet {
    private Container contentPane;
    private MyPanel homePanel;
    private MyTable table;
    private CompanyTM tableModel;
    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            contentPane.setBackground(Color.cyan);
            homePanel = createHomePanel();
            contentPane.add(homePanel);
//            contentPane.validate();
//            contentPane.repaint();
        } catch (Exception e) {
            MyUtils.showException("UserMaster", e);
        }
    }

    private MyPanel createHomePanel() throws Exception {
        tableModel = new CompanyTM();
        MyPanel panel = new MyPanel();
        panel.setLayout(new BorderLayout());
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getCompanyInfo();
        MyButton b2 = new MyButton("Apply CEP") {
            @Override
            public void onClick() {
                applyCep();

            }
        };
        p.add(b2);
        /*
        MyButton b3 = new MyButton("AddCompany") {

        @Override
        public void onClick() {
        //updateCmpInfo();

        }
        };
        p.add(b3);
        MyButton b4= new MyButton("AddVisitor") {

        @Override
        public void onClick() {
        //ShowDetails();
        //throw new UnsupportedOperationException("Not supported yet.");
        }
        };
        p.add(b4);
         */
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Company ID:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        panel.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);
        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        //table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        panel.add(table.getGUI(), BorderLayout.CENTER);
        return panel;

    }
    /**
     * 
     */
    private void getCompanyInfo() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("VisAppServlet");
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
            Company[] companyList = new Company[rowCount];
            for (int i = 0; i < rowCount; i++) {
                companyList[i] = new Company();
                companyList[i].companyID = d.getString();
                companyList[i].companyName = d.getString();
                companyList[i].city = d.getString();
                companyList[i].address = d.getString().replace("$", " ");
            }
            tableModel.setData(companyList);
        } catch (Exception e) {
            MyUtils.showException("getCompanyList", e);
            tableModel.setData(null);
        }

    }
    /**
     *
     */
    private void applyCep() {
        int row = table.getSelectedRow();
        
        if (row == -1) {
            MyUtils.showMessage("Please Select Company");
            return;
        }
        String companyID = (String) table.getValueAt(row, 0);
       // MyUtils.showMessage("Selected Company = "+companyID);
        contentPane.removeAll();
        contentPane.add(new ApplyVisitorForm(this, companyID));
        contentPane.validate();
        contentPane.repaint();

    }
    /**
     * 
     */
    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        //showVisitor();
        contentPane.validate();
        contentPane.repaint();
    }
}
