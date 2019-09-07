/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.KeySetting;

import SmartTimeApplet.reader.ReaderMaster.RMTableModel;
import SmartTimeApplet.reader.ReaderMaster.ReaderMasterClass;
import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class KeySettingApplet extends MyApplet {

    private RMTableModel tableModel;
    private MyTable table;
    private Container contentPane;
    private MyPanel homePanel;
    private int counter = 0;

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
        MyPanel p = new MyPanel(new FlowLayout(FlowLayout.LEFT));

        getReaderMasterList();
        MyButton b = new MyButton("Key Setting", 1) {

            @Override
            public void onClick() {
                setKeyForSelectedReaders();
            }
        };
        p.add(b);

        p.add(new MyLabel(MyLabel.TYPE_LABEL, " Search Reader:"));
        MyTextField searchTextName = new MyTextField();
        searchTextName.setPreferredSize(new Dimension(150, 25));
        p.add(searchTextName);
        ret.add(p, BorderLayout.NORTH);
        //create Documents table
        table = new MyTable(tableModel);

        table.addSearchField(0, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
        ret.add(table.getGUI(), BorderLayout.CENTER);
        return ret;
    }

    private void setKeyForSelectedReaders() {
        int row = table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Reader Selected");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        new KeySettingDialog(this, getSelectedReader(), counter).setVisible(true);
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

    private String getSelectedReader() {
        Packetizer p = new Packetizer();
        int count = 0;
        counter = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            try {
                if (table.isRowSelected(i)) {
                    p.addInt(Integer.parseInt(table.getValueAt(i, 0).toString()));
                    counter++;
                }
            } catch (Exception ex) {
                MyUtils.showException("getSelectedReader", ex);
            }
        }
        return p.getPacket();
    }
}
