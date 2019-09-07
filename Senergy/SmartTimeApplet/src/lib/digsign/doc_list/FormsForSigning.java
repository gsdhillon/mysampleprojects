package lib.digsign.doc_list;

import SmartTimeApplet.visitor.vis_app.SignVisitorForm;
import SmartTimeApplet.visitor.vis_app.VisitorForm;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
 * FormsForSigning.java
 */
public class FormsForSigning extends MyApplet implements
        ActionListener, DocListInterface {
    /**
     * 
     */
    @Override
    public void init() {
        try {
            super.init();
            contentPane = getContentPane();
            contentPane.setLayout(new GridLayout(1, 1));
            docListPanel = new MyPanel(new BorderLayout());
            tableModel = new DocumentsTableModel();

            //create Documents table
            documentsTable = new MyTable(tableModel);
            documentsTable.addSearchField(2, searchTextApplicant, MyTable.SEARCH_TYPE_CONTAINS);
            //create sp
            JScrollPane sp = documentsTable.getGUI();
            sp.setBorder(BorderFactory.createTitledBorder(
                    "List of applied forms"));
            docListPanel.add(sp, BorderLayout.CENTER);

            //create buttons
            MyPanel buttonsPane = new MyPanel(new GridLayout(1, 1));
            buttonsPane.add(createButtonPane());
            buttonsPane.setBorder(BorderFactory.createTitledBorder("Actions"));
            docListPanel.add(buttonsPane, BorderLayout.SOUTH);
            contentPane.add(docListPanel);
            //
            tableModel.getData();
        } catch (Exception e) {
            MyUtils.showException("FormsForSigning", e);
        }
    }

    /**
     */
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

    /**
     */
    private MyPanel createButtonPane() {
        MyPanel buttonPane = new MyPanel(new GridLayout(1, 0));
        //Download
        JButton b = new JButton("OpenForSign");
        b.addActionListener(this);
        b.setActionCommand("openForSign");
        buttonPane.add(b);
        //searchTextApplicant
        buttonPane.add(new MyLabel(MyLabel.TYPE_LABEL, "Applicant: "));
        buttonPane.add(searchTextApplicant);
        //Close
        b = new JButton("Close");
        b.addActionListener(this);
        b.setActionCommand("close");
        buttonPane.add(b);
        return buttonPane;
    }

    /**
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "openForSign":
                openSelectedDocForSign();
                break;
            case "close":
                break;
        }
    }

    /**
     */
    private void openSelectedDocForSign() {
        int row = documentsTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        String formID = tableModel.getFormID(row);
        String docType = tableModel.getDocType(row);
        //
        MyPanel viewPanel = null;
        if (docType.equals(VisitorForm.shortName)) {
            viewPanel = new SignVisitorForm(formID, this);
        }
        //TODO NEW_APP
        if (viewPanel != null) {
            contentPane.removeAll();
            contentPane.add(viewPanel);
            contentPane.validate();
            contentPane.repaint();
        }
    }
    /**
     *
     */
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
            addColumn(new MyTableColumn("Applicant", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][2];
                }
            });
            //Column 3
            addColumn(new MyTableColumn("ApplyDate", 100, MyTableColumn.TYPE_DATE_SHORT) {

                @Override
                public String getValueAt(int row) {
                    return data[row][3];
                }
            });
            //Column 4
            addColumn(new MyTableColumn("Description", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][4];
                }
            });
            //Column 5
            addColumn(new MyTableColumn("AuthRole", 100, MyTableColumn.TYPE_STRING) {

                @Override
                public String getValueAt(int row) {
                    return data[row][5];
                }
            });
        }

        @Override
        public void exchange(int row1, int row2) {
            String[] temp = data[row1];
            data[row1] = data[row2];
            data[row2] = temp;
        }

        /**
         *
         */
        public String getStatus(int row) {
            return (String) getValueAt(row, 4);
        }

        /**
         *
         */
        public String getFormID(int row) {
            return (String) getValueAt(row, 1);
        }

        /**
         */
        @SuppressWarnings("CallToThreadDumpStack")
        public void getData() {
            try {
                rowCount = 0;
                MyHTTP myHTTP = MyUtils.createServletConnection("DigSignFormListServlet");
                myHTTP.openOS();
                myHTTP.println("getDocListForSign");
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
                data = new String[rowCount][6];
                //get all docs
                for (int i = 0; i < rowCount; i++) {
                    String type = d.getString();
                    String id = d.getString();
                    String applicant = d.getString();
                    String dated = d.getString();
                    String desc = d.getString();
                    String authRole = d.getString();
                    data[i][0] = type;
                    data[i][1] = id;
                    data[i][2] = applicant;
                    data[i][3] = dated;
                    data[i][4] = desc;
                    data[i][5] = authRole;
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyUtils.showException("Get doc list", e);
            } finally {
                tableModel.fireTableDataChanged();
            }
        }
        /**
         * 
         * @param row
         * @return 
         */
        private String getDocType(int row) {
            return data[row][0];
        }
    }//end of table model
    //data
    private Container contentPane;
    private MyPanel docListPanel;
    private MyTable documentsTable = null;
    private DocumentsTableModel tableModel = null;
    private MyTextField searchTextApplicant = new MyTextField();
}
