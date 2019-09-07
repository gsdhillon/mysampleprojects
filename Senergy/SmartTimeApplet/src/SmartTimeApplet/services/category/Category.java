/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.category;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
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
public class Category extends MyApplet {

    private CategoryTableModel tableModel;
    private MyTable table;
    private MyPanel homePanel;
    private Container contentPane;

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            homePanel = CreateHomePanel();
            contentPane.add(homePanel);

        } catch (Exception e) {
            MyUtils.showException("Category", e);
        }
    }

    /*
     *
     */
    public MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new CategoryTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getCategoryMasterList();
        MyButton b2 = new MyButton("Add Category") {

            @Override
            public void onClick() {
                addCategory();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Category") {

            @Override
            public void onClick() {
                updateCategory();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Category") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteCategory();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search Category Name:</html>"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_EXACT_MATCH);
        table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);

        return ret;
    }

    /**
     *
     */
    private void addCategory() {
        contentPane.removeAll();
        contentPane.add(new CategoryMaster(this, "add", "1"));
        contentPane.validate();
        //loadPage("services/addCategory.jsp", "_self");
    }

    /**
     *
     */
    private void updateCategory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Category Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.add(new CategoryMaster(this, "update", id));
        contentPane.validate();
    }

    private void deleteCategory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Category Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("CategoryFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteCategory");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getCategoryMasterList();
        } catch (Exception ex) {
            MyUtils.showMessage(id + " " + ex);
        }
    }

    /**
     *
     */
    private void getCategoryMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("CategoryFormServlet");
            myHTTP.openOS();
            myHTTP.println("getCategoryList");
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

            CategoryClass[] Category = new CategoryClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                Category[i] = new CategoryClass();

                Category[i].CatCode = d.getString();
                Category[i].CatName = d.getString();
                Category[i].OverTime = d.getString();
                Category[i].OverTimeLimit = d.getString();
                Category[i].CompOff = d.getString();
                Category[i].CompOffLimit = d.getString();
                Category[i].GraceLateTime = d.getString();
                Category[i].GraceEarlyTime = d.getString();

            }
            tableModel.setData(Category);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetCategoryList", e);
            tableModel.setData(null);
        }
    }
    /*
     *
     */

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getCategoryMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
