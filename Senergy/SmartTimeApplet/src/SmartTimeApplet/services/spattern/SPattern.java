/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.spattern;

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
public class SPattern extends MyApplet {

    private SPatternTableModel tableModel;
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

        } catch (Exception e) {
            MyUtils.showException("Shift Pattern", e);
        }
    }
    /*
     *
     */

    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new SPatternTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));

        getSPatternMasterList();
        MyButton b2 = new MyButton("Add Shift Pattern") {

            @Override
            public void onClick() {
                addSPattern();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Shift Pattern") {

            @Override
            public void onClick() {
                updateSPattern();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Shift Pattern") {

            @Override
            public void onClick() {
                if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                    deleteSPattern();
                } else {
                    return;
                }

            }
        };
        p.add(b4);
        p.add(new MyLabel(MyLabel.TYPE_LABEL, "<html>Search by Shift Pattern Code:</html>", MyLabel.ALIGN_CENTER));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addSPattern() {
        contentPane.removeAll();
        contentPane.add(new SPatternMaster(this, "add", "0"));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateSPattern() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Shift Pattern Selected");
            return;
        }
        String SPCode = (String) table.getValueAt(row, 0);
        contentPane.removeAll();

        contentPane.add(new SPatternMaster(this, "update", SPCode));

        contentPane.validate();
        contentPane.repaint();
    }

    private void deleteSPattern() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No row Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
            myHTTP.openOS();
            myHTTP.println("DeleteSPattern");
            myHTTP.println(id);
            myHTTP.closeOS();
            myHTTP.openIS();
            String result = myHTTP.readLine();
            myHTTP.closeIS();
            if (result.startsWith("Error")) {
                MyUtils.showMessage(result);
            }
            getSPatternMasterList();
        } catch (Exception ex) {
            MyUtils.showException("SPattern", ex);
        }
    }

    /**
     *
     *
     */
    private void getSPatternMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("SPatternFormServlet");
            myHTTP.openOS();
            myHTTP.println("getSPatternList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response1 = myHTTP.readLine();
            String response2 = myHTTP.readLine();
            myHTTP.closeIS();
            if (response1.startsWith("ERROR")) {
                MyUtils.showMessage(response1);
                return;
            } else if (response2.startsWith("ERROR")) {
                MyUtils.showMessage(response2);
                return;
            }

            Depacketizer d = new Depacketizer(response1);
            int SCount = d.getInt();

            SPatternClass[] Sections = new SPatternClass[SCount];
            for (int i = 0; i < SCount; i++) {
                Sections[i] = new SPatternClass();
                Sections[i].patternCode = d.getString();
            }

            Depacketizer dp = new Depacketizer(response2);
            int rowCount = dp.getInt();
            int position, j;
            String patterncode;

            for (int i = 0; i < rowCount; i++) {

                patterncode = dp.getString();
                position = dp.getInt();
                for (j = 0; j < SCount; j++) {

                    if (Sections[j].patternCode.equals(patterncode)) {
                        break;
                    }
                }
                Sections[j].shift[position] = dp.getString();
                Sections[j].days[position] = dp.getString();

            }
            tableModel.setData(Sections);
        } catch (Exception e) {
            MyUtils.showException("GetSPatternListList", e);
            tableModel.setData(null);
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getSPatternMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
