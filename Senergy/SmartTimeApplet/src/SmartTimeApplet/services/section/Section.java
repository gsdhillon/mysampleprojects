package SmartTimeApplet.services.section;

import SmartTimeApplet.services.employee.Employee;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
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
 * @author GAURAV
 */
public class Section extends MyApplet {

    private SectionTableModel tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
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
        tableModel = new SectionTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getSectionMasterList();
        MyButton b2 = new MyButton("Add Section") {

            @Override
            public void onClick() {
                addSection();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Section") {

            @Override
            public void onClick() {
                updateSection();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Section") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteSection();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Section ID:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        table.addSearchField(2, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);

        return ret;
    }

    private void addSection() {
        //loadPage("services/addSection.jsp","_self");
        //new AddSectionDialog(this).setVisible(true);
        contentPane.removeAll();
        contentPane.add(new SectionMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateSection() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Section Selected");
            return;
        }
        String SecCode = (String) table.getValueAt(row, 1);
        contentPane.removeAll();
        contentPane.add(new SectionMaster(this, "update", SecCode));
        contentPane.validate();
        contentPane.repaint();

    }

    private void deleteSection() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Section Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 1);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteSection");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            // MyUtils.showMessage(result);
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getSectionMasterList();
        } catch (Exception ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void getSectionMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SectionFormServlet");
            myHTTP.openOS();
            myHTTP.println("getSectionList");
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

            SectionClass[] Sections = new SectionClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                Sections[i] = new SectionClass();
                Sections[i].DivisionName = d.getString();
                Sections[i].SectionID = d.getString();
                Sections[i].SectionName = d.getString();
                Sections[i].SectionHeadName = d.getString();
                Sections[i].SectionHeadID = d.getString();

            }
            tableModel.setData(Sections);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetDivisionList", e);
            tableModel.setData(null);
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getSectionMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
