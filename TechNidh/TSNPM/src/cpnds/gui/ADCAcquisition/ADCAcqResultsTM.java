package cpnds.gui.ADCAcquisition;

import gui.MyTableModel;
/**
 * @type     : Java Class
 * @name     : ADCAcqResultsTM
 * @file     : ADCAcqResultsTM.java
 * @created  : Jun 22, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class ADCAcqResultsTM extends MyTableModel{

    public byte fsTableIndex = 0;
    public float pulseHeight;
    //changed from byte to int due to 128 -> -128
    //in the file it is stored as byte only
    //and while reading it is converted back to int
    //treating it as unsigned byte
    public int samplingAvg;
    public int pulseWidth;
    public float inputPower;
    public int inputCountRate;


    private ADCAcqParams[] adcAcqParams;
    private int[] colWidth = {50, 80, 80, //100, 100,
                              80, 120, 120, 80,
                              80, 80, 80, 80, 
                              80, 80, 80};
    private String[] colNames = {"UID", "InContRt", "SnapShTm [Sec]", //"Exp.Date", "Exp.Time",
                                  "InputPower [nv]", "MinY [XVal]", "MaxY [XVal]", "AvgY",
                                  "NoOfPts", "FS [Hz]", "PulsHt [V]", "SampAvg",
                                  "PulseWdth [NSec]", "RiseTm[NSec]", "FallTm[NSec]"
                                  };
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
        return adcAcqParams!=null?adcAcqParams.length:0;
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
        if(adcAcqParams[rowIndex].uid==0){
            return "";
        }
        //
        if(columnIndex==0){
            return String.valueOf(adcAcqParams[rowIndex].uid);
        /*}else if(columnIndex==1){
            return adcAcqParams[rowIndex].getDate();
        }else if(columnIndex==2){
            return adcAcqParams[rowIndex].getTime();*/
        }else if(columnIndex==1){
            return String.valueOf(adcAcqParams[rowIndex].inputCountRate);
        }else if(columnIndex==2){
            try{
                return adcAcqParams[rowIndex].getSnapshotTime()+" Sec";
            }catch(Exception e){
                return "";
            }
        }else if(columnIndex==3){
            return String.valueOf(adcAcqParams[rowIndex].inputPower)+" nv";
        }else if(columnIndex==4){
            return adcAcqParams[rowIndex].minVal +" ["+adcAcqParams[rowIndex].minValX+"]";
        }else if(columnIndex==5){
            return adcAcqParams[rowIndex].maxVal+ " ["+adcAcqParams[rowIndex].maxValX+"]";
        }else if(columnIndex==6){
            return String.valueOf(adcAcqParams[rowIndex].avgVal);
        }else if(columnIndex==7){
            return String.valueOf(adcAcqParams[rowIndex].numOfPoints);
        }else if(columnIndex==8){
            try{
                return adcAcqParams[rowIndex].getFS()+" Hz";
            }catch(Exception e){
                return  "";
            }
        }else if(columnIndex==9){
            return String.valueOf(adcAcqParams[rowIndex].pulseHeight)+" V";
        }else if(columnIndex==10){
            return String.valueOf(adcAcqParams[rowIndex].samplingAvg);
        }else if(columnIndex==11){
            return String.valueOf(adcAcqParams[rowIndex].pulseWidth)+ " NSec";
        }else if(columnIndex==12){
            return String.valueOf(adcAcqParams[rowIndex].riseTime)+ " NSec";
        }else if(columnIndex==13){
            return String.valueOf(adcAcqParams[rowIndex].fallTime)+ " NSec";
        }else{
            return "";
        }
    }
    /**
     * 
     * @param adcAcqParams
     */
    public void setData(ADCAcqParams[] adcAcqParams){
        this.adcAcqParams = adcAcqParams;
        fireTableDataChanged();
    }
}
