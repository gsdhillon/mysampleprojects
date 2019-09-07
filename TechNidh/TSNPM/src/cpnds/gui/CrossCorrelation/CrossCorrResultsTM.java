package cpnds.gui.CrossCorrelation;

import cpnds.gui.ADCAcquisition.*;
import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : CrossCorrResultsTM
 * @file     : CrossCorrResultsTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class CrossCorrResultsTM extends MyTableModel{
    private CrossCorrParams[] crossCorrParams;
    private ADCAcqParams[] adcAcqParams;
    private CrossCorrCommonParams[] crossCorrCommonParams;
    private int[] colWidth = {50, 100, 
                              100, 100, 100,
                              100, 100, 100,
                              120, 120, 100, 140,
                              50, 100, 140, 60};
    private String[] colNames = {"UID", "InputPower[nv]",
                                 "RsltNo", "NoPoints", "Decade",
                                 "StartFreq", "EndFreq", "CCFreq [Hz]",
                                 "MinY [XVal]", "MaxY [XVal]", "MaxY_All [ResNo-X]", "AvgY",
                                 "SetNo", "ThVal", "AvgSignalFreq [Hz]", "PrefRslt",};
    //private final NumberFormat f = new DecimalFormat("#000000.000000000000");

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
        return crossCorrParams!=null?crossCorrParams.length:0;
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
            return crossCorrParams[rowIndex].uid+"";
        }else if(columnIndex==1){
            return adcAcqParams[rowIndex].inputPower+" nv";
        }else if(columnIndex==2){
            return crossCorrParams[rowIndex].resultNo+"";
        }else if(columnIndex==3){
            return crossCorrParams[rowIndex].getNoOfPoints()+"";
        }else if(columnIndex==4){
            return crossCorrParams[rowIndex].getDecadeOption(); 
        }else if(columnIndex==5){
            return crossCorrParams[rowIndex].startFreq+"";
        }else if(columnIndex==6){
            return crossCorrParams[rowIndex].endFreq+"";
        }else if(columnIndex==7){
            return crossCorrParams[rowIndex].ccFreq+" Hz";
        }else if(columnIndex==8){
            return crossCorrParams[rowIndex].minVal+" ["+crossCorrParams[rowIndex].minValX+"]";
        }else if(columnIndex==9){
            return crossCorrParams[rowIndex].maxVal+" ["+crossCorrParams[rowIndex].maxValX+"]";
        }else if(columnIndex==10){
            return crossCorrCommonParams[rowIndex].maxVal+" ["
                    +crossCorrCommonParams[rowIndex].maxValResultNo+"-"
                    +crossCorrCommonParams[rowIndex].maxValX+"]";
        }else if(columnIndex==11){
            return crossCorrParams[rowIndex].avgVal+"";
        }else if(columnIndex==12){
            return (crossCorrParams[rowIndex].getSelectedThTPNo()+1)+"";
        }else if(columnIndex==13){
            return crossCorrParams[rowIndex].getTh()+"";
        }else if(columnIndex==14){
            return crossCorrParams[rowIndex].getSignalFreq()+" Hz";
        }else if(columnIndex==15){
            return crossCorrCommonParams[rowIndex].preferredResult+"";
        }else{
            return "";
        }
    }
    /**
     * 
     * @param adcAcqParams
     * @param autoCorrParams
     * @param crossCorComParams
     */
    public void setData(ADCAcqParams[] adcAcqParams, 
                                CrossCorrParams[] autoCorrParams,
                                    CrossCorrCommonParams[] crossCorComParams){
        this.adcAcqParams = adcAcqParams;
        this.crossCorrParams = autoCorrParams;
        this.crossCorrCommonParams = crossCorComParams;
        fireTableDataChanged();
    }
}