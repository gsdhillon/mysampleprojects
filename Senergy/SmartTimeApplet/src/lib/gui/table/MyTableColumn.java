package lib.gui.table;

import java.io.Serializable;
import javax.swing.table.TableColumn;
/**
 */
public abstract class MyTableColumn  implements Serializable{
    public static final int TYPE_DO_NOT_SORT = 0;
    public static final int TYPE_INT = 1;//long
    public static final int TYPE_STRING = 2;//String
    public static final int TYPE_DATE_SHORT = 3;//java.util.date
    public static final int TYPE_DATE_FULL = 4;//java.util.date
    public static final int TYPE_BIG_NUM = 5;//java.math.BigInteger
    public static final int TYPE_FLOAT = 6;//float
    public static final int TYPE_CHECK_BOX = 7;//JCheckBox
    //
    public static final int SORTED_NOT = 11;
    public static final int SORTED_ASCENDING = 12;
    public static final int SORTED_DESCENDING = 13;
    //
    public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_FULL = "dd/MM/yyyy HH:mm:ss";
    private String name;
    private int width;
    public int type;
    public int sortedType = SORTED_NOT;
    private MyTableModel tableModel;
    private int colIndex = -1;
    private boolean isEditable = false;
    /**
     * @param name
     * @param width
     * @param type
     */
    public MyTableColumn(String name, int width, int type){
        this.name = name;
        this.width = width;
        this.type = type;
    }
    /**
     * @param name
     * @param width
     * @param type
     */
    public MyTableColumn(String name, int width, int type, boolean isEditable){
        this.name = name;
        this.width = width;
        this.type = type;
        this.isEditable = isEditable;
    }
    /**
     * 
     * @return
     */
    public boolean isEditable() {
        return isEditable;
    }
    /**
     *
     * @return
     */
    public int getColIndex() {
        return colIndex;
    }
    /**
     * 
     * @param colIndex
     */
    public void setColIndex(int colIndex){
        this.colIndex = colIndex;
    }
    /**
     *
     * @param colIndex
     */
    public void setTableModel(MyTableModel tableModel){
        this.tableModel = tableModel;
    }
    /**
     * 
     * @param column
     */
    public void setWidth(TableColumn column) {
        column.setPreferredWidth(width);
    }
    /**
     *
     * @return
     */
    public int getType(){
        return type;
    }
    /**
     *
     * @return
     */
    public int getSortedType(){
        return sortedType;
    }
    /**
     * 
     * @param sortedType
     */
    public void setSortedType(int sortedType){
        this.sortedType = sortedType;
    }
    /**
     * 
     * @return
     */
    public String getColumnName(){
        if(colIndex != tableModel.getSortedColIndex()){
            return name;
        }else if(sortedType == SORTED_ASCENDING){
            return name + " ↓";
        }else if(sortedType == SORTED_DESCENDING){
            return name + " ↑";
        }else{
            return name + " -";
        } 
    }
    /**
     *
     * @param row
     * @return
     */
    public abstract Object getValueAt(int row);
    /**
     * Override this method for editable cell
     * @param row
     * @param value
     */
    public void setValueAt(int row, Object value){}
    /**
     *
     * @param column
     */
    public void setHeaderRenderer(TableColumn column) {
        column.setHeaderRenderer(new MyHeaderRenderer());
    }
    /**
     * Override this method for editable cell
     * @param column
     */
    public void setCellRenderer(TableColumn column){}
    /**
     * Override this method for editable cell
     * @param column
     */
    public void setCellEditor(TableColumn column){}
}