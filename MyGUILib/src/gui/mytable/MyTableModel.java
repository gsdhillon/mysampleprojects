package gui.mytable;
import gui.MyLog;
import java.io.File;
import java.io.Serializable;
import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.write.*;
/**
 */
public abstract class MyTableModel extends AbstractTableModel implements Serializable{
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
    @SuppressWarnings("CallToThreadDumpStack")
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
    
    @SuppressWarnings("UseSpecificCatch")
    public void exportJTable(File file, String headerText, int pageorient) {
        try {
            MyTableModel tableModel = this;
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            String name = file.getName();
            WritableSheet sheet = workbook.createSheet(name.substring(0, name.indexOf(".")), 0);
            //Page Setup
            SheetSettings s = sheet.getSettings();
            s.setPaperSize(PaperSize.A4);
            if (pageorient == 1) {
                s.setOrientation(PageOrientation.PORTRAIT);
            } else {
                s.setOrientation(PageOrientation.LANDSCAPE);
            }
            s.setHeaderMargin(0.25f);
            s.setFooterMargin(0.25f);
            s.setTopMargin(0.5f);
            s.setBottomMargin(0.5f);
            s.setLeftMargin(0.75f);
            s.setRightMargin(0.75f);
            s.setHorizontalCentre(true);
            //Header
            HeaderFooter header = new HeaderFooter();
            HeaderFooter.Contents c = header.getLeft();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            c.toggleBold();
            c.append(headerText);
            c = header.getRight();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            c.toggleBold();
            c.append("Generated on ");
            c.appendDate();
            c.append(" Number of records ");
            c.append(String.valueOf(tableModel.getRowCount()));
            sheet.getSettings().setHeader(header);

            //Column Headers
            WritableFont fontHeader = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
            WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
            formatHeader.setBackground(Colour.LIGHT_GREEN);
            formatHeader.setAlignment(Alignment.CENTRE);
            formatHeader.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_50_PERCENT);
            Label label;
            CellView colView = new CellView();
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                colView.setAutosize(true);
                sheet.setColumnView(col, colView);
                label = new Label(col, 0, (String) tableModel.getColumnName(col), formatHeader);
                sheet.addCell(label);
            }
            //Rows
            WritableFont fontData = new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD);
            WritableCellFormat formatData = new WritableCellFormat(fontData);
            formatData.setVerticalAlignment(VerticalAlignment.CENTRE);
            formatData.setAlignment(Alignment.CENTRE);
            formatData.setWrap(true);
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object obj = tableModel.getValueAt(row, col);
                    String text = "";
                    if (obj instanceof JCheckBox) {
                        text = ((JCheckBox) obj).isSelected() ? "T" : "F";
                    } else {
                        text = obj.toString();
                    }
                    label = new Label(col, row + 1, text, formatData);
                    sheet.addCell(label);
                }
            }
            //Footer
            HeaderFooter footer = new HeaderFooter();
            c = footer.getRight();
            c.setFontName("TAHOMA");
            c.setFontSize(8);
            c.append("Page ");
            c.appendPageNumber();
            c.append(" of ");
            c.appendTotalPages();
            sheet.getSettings().setFooter(footer);
            workbook.write();
            workbook.close();
            MyLog.showInfoMsg("Report saved at:\n" + file.getAbsolutePath());
        }catch (Exception e) {
            MyLog.showErrorMsg("Export Failed : " + e.getMessage());
        }
    }
}