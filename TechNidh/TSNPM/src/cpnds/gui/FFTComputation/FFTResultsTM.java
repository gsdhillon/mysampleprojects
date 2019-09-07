package cpnds.gui.FFTComputation;

import cpnds.gui.ADCAcquisition.*;
import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : FFTResultsTM
 * @file     : FFTResultsTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class FFTResultsTM extends MyTableModel{
    private FFTParams[] fftParams;
    private ADCAcqParams[] adcAcqParams;
    private int[] colWidth = {  50, 125,
                                100, 100, 100, 70,
                                120, 120, 100, 100,
                                100, 100};
    private String[] colNames = {"UID", "InputPower [nv]",
                                 "NoPoints", "FiltMethod", "DCComp.","Decade",
                                 "MinY [XVal]", "MaxY [XVal]", "AvgY", "RMS [volts]",
                                 "WtdVal", "Type"};
    //private final NumberFormat f = new DecimalFormat("#000000.000000000000");
    /**
     * 
     */
    public FFTResultsTM(){
    }
    @Override
    public int getColumnWidth(int col) {
        return colWidth[col];
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }
    /**
     *
     * @return
     */
    public int getRowCount() {
        return fftParams!=null?fftParams.length:0;
    }
    /**
     *
     * @return
     */
    public int getColumnCount() {
        return colNames.length;
    }
    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return fftParams[rowIndex].uid+"";
        }else if(columnIndex==1){
            return adcAcqParams[rowIndex].inputPower+" nv";
        }else if(columnIndex==2){
            return fftParams[rowIndex].getNoOfPoints()+"";
        }else if(columnIndex==3){
            return fftParams[rowIndex].getFiltMethod();
        }else if(columnIndex==4){
            return fftParams[rowIndex].getDCVal();
        }else if(columnIndex==5){
            return fftParams[rowIndex].getSelectedDecade();
        }else if(columnIndex==6){
            return fftParams[rowIndex].getMinValCol();
        }else if(columnIndex==7){
            return fftParams[rowIndex].getMaxValCol();
        }else if(columnIndex==8){
            return fftParams[rowIndex].getAvgValCol();
        }else if(columnIndex==9){
            return fftParams[rowIndex].getRMSValCol();
        }else if(columnIndex==10){
            return fftParams[rowIndex].getWeightedValCol();
        }else if(columnIndex==11){
            return fftParams[rowIndex].getDisplayType();
        }else{
            return "";
        }
    }
    /**
     * 
     * @param adcAcqParams
     * @param fftParams
     * @param displayType
     */
    public void setData(ADCAcqParams[] adcAcqParams, FFTParams[] fftParams){
        this.adcAcqParams = adcAcqParams;
        this.fftParams = fftParams;
        fireTableDataChanged();
    }
}