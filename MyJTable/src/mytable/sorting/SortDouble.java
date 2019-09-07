package mytable.sorting;
import mytable.MyColumnModel;
import mytable.MyTableModel;
/**
 * Created on Feb 23, 2014
 * @version 1.0.0
 */
class SortDouble extends MergeSort{
    private double[] val;
    private double[] tempVal;
    private double temp;
    /**
     * 
     * @param tableModel
     * @param column 
     */
    public SortDouble(MyTableModel tableModel, MyColumnModel column){
        super(tableModel, column);
        val = new double[numRows];
        tempVal = new double[numRows];
        for(int i=0;i<numRows;i++){
            try{
                val[i] = Double.parseDouble((String)column.getValueAt(i));
            }catch(Exception e){
                val[i] = Double.MAX_VALUE;
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
        return (val[i] > val[j]);
    }
    /**
     * @param i
     * @param j
     * @return (val[i] < val[j])
     */
    @Override
    protected boolean isLesser(int i, int j) {
        return (val[i] < val[j]);
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