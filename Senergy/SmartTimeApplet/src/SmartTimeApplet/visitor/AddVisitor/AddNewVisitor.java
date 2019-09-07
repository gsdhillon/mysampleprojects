/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.visitor.AddVisitor;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class AddNewVisitor extends MyApplet implements ActionListener {

    private Container contentPane;
    private MyPanel homePanel;
    private VisitortableModel tableModel;
    MyPanel panSearch;
    private MyTable table;

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
        tableModel = new VisitortableModel();

        // MyPanel main = new MyPanel(new BorderLayout());
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        showVisitor();
        MyButton b2 = new MyButton("Add Visitor") {

            @Override
            public void onClick() {
                addVisitor();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Visitor") {

            @Override
            public void onClick() {
                updateVisitorInfo();

            }
        };
        p.add(b3);

        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Visitor Name:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        table.addSearchField(2, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;

        //throw new UnsupportedOperationException("Not yet implemented");
    }
    //For AddNew Visitor  

    private void addVisitor() {
        contentPane.removeAll();
        contentPane.add(new AddVisitorMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();

    }

    void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        showVisitor();
        contentPane.validate();
        contentPane.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void showVisitor() {
        try {

            MyHTTP myHTTP = MyUtils.createServletConnection("VisitorServlet");
            myHTTP.openOS();
            myHTTP.println("showVisitorInfo");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                //MyUtils.showMessage(response);
                return;
            }
            //MyUtils.showMessage("Show Visitor response :"+response);
            Depacketizer d = new Depacketizer(response);
            int rowCount = d.getInt();

            VisitorInfoClass[] visitors = new VisitorInfoClass[rowCount];
            for (int i = 0; i < rowCount; i++) {

                visitors[i] = new VisitorInfoClass();
                visitors[i].VisitorId = d.getString();
                visitors[i].PCOM_ID = d.getString();
                visitors[i].VisitorName = d.getString();
                visitors[i].Occupation = d.getString();



            }

            tableModel.setData(visitors);

        } catch (Exception e) {
            MyUtils.showException("showVisitor", e);
            tableModel.setData(null);
        }

    }

    private void updateVisitorInfo() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("please Select VisitorId");
            return;
        }
        String PVIS_ID = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.add(new AddVisitorMaster(this, "update", PVIS_ID));
        contentPane.validate();
        contentPane.repaint();

    }
}
