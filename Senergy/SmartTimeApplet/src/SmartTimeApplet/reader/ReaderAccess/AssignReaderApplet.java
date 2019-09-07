/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reader.ReaderAccess;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import lib.Utility.SimpleUtilities;
import lib.gui.MyButton;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;

/**
 *
 * @author pradnya
 */
public class AssignReaderApplet extends MyApplet implements ActionListener {

    private Container contentsPane;
    JComboBox cmbAccessLevel;
    MyTable tlbReader;
    ReaderAccTableModel tableModel;
    MyButton btnEdit, btnSave, btnCancel;
    MyPanel MainPanel;

    @Override
    public void init() {
        super.init();
        AssignReader();
    }

    public void AssignReader() {
        try {
            MainPanel = new MyPanel(new FlowLayout());
            MyPanel assnedRdr = new MyPanel(new BorderLayout());
            MyPanel pancmbaccess = new MyPanel(new FlowLayout());

            cmbAccessLevel = new JComboBox(SimpleUtilities.fillReaderAccessCombo());
            cmbAccessLevel.setPreferredSize(new Dimension(120, 25));
            cmbAccessLevel.addActionListener(this);
            cmbAccessLevel.setActionCommand("GetAccessString");
            pancmbaccess.add(cmbAccessLevel);
            assnedRdr.add(pancmbaccess, BorderLayout.NORTH);
            MyPanel pantable = new MyPanel(new BorderLayout());
            tableModel = new ReaderAccTableModel();
            getReaderList();
            tlbReader = new MyTable(tableModel);

            tlbReader.setEnabled(false);
            pantable.add(tlbReader.getGUI(), BorderLayout.CENTER);
            assnedRdr.add(pantable, BorderLayout.CENTER);

            pantable.setEnabled(false);
            MyPanel panButtons = new MyPanel(new FlowLayout());

            btnEdit = new MyButton("Edit", 1) {

                @Override
                public void onClick() {
                    tlbReader.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnSave.setEnabled(true);
                    btnEdit.setEnabled(false);
                }
            };
            btnEdit.setEnabled(false);
            panButtons.add(btnEdit);
            btnSave = new MyButton("Save", 1) {

                @Override
                public void onClick() {
                    saveReaderAccess();
                    tlbReader.setEnabled(false);
                    btnCancel.setEnabled(false);
                    btnEdit.setEnabled(true);
                    btnSave.setEnabled(false);
                }
            };
            btnSave.setEnabled(false);
            panButtons.add(btnSave);
            btnCancel = new MyButton("Cancel", 1) {

                @Override
                public void onClick() {
                    clearAllCheck();
                    getAssignedReaderString(cmbAccessLevel.getSelectedIndex());
                    tlbReader.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnEdit.setEnabled(true);
                    btnCancel.setEnabled(false);
                }
            };
            btnCancel.setEnabled(false);
            panButtons.add(btnCancel);
            assnedRdr.add(panButtons, BorderLayout.SOUTH);

            MainPanel.add(assnedRdr);
            contentsPane = getContentPane();
            contentsPane.setLayout(new BorderLayout());
            contentsPane.add(MainPanel, BorderLayout.CENTER);
            setVisible(true);
        } catch (Exception ex) {
            MyUtils.showException("AssignReader", ex);
            tableModel.setData(null);
        }
    }

    private void getReaderList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReaderAccessServlet");
            myHTTP.openOS();
            myHTTP.println("GetReaders");
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

            Assignreaderclass[] readers = new Assignreaderclass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {
                readers[i] = new Assignreaderclass();
                readers[i].readerno = d.getString();
                readers[i].readerloc = d.getString();
                readers[i].readerIP = d.getString();
            }
            tableModel.setData(readers);
        } catch (Exception e) {
            MyUtils.showException("GetReadersListDataSync", e);
            tableModel.setData(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("GetAccessString")) {
            if (cmbAccessLevel.getSelectedIndex() != 0) {
                clearAllCheck();
                btnEdit.setEnabled(true);
                getAssignedReaderString(cmbAccessLevel.getSelectedIndex());
            } else {
                clearAllCheck();
                btnEdit.setEnabled(false);
            }
        }
    }

    private void saveReaderAccess() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReaderAccessServlet");
            myHTTP.openOS();
            myHTTP.println("SaveReaderAccess");
            myHTTP.println("" + cmbAccessLevel.getSelectedIndex());
            myHTTP.println(getSelectedReaders());
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            } else {
                MyUtils.showMessage("Saved Successfully");
            }
        } catch (Exception e) {
            MyUtils.showException("getAssignedReaderString", e);
        }
    }

    private void getAssignedReaderString(int index) {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("ReaderAccessServlet");
            myHTTP.openOS();
            myHTTP.println("GetAssignedReaderString");
            myHTTP.println("" + index);
            myHTTP.closeOS();
            myHTTP.openIS();
            String response = myHTTP.readLine();
            myHTTP.closeIS();
            if (response.startsWith("ERROR")) {
                MyUtils.showMessage(response);
                return;
            } else {
                if ((!"None".equals(response)) & (!"".equals(response)) & (response != null) & (!"null".equals(response))) {
                    String reader = response;
//                    MyUtils.showMessage(" reader :" + reader);
                    if ((reader != null) & (!"".equals(reader))) {
                        String assign_reader[] = reader.split(" ");
                        for (int i = 0; i < assign_reader.length; i++) {
                            tableModel.setSelectedRow(Integer.parseInt(assign_reader[i]) - 1);
                        }
                    }
                    tableModel.fireTableDataChanged();
                }
            }
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("getAssignedReaderString", e);
        }
    }

    private String getSelectedReaders() {
        int totalreader = tableModel.getRowCount();
        String selReader = "";

        for (int i = 0; i < totalreader; i++) {
            if (tableModel.isRowChecked(i)) {
                if (i == 0) {
                    selReader = (tableModel.getValueAt(i, 1)).toString();
                } else {
                    selReader = selReader + " " + tableModel.getValueAt(i, 1);
                }
            }
        }
        return selReader;
    }

    private void clearAllCheck() {
        int totalreader = tableModel.getRowCount();
        for (int i = 0; i < totalreader; i++) {
            tableModel.setUnSelectedRow(i);
        }
        tableModel.fireTableDataChanged();
    }
}
