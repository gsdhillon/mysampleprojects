/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.ExceptionEntryApplication;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 */
public class ExceptionEntryApplet extends MyApplet {

    Container contentPane;
    private MyPanel homePanel;
    ExceptionEntryTM tableModel;
    MyTable table;

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
        tableModel = new ExceptionEntryTM();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getApplicationDetailsList();
        MyButton b2 = new MyButton("Add") {

            @Override
            public void onClick() {
                addExceptionEntry();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update") {

            @Override
            public void onClick() {
                updateExceptionEntry();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Cancel") {

            @Override
            public void onClick() {
                cancelExceptionEntry();
            }
        };
        p.add(b4);
        ret.add(p, BorderLayout.NORTH);
        table = new MyTable(tableModel);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addExceptionEntry() {
        contentPane.removeAll();
        contentPane.add(new ExceptionEntry(this, "Add", ""));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateExceptionEntry() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }
        String appid = table.getValueAt(row, 0).toString();
        contentPane.removeAll();
        contentPane.add(new ExceptionEntry(this, "Update", appid));
        contentPane.validate();
        contentPane.repaint();
    }

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getApplicationDetailsList();
        contentPane.validate();
        contentPane.repaint();
    }

    public void getApplicationDetailsList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
            myHTTP.openOS();
            myHTTP.println("getApplicationDetailsList");
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            }
            Depacketizer dp = new Depacketizer(response);
            int rowCount = dp.getInt();
            ExceptionEntryClass[] exc = new ExceptionEntryClass[rowCount];

            for (int i = 0; i < rowCount; i++) {
                exc[i] = new ExceptionEntryClass();
                exc[i].appid = dp.getString();
                exc[i].appdate = dp.getString();
                exc[i].prein = dp.getString();//coff date 
                exc[i].currin = dp.getString();//worked date
                exc[i].preout = dp.getString();
                exc[i].currout = dp.getString();
                exc[i].presatus = dp.getString();
                exc[i].currstatus = dp.getString();
            }
            tableModel.setData(exc);
        } catch (Exception e) {
            MyUtils.showException("getApplicationDetailsList", e);
            tableModel.setData(null);
        }
    }

    private void cancelExceptionEntry() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }

        String AppID = (String) table.getValueAt(row, 0);
        if (MyUtils.confirm("Do you want to Cancel " + AppID, "Cancel")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("ExceptionEntryServlet");
                myHTTP.openOS();
                myHTTP.println("cancelExceptionEntry");
                myHTTP.println(AppID);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();
                if (result.startsWith("Error")) {
                    MyUtils.showMessage(result);
                }
                getApplicationDetailsList();
            } catch (Exception ex) {
                MyUtils.showException("Cancele Record", ex);
            }
        }
    }
}
