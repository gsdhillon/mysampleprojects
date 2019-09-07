package mytable.sorting;
import mytable.MyColumnModel;
import mytable.MyTableModel;
/**
 * Created on Feb 23, 2014
 * @version 1.0.0
 */
class SortString extends MergeSort{
    private String[] val;
    private String[] tempVal;
    private String temp;
    /**
     * 
     * @param tableModel
     * @param column 
     */
    public SortString(MyTableModel tableModel, MyColumnModel column){
        super(tableModel, column);
        val = new String[numRows];
        tempVal = new String[numRows];
        for(int i=0;i<numRows;i++){
            try{
                val[i] = (String)column.getValueAt(i);
                if(val[i] == null){
                    val[i] = "";
                }
            }catch(Exception e){
                val[i] = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
            }
        }
    }
    /**
     * @param i
     * @param j
     * @return (val[i] > val[j])
     */
    @Override
    protected boolean isGreater(int i, int j) {
        return (val[i].compareToIgnoreCase(val[j])>0);//compareTo
    }
    /**
     * @param i
     * @param j
     * @return (val[i] < val[j])
     */
    @Override
    protected boolean isLesser(int i, int j) {
        return (val[i].compareToIgnoreCase(val[j])<0);
    }
    /**
     * 
     * @param i
     * @param j 
     */
    @Override
    protected void exchangeValues(int i, int j) {
        //exchange values
        temp = val[i];
        val[i] = val[j];
        val[j] = temp;
    }
    /**
     * 
     * @param t
     * @param iOrJ 
     */
    @Override
    protected void copyInTemp(int t, int iOrJ) {
        tempVal[t] = val[iOrJ];
    }
    /**
     * 
     * @param startIndex
     * @param endIndex 
     */
    @Override
    protected void copyFromTemp(int startIndex, int endIndex) {
        System.arraycopy(tempVal, startIndex, val, startIndex, endIndex-startIndex);
    }
}