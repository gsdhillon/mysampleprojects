/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.services.holiday;

import lib.session.MyHTTP;
import lib.session.MyApplet;
import lib.session.MyUtils;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.utils.Depacketizer;
import lib.utils.MyDate;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;

/**
 *
 * @author GAURAV
 */
public class Holiday extends MyApplet implements ActionListener {

    private HolidayMaster p;

    @Override
    public void init() {
        try {
            super.init();
            setLayout(new BorderLayout());
            p = new HolidayMaster();
            selectionEvents(p);
            MyPanel panel = new MyPanel(new GridLayout(1, 5));
            getHolidayMasterList();
            MyButton b2 = new MyButton("Add Holiday") {

                @Override
                public void onClick() {
                    addHoliday();
                }
            };
            panel.add(b2);

            MyButton b3 = new MyButton("Update Holiday") {

                @Override
                public void onClick() {
                    updateHoliday();
                }
            };
            panel.add(b3);

            MyButton b4 = new MyButton("Delete Holiday") {

                @Override
                public void onClick() {
                    if (MyUtils.confirm("Do you want to DELETE ?", "DELETE")) {
                        deleteHoliday();
                    } else {
                        return;
                    }

                }
            };
            panel.add(b4);
            panel.add(new MyLabel(MyLabel.TYPE_LABEL, " Search Holiday:"));
            MyTextField searchTextName = new MyTextField();
            panel.add(searchTextName);
            add(panel, BorderLayout.NORTH);
            //create Documents table
            p.table.addSearchField(1, searchTextName, MyTable.SEARCH_TYPE_CONTAINS);
            add(p, BorderLayout.CENTER);
            getHolidayMasterList();
        } catch (Exception e) {
            MyUtils.showException("UserMaster", e);
        }
    }

    private void selectionEvents(HolidayMaster p) {

        p.btnAdd.addActionListener(this);
        p.btnAdd.setActionCommand("add");
        p.btnCancel.addActionListener(this);
        p.btnCancel.setActionCommand("Cancel");
    }

    private void addHoliday() {
        p.txtHolidayNm.setEditable(true);
        p.txtHolidayNm.setText("");
        ((JComponent) p.picker).setEnabled(true);
        ((JDateComponent) p.picker).getModel().setSelected(false);
        p.btnAdd.setEnabled(true);
        p.btnAdd.setText("AddHoliday");
        p.btnAdd.setActionCommand("AddHoliday");
    }

    private void updateHoliday() {
        int row = p.table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Holiday Selected");
            return;
        }
        p.txtHolidayNm.setEditable(true);
        String date = (String) p.table.getValueAt(row, 0);
        String name = (String) p.table.getValueAt(row, 1);
        p.txtHolidayNm.setText(name);
        DateModel model = ((JDateComponent) p.picker).getModel();
        ((JComponent) p.picker).setEnabled(false);
        model.setDate(Integer.parseInt(date.substring(6, 10)),
                Integer.parseInt(date.substring(3, 5)) - 1, Integer.parseInt(date.substring(0, 2)));
        model.setSelected(true);
        p.btnAdd.setText("Update Holiay");
        p.btnAdd.setEnabled(true);
        p.btnAdd.setActionCommand("UpdateHoliday");

    }

    private void deleteHoliday() {
        int row = p.table.getSelectedRow();
        if (row == -1) {
            MyUtils.showMessage("No Holiday Selected");
            return;
        }
        if (MyUtils.confirm("Are You Sure !!", "DELETE")) {
            String id = (String) p.table.getValueAt(row, 0);
            String Date = sqlDate(id);
            try {

                MyHTTP myHTTP = MyUtils.createServletConnection("HolidayFormServlet");
                myHTTP.openOS();
                myHTTP.println("DeleteHoliday");
                myHTTP.println(Date);
                myHTTP.closeOS();
                myHTTP.openIS();
                String result = myHTTP.readLine();
                myHTTP.closeIS();

                if (result.startsWith("Error")) {
                    MyUtils.showMessage(result);
                }
                getHolidayMasterList();
                p.txtHolidayNm.setText("");
                ((JDateComponent) p.picker).getModel().setSelected(false);

            } catch (Exception ex) {
                MyUtils.showException("Delete Holiday", ex);
            }
        }
    }

    public String sqlDate(String id) {
        String date;
        date = id.substring(6, 10) + "-" + id.substring(3, 5) + "-" + id.substring(0, 2);
        return date;
    }

    /**
     *
     */
    private void getHolidayMasterList() {
        try {
            MyHTTP myHTTP = MyUtils.createServletConnection("HolidayFormServlet");
            myHTTP.openOS();
            myHTTP.println("getHolidayList");
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

            HolidayClass[] Holidays = new HolidayClass[rowCount];
            //get all docs
            for (int i = 0; i < rowCount; i++) {

                Holidays[i] = new HolidayClass();
                Holidays[i].HolidayDate = new MyDate(d.getString(), MyDate.FORMAT_DD_MM_YYYY);
                Holidays[i].HolidayName = d.getString();
            }
            p.tableModel.setData(Holidays);
        } catch (Exception e) {
            MyUtils.showMessage("Error: " + e);
            MyUtils.showException("GetHolidayList", e);
            p.tableModel.setData(null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("AddHoliday")) {

            if (FormFilled(p)) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("HolidayFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("AddHoliday");
                    myHTTP.println(CreatePacket(p));
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.closeIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else if (result.equals("Inserted")) {
                        p.txtHolidayNm.setText("");
                        ((JDateComponent) p.picker).getModel().setSelected(false);
                        p.txtHolidayNm.setEnabled(false);
                        ((JComponent) p.picker).setEnabled(false);
                        p.btnAdd.setText("submit");
                        p.btnAdd.setEnabled(false);
                        getHolidayMasterList();
                    }
                } catch (Exception ex) {

                    Logger.getLogger(Holiday.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getActionCommand().equals("UpdateHoliday")) {
            if (FormFilled(p)) {
                try {
                    MyHTTP myHTTP = MyUtils.createServletConnection("HolidayFormServlet");
                    myHTTP.openOS();
                    myHTTP.println("UpdateHoliday");
                    myHTTP.println(CreatePacket(p));
                    myHTTP.closeOS();
                    myHTTP.openIS();
                    String result = myHTTP.readLine();
                    myHTTP.openIS();
                    if (result.startsWith("ERROR")) {
                        MyUtils.showMessage(result);
                    } else if (result.equals("Updated")) {
                        p.txtHolidayNm.setText("");
                        ((JDateComponent) p.picker).getModel().setSelected(false);
                        p.txtHolidayNm.setEditable(false);
                        ((JComponent) p.picker).setEnabled(false);
                        p.btnAdd.setText("submit");
                        p.btnAdd.setEnabled(false);
                        getHolidayMasterList();
                    }
                } catch (Exception ex) {

                    Logger.getLogger(Holiday.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            p.txtHolidayNm.setText("");
            p.txtHolidayNm.setEditable(false);
            ((JDateComponent) p.picker).getModel().setSelected(false);
            ((JComponent) p.picker).setEnabled(false);
            p.btnAdd.setText("submit");
            p.btnAdd.setEnabled(false);

        } else {
            MyUtils.showMessage(e.getActionCommand());
        }

    }

    public boolean FormFilled(HolidayMaster p) {
        if (p.txtHolidayNm.getText().equals("") || (!((JDateComponent) p.picker).getModel().isSelected())) {
            MyUtils.showMessage("Form Not Filled");
            return false;
        }
        return true;
    }

    public String CreatePacket(HolidayMaster p) throws Exception {
        Packetizer a = new Packetizer();
        a.addString(p.txtHolidayNm.getText());
        DateModel model = ((JDateComponent) p.picker).getModel();
        int month = model.getMonth() + 1;
        String date = model.getYear() + "-" + month + "-" + model.getDay();
        a.addString(date);
        return a.getPacket();
    }
}
