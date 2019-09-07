package lib.gui.table;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
/**
 */
public abstract class MyTableModel extends AbstractTableModel{
    private static final int MAX_COLUMNS = 100;
    public int rowCount = 0;
    public int numColumns = 0;
    private MyTableColumn[] myColumns = new MyTableColumn[MAX_COLUMNS];
    private MyTable table;
    private int sortedColIndex = -1;
    /**
     *
     */
    public MyTableModel(){
        super();
    }
    /**
     *
     * @param table
     */
    public void setCellRenderersAndEditors(MyTable table){
        this.table = table;
        for (int i = 0; i < getColumnCount(); i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            myColumns[i].setHeaderRenderer(column);
            myColumns[i].setWidth(column);
            myColumns[i].setCellRenderer(column);
            myColumns[i].setCellEditor(column);
        }
    }
    @Override
    public String getColumnName(int col){
        return myColumns[col].getColumnName();
    }
    /**
     *
     * @param row
     * @return
     */
    public boolean isRowSelected(int row){
        return table.isRowSelected(row);
    }
    /**
     *
     * @return
     */
    public int getSortedColIndex() {
        return sortedColIndex;
    }
    /**
     *
     * @param sortedColIndex
     */
    public void setSortedColIndex(int sortedCol) {
        this.sortedColIndex = sortedCol;
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return myColumns[col].isEditable();
    }
    @Override
    public int getColumnCount() {
        return numColumns;
    }
    @Override
    public int getRowCount() {
        return rowCount;
    }
    /**
     *
     * @param col
     * @return
     */
    public int getColType(int col) {
         return myColumns[col].type;
    }

    @Override
    public Object getValueAt(int row, int col) {
       return myColumns[col].getValueAt(row);
    }

    @Override
    public void setValueAt(Object value, int row, int col){
        myColumns[col].setValueAt(row, value);
    }

    /**
     * 
     * @return
     */
    public void  addColumn(MyTableColumn tableColumn) throws Exception{
        if(numColumns >= MAX_COLUMNS){
            throw new Exception("MAX_COL_ALLOWED = "+MAX_COLUMNS);
        }
        tableColumn.setColIndex(numColumns);
        tableColumn.setTableModel(this);
        myColumns[numColumns++] = tableColumn;
    }
    /**
     * 
     * @param col
     */
    public void sortTableRows(int col) {
        try {
            MyColumnSorter.sortColumn(this, myColumns[col]);
            fireTableStructureChanged();
            setCellRenderersAndEditors(table);
            table.validate();
            table.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 
     * @param index0
     * @param index1
     */
    public void setSelectionInterval(int index0, int index1){
        if(getRowCount()>index1){
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
    }
    /**
     * Exchange data of row1 and row2
     * @param row1
     * @param row2
     */
    public abstract void exchange(int row1, int row2);
    /**
     * Override this method if you implements filter option
     * @param text
     */
    public void filter(String text){
    }
    /**
     * Override this method in your TM implementation
     * @param row
     * @return
     */
    public Object getDataObject(int row){
        return null;
    }
}