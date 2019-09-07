package lib.demos.popup_search;

import lib.demos.SampleData;
import lib.gui.table.MyTableColumn;
import lib.gui.table.MyTableModel;

/**
 * TODO MANOJ copy this class for making table model
 * for Search popup. Change Sample data to your Data Class
 * and change the name filed to your desired search field
 *
 */
public class PopupSearchTM extends MyTableModel{
    //use filtered data evry where for table use
    private SampleData[] allData;
    private SampleData[] filteredData;
    public PopupSearchTM() throws Exception{
        super();
        ///Column 0
        addColumn(new MyTableColumn("Name", 100, MyTableColumn.TYPE_STRING){
            @Override
            public String getValueAt(int row) {
                return filteredData[row].name;
            }
        });
    }
    /**
     *
     * @param row1
     * @param row2
     */
    @Override
    public void exchange(int row1, int row2) {
        SampleData temp = filteredData[row1];
        filteredData[row1] = filteredData[row2];
        filteredData[row2] = temp;
    }
    /**
     *
     * @param certInfo
     */
    public void setData(SampleData[] data) {
        if(data == null){
            this.allData = null;
            this.filteredData = null;
            rowCount = 0;
        }else{
            this.allData = data;
            this.filteredData = new SampleData[allData.length];
            System.arraycopy(allData, 0, filteredData, 0, allData.length);
            rowCount = data.length;
        }
        fireTableDataChanged();
    }
    /**
     *
     * @param text
     */
    @Override
    public void filter(String text){
        if(text == null || text.equals("")){
            System.arraycopy(allData, 0, filteredData, 0, allData.length);
            rowCount = allData.length;
        }else{
            text = text.toLowerCase();
            //System.out.println("Filter starts - "+text);
            //System.out.print("Matching rows: ");
            //filter matching rows
            int count = 0;
            for(int i=0; i<allData.length; i++){
                String rowText = allData[i].name.toLowerCase();
                if(rowText.startsWith(text)){
                    filteredData[count++] = allData[i];
                    System.out.print(rowText+", ");
                }
            }
            rowCount = count;
            System.out.println();
        }
        fireTableDataChanged();
        setSelectionInterval(0, 0);
    }
    /**
     * 
     * @param i
     * @return
     */
    @Override
    public Object getDataObject(int i) {
        return filteredData[i];
    }
}