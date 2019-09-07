package lib.digsign.doc_list;

import SmartTimeApplet.visitor.vis_app.ViewVisitorForm;
import SmartTimeApplet.visitor.vis_app.VisitorForm;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.MyTextField;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;
import lib.session.MyApplet;
import lib.session.MyHTTP;
import lib.session.MyUtils;
import lib.utils.Depacketizer;

/**
 * AppliedForms.java
 */
public class AppliedForms extends MyApplet implements
        ActionListener, DocListInterface {

    private MyPanel buttonsPane;
    private String docType = "";

    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            contentPane.setLayout(new GridLayout(1, 1));
            docListPanel = new MyPanel(new BorderLayout());
            tableModel = new DocumentsTableModel();
            //create search panel
            MyPanel searchPanel = createTopPanel();
            docListPanel.add(searchPanel, BorderLayout.NORTH);
            //create Documents table
            documentsTable = new MyTable(tableModel);
            documentsTable.addSearchField(1, searchTextRefNO, MyTable.SEARCH_TYPE_EXACT_MATCH);
            //create sp
            JScrollPane sp = documentsTable.getGUI();
            sp.setBorder(BorderFactory.createTitledBorder(
                    "List of applied forms"));
            docListPanel.add(sp, BorderLayout.CENTER);
            //create buttons
            buttonsPane = new MyPanel(new GridLayout(1, 1));
            buttonsPane.add(createButtonPane());
            buttonsPane.setBorder(BorderFactory.createTitledBorder("Actions"));
            docListPanel.add(buttonsPane, BorderLayout.SOUTH);
            contentPane.add(docListPanel);
        } catch (Exception e) {
            MyUtils.showException("AppliedForms", e);
        }
    }

    @Override
    public void reLoad(boolean refresh) {
        if (refresh) {
            tableModel.getData();
        }
        contentPane.removeAll();
        contentPane.add(docListPanel);
        contentPane.validate();
        contentPane.repaint();
    }

    private MyPanel createButtonPane() {
        MyPanel buttonPane = new MyPanel(new GridLayout(1, 0));
        //Download
        JButton b = new JButton("Download");
        b.addActionListener(this);
        b.setActionCommand("download");
        buttonPane.add(b);
        //View/Edit
        b = new JButton("View/Edit");
        b.addActionListener(this);
        b.setActionCommand("view");
        buttonPane.add(b);
        //Close
        b = new JButton("Close");
        b.addActionListener(this);
        b.setActionCommand("close");
        buttonPane.add(b);
        return buttonPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "getdocs":
                tableModel.getData();
                break;
            case "download":
                downloadSelectedDoc();
                break;
            case "view":
                viewSelectedDoc();
                break;
            case "close":
                break;
        }
    }

    private void viewSelectedDoc() {
        if (docType == null || docType.equals("")) {
            return;
        }
        int row = documentsTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        String formID = tableModel.getFormID(row);
        MyPanel viewPanel = null;
        if (docType.equals(VisitorForm.shortName)) {
            viewPanel = new ViewVisitorForm(formID, "APPLICANT", this);
        }
        //TODO NEW_APP
        if (viewPanel != null) {
            contentPane.removeAll();
            contentPane.add(viewPanel);
            contentPane.validate();
            contentPane.repaint();
        }
    }

    private void downloadSelectedDoc() {
        if (docType == null || docType.equals("")) {
            return;
        }
        int row = documentsTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        String formID = tableModel.getFormID(row);
        if (docType.equals(VisitorForm.shortName)) {
            try {
                VisitorForm form = VisitorForm.getForm(formID);
                form.saveAtClientPC();
            } catch (Exception ex) {
                MyUtils.showException("DownloadVisitorForm", ex);
            }
        }
        //TODO NEW_APP
    }

    private class DocumentsTableModel extends MyTableModel {

        private String[][] data;

        /**
         */
        public DocumentsTableModel() throws Exception {
            super();
            //Column 0
            addColumn(new MyTableColumn("AppType", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][0];
                }
            });
            ///Column 1
            addColumn(new MyTableColumn("AppID", 100, MyTableColumn.TYPE_INT) {

                @Override
                public String getValueAt(int row) {
                    return data[row][1];
                }
            });
            //Column 2
            addColumn(new MyTableColumn("ApplyDate", 100, MyTableColumn.TYPE_DATE_SHORT) {

                @Override
                public String getValueAt(int row) {
                    return data[row][2];
                }
            });
            //Column 3
            addColumn(new MyTableColumn("Description", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][3];
                }
            });
            //Column 4
            addColumn(new MyTableColumn("Status", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][4];
                }
            });
        }

        @Override
        public void exchange(int row1, int row2) {
            String[] temp = data[row1];
            data[row1] = data[row2];
            data[row2] = temp;
        }

        public String getStatus(int row) {
            return (String) getValueAt(row, 4);
        }

        public String getFormID(int row) {
            return (String) getValueAt(row, 1);
        }

        @SuppressWarnings("CallToThreadDumpStack")
        public void getData() {
            try {
                rowCount = 0;
                docType = ((String) docTypeCombo.getSelectedItem()).trim();
                MyHTTP myHTTP = MyUtils.createServletConnection("DigSignFormListServlet");
                myHTTP.openOS();
                myHTTP.println("getAppliedForms");
                myHTTP.println(docType);
                myHTTP.println((String) yearCombo.getSelectedItem());
                myHTTP.closeOS();
                myHTTP.openIS();
                String response = myHTTP.readLine();
                myHTTP.closeIS();
                if (response.startsWith("ERROR")) {
                    MyUtils.showMessage(response);
                    return;
                }
                Depacketizer d = new Depacketizer(response);
                rowCount = d.getInt();
                data = new String[rowCount][5];
                //get all docs
                for (int i = 0; i < rowCount; i++) {
                    String type = d.getString();
                    String id = d.getString();
                    String dated = d.getString();
                    String desc = d.getString();
                    String status = d.getString();
                    data[i][0] = type;
                    data[i][1] = id;
                    data[i][2] = dated;
                    data[i][3] = desc;
                    data[i][4] = status;
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyUtils.showException("Get doc list", e);
            } finally {
                tableModel.fireTableDataChanged();
            }
        }
    }//end of table model

    /**
     *
     */
    private MyPanel createTopPanel() throws Exception {
        MyPanel searchPanel = new MyPanel(new GridLayout(1, 2));
        searchPanel.setPreferredSize(new Dimension(800, 50));
        MyPanel p = new MyPanel(new GridLayout(1, 3));
        p.setBorder(BorderFactory.createTitledBorder("Choose type of document"));
        Calendar c = MyUtils.getTodayDate();
        int yyyy = c.get(Calendar.YEAR);
        yearCombo = new JComboBox(new String[]{"" + (yyyy - 1), "" + yyyy, "" + (yyyy + 1)});
        yearCombo.setSelectedIndex(1);
        p.add(yearCombo);
        docTypeCombo = new JComboBox(new String[]{
            VisitorForm.shortName
        });
        //TODO NEW_APP
        p.add(docTypeCombo);
        JButton b = new JButton("GET Docs");
        b.setActionCommand("getdocs");
        b.addActionListener(this);
        p.add(b);
        searchPanel.add(p);
        p = new MyPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(
                "Type Ref_NO press ENTER"));
        MyLabel l = new MyLabel(MyLabel.TYPE_LABEL, " Ref_NO: ");
        //MyDateTools.setLabelForeGround( );
        p.add(l, BorderLayout.WEST);
        p.add(searchTextRefNO, BorderLayout.CENTER);
        searchPanel.add(p);
        return searchPanel;
    }
    //data
    private Container contentPane;
    private MyPanel docListPanel;
    private MyTable documentsTable = null;
    private DocumentsTableModel tableModel = null;
    private JComboBox docTypeCombo = null;
    private JComboBox yearCombo = null;
    private MyTextField searchTextRefNO = new MyTextField();
}
