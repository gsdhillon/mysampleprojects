package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * @type     : Java Class
 * @name     : MyTable
 * @file     : MyTable.java
 * @created  : Aug 13, 2010 2:07:43 PM
 * @version  : 1.2
 */
public class MyTable extends JTable{
    public static final int EXACT_MATCH = 0;
    public static final int CONTAINS = 1;
    public static final int STARTS_WITH = 2;
    private MyTextField[] searchText;
    private MyTableModel tableModel;
    /**
     * Constructor
     */
    public MyTable(MyTableModel tableModel){

        super(tableModel);
        this.tableModel = tableModel;
        searchText = new MyTextField[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++){
            TableColumn column = getColumnModel().getColumn(i);
            column.setPreferredWidth(tableModel.getColumnWidth(i));
        }
        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(getPreferredSize().width, 20));
        header.setUpdateTableInRealTime(true);
        header.addMouseListener(listener);
        header.setReorderingAllowed(true);
        header.setBackground(Color.RED);
        header.setForeground(Color.BLUE);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        ListSelectionModel lsm = getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setRowHeight(20);
        setFont(new Font("Arial", Font.PLAIN, 11));
    }
    //sort table data on the column ON MouseClick
    MyMouseAdaptor listener = new MyMouseAdaptor() {
        @Override
        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            tableModel.sortTableRows(columnModelIndex);
        }
    };

    
    public void addSearchField(final int col, MyTextField textField, final int searchType){
        searchText[col] = textField;
        textField.addKeyListener(new MyKeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '\n'){
                    searchNext(searchText[col].getText(), col, searchType);
                }
            }
        });
    } 

    /**
     * @param table - JTable
     * @param searchString - not case sensitive
     * @param searchCol - col number of the table
     * @param searchType - 0 exact match, 1 containing, 2 startsWith
     */
    public void searchNext(String string, int col, int type) {
        if(getRowCount()==0){
            //System.out.println("row count 0");
            return;
        }
        int selectedRow = getSelectionModel().getMinSelectionIndex();
        if(selectedRow == -1){
            selectedRow = 0;
        }
        int numRows = getRowCount();
        string = string.trim().toLowerCase();
        boolean found = false;
        int count = 0;
        int i = (selectedRow+1)%numRows;
        while(count<numRows){
            String targateString = (String)getValueAt(i, col);
            targateString = targateString.toLowerCase();
            if(type == EXACT_MATCH){//t = s
                if(targateString.equals(string)){
                    found = true;
                }
            }else if(type == CONTAINS){//t contains s
                if(targateString.indexOf(string) != -1){
                    found = true;
                }
            }else if(type == STARTS_WITH){//t starts with s
                if(targateString.startsWith(string)){
                    found = true;
                }
            }
            if(found){
                getSelectionModel().setSelectionInterval(i, i);
                Rectangle rect = getCellRect(i, 0, true);
                scrollRectToVisible(rect);
                break;//while loop
            }
            i = (i+1)%numRows;
            count++;
        }
    }
}