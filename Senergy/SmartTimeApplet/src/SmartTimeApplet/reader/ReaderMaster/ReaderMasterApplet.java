/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderMaster;

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
 * @author pradnya
 */
public class ReaderMasterApplet extends MyApplet {

    private RMTableModel tableModel;
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

    private MyPanel CreateHomePanel() throws Exception {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BorderLayout());
        tableModel = new RMTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getReaderMasterList();
        MyButton b2 = new MyButton("Add Reader") {

            @Override
            public void onClick() {
                addReader();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update Reader") {

            @Override
            public void onClick() {
                updateReader();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Delete Reader") {

            @Override
            public void onClick() {
                deleteReader();
            }
        };
        p.add(b4);

        p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search ReaderNo:"));
        MyTextField searchTextName = new MyTextField();
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addReader() {
        contentPane.removeAll();
        contentPane.add(new ReaderMaster(this, "add", "1"));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateReader() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Reader Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        contentPane.removeAll();
        contentPane.add(new ReaderMaster(this, "update", id));
        contentPane.validate();
        contentPane.repaint();
    }

    private void deleteReader() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No row Selected");
            return;
        }

        String id = (String) table.getValueAt(row, 0);
        if (MyUtils.confirm("Do you want to Delete " + id + " reader", "Delete")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("ReaderMasterFormServlet");
                myHTTP.openOS();
                myHTTP.println("DeleteReader");
                myHTTP.println(id);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();
                if (result.startsWith("Error")) {
                    MyUtils.showMessage(result);
                }
                getReaderMasterList();
            } catch (Exception ex) {
                MyUtils.showException("DeleteRader", ex);
            }
        }
    }

    private void getReaderMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReaderMasterFormServlet");
            myHTTP.openOS();
            myHTTP.println("getReaderList");
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

            ReaderMasterClass[] readers = new ReaderMasterClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                readers[i] = new ReaderMasterClass();
                readers[i].rdrNo = d.getString();
                readers[i].door = d.getString();
                readers[i].div = d.getString();
                readers[i].readerType = d.getString();
                readers[i].selfIP = d.getString();
                readers[i].serverIP = d.getString();
                readers[i].readerZone = d.getString();
            }
            tableModel.setData(readers);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetReadersList", e);
            tableModel.setData(null);
        }
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getReaderMasterList();
        contentPane.validate();
        contentPane.repaint();
    }
}
