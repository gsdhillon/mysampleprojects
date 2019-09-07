/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.COFFApplication;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class COFFApplicationApplet extends MyApplet {

    private COFFTableModel tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;
    public static String compoffAppID;

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
        tableModel = new COFFTableModel();
        MyPanel p = new MyPanel(new GridLayout(1, 5));
        getApplicationDetailsList();
        MyButton b2 = new MyButton("Add") {

            @Override
            public void onClick() {
                addCompOffPanel();
            }
        };
        p.add(b2);

        MyButton b3 = new MyButton("Update") {

            @Override
            public void onClick() {
                updateCompOff();
            }
        };
        p.add(b3);

        MyButton b4 = new MyButton("Cancel") {

            @Override
            public void onClick() {
                cancelCompOff();
            }
        };
        p.add(b4);

//        p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search ReaderNo:"));
//        MyTextField searchTextName = new MyTextField();
//        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);
//        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void addCompOffPanel() {
        contentPane.removeAll();
        contentPane.add(new COFFAplication(this, "Add", tableModel, ""));
        contentPane.validate();
        contentPane.repaint();
    }

    private void updateCompOff() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }
        String Packet = createUpdatePacket(row);
        contentPane.removeAll();
        contentPane.add(new COFFAplication(this, "Update", tableModel, Packet));
        contentPane.validate();
        contentPane.repaint();
    }

    private void cancelCompOff() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Record Selected");
            return;
        }

        String CFID = (String) table.getValueAt(row, 0);
        if (MyUtils.confirm("Do you want to Cancel " + CFID, "Cancel")) {
            try {
                MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
                myHTTP.openOS();
                myHTTP.println("cancelCFApplication");
                myHTTP.println(CFID);
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

    public void showHomePanel() {
        contentPane.removeAll();
        contentPane.add(homePanel);
        getApplicationDetailsList();
        contentPane.validate();
        contentPane.repaint();
    }

    public void getApplicationDetailsList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("CoffFormServlet");
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
            CoffClass[] coff = new CoffClass[rowCount];

            for (int i = 0; i < rowCount; i++) {
                coff[i] = new CoffClass();
                coff[i].coffentryid = dp.getString();
                coff[i].appdate = dp.getString();
                coff[i].coffdate = dp.getString();//coff date 
                coff[i].workeddate = dp.getString();//worked date
                coff[i].purpose = dp.getString();
                coff[i].status = dp.getString();
            }
            tableModel.setData(coff);
        } catch (Exception e) {
            MyUtils.showException("getApplicationDetailsList", e);
            tableModel.setData(null);
        }
    }

    public String createUpdatePacket(int row) {
        Packetizer p = new Packetizer();
        try {
            String CFID = tableModel.getValueAt(row, 0).toString();
            String CFDate = tableModel.getValueAt(row, 2).toString();
            String workeddate = tableModel.getValueAt(row, 3).toString();
            String purpose = tableModel.getValueAt(row, 4).toString();
            p.addString(CFID);
            p.addString(CFDate);
            p.addString(workeddate);
            p.addString(purpose);
        } catch (Exception ex) {
            Logger.getLogger(COFFApplicationApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p.getPacket();

    }
}
