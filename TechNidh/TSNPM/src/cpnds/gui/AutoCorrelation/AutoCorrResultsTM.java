package cpnds.gui.AutoCorrelation;

import cpnds.gui.ADCAcquisition.*;
import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : AutoCorrResultsTM
 * @file     : AutoCorrResultsTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class AutoCorrResultsTM extends MyTableModel{
    private AutoCorrParams[] autoCorrParams;
    private ADCAcqParams[] adcAcqParams;
    private int[] colWidth = {50, 100, 
                                120, 120, 100, 100,
                                30, 100, 140};
    private String[] colNames = {"UID", "InputPower [nv]",
                                 "NoPoints", "MinY [XVal]", "MaxY [XVal]", "AvgY", 
                                 "SetNo", "ThVal", "AvgSignalFreq [Hz]"};
    
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
        return autoCorrParams!=null?autoCorrParams.length:0;
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
        if(autoCorrParams[rowIndex].uid==0){
            return "";
        }

        if(columnIndex==0){
            return autoCorrParams[rowIndex].uid+"";
        }else if(columnIndex==1){
            return adcAcqParams[rowIndex].inputPower+" nv";
        }else if(columnIndex==2){
            return autoCorrParams[rowIndex].getNoOfPoints()+"";
        }else if(columnIndex==3){
            return autoCorrParams[rowIndex].minVal+" ["+autoCorrParams[rowIndex].minValX+"]";
        }else if(columnIndex==4){
            return autoCorrParams[rowIndex].maxVal+" ["+autoCorrParams[rowIndex].maxValX+"]";
        }else if(columnIndex==5){
            return autoCorrParams[rowIndex].avgVal+"";
        }else if(columnIndex==6){
            return (autoCorrParams[rowIndex].getSelectedThTPNo()+1)+"";
        }else if(columnIndex==7){
            return autoCorrParams[rowIndex].getTh()+"";
        }else if(columnIndex==8){
            return autoCorrParams[rowIndex].getSignalFreq()+" Hz";
        }else{
            return "";
        }
    }

    public void setData(ADCAcqParams[] adcAcqParams, AutoCorrParams[] autoCorrParams){
        this.adcAcqParams = adcAcqParams;
        this.autoCorrParams = autoCorrParams;
        fireTableDataChanged();
    }
}