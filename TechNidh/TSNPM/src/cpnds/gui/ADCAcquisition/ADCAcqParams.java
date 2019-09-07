package cpnds.gui.ADCAcquisition;

import cpnds.MyGraphs.DataPoint;
import cpnds.data.MyData;
import gui.MyTable;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JScrollPane;

/**
 * @type     : Java Class
 * @name     : ADCAcqParams
 * @file     : ADCAcqParams.java
 * @created  : May 15, 2011 11:43:39 AM
 * @version  : 1.2
 */
public class ADCAcqParams {
    public final byte processID = 71;
    public int uid = 0;
    public File paramFile;
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
    //following results are saved with params file
    //actual values will be written after analyzing
    public int minValX = -1;//newG
    public float minVal = 0.0f;
    public int maxValX = -1;//newG
    public float maxVal = 0.0f;
    public float avgVal = 0.0f;
    public int numOfPoints = 0;
    public int riseTime = 0;
    public int fallTime = 0;
    private final int LEN = 55;//newG
    //
    private final String dirName = "ADC_ACQ_DATA";
    private final String paramsFileName = "ADC_ACQ_PARAMS";

    private ADCAcqParams[] adcAcqParams = new ADCAcqParams[1];
    private ADCAcqResultsTM paramsTM = new ADCAcqResultsTM();
    private MyTable paramsTable = new MyTable(paramsTM);
    public JScrollPane sp = new JScrollPane(paramsTable);
    /**
     * 
     */
    public ADCAcqParams(){
        adcAcqParams[0] = this;
        paramsTM.setData(adcAcqParams);
        paramsTable.setColumnSelectionAllowed(true);
        paramsTable.setRowHeight(23);
        sp.setPreferredSize(new Dimension(1000, 45));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //paramsTM.fireTableDataChanged();
    }
    /**
     * 
     * @param file
     * @throws Exception
     */
    public void write(File file) throws Exception{
        //writing parameters in the param file
        byte[] params = new byte[LEN];//newG
        params[0] = processID;
        MyData.putIntBytesToBuffer(uid, params, 1);
        params[5] = fsTableIndex;
        MyData.putFloatBytesToBuffer(pulseHeight, params, 6);
        params[10] = (byte)samplingAvg;
        MyData.putIntBytesToBuffer(pulseWidth, params, 11);
        MyData.putFloatBytesToBuffer(inputPower, params, 15);
        MyData.putIntBytesToBuffer(inputCountRate, params, 19);
        MyData.putIntBytesToBuffer(minValX, params, 23);//newG
        MyData.putFloatBytesToBuffer(minVal, params, 27);
        MyData.putIntBytesToBuffer(maxValX, params, 31);//newG
        MyData.putFloatBytesToBuffer(maxVal, params, 35);
        MyData.putFloatBytesToBuffer(avgVal, params, 39);
        MyData.putIntBytesToBuffer(numOfPoints, params, 43);
        MyData.putIntBytesToBuffer(riseTime, params, 47);//newG
        MyData.putIntBytesToBuffer(fallTime, params, 51);//newG
        MyData.writeParameterFile(file, params, 0, LEN);//newG
    }
    /**
     *
     * @param file
     * @throws Exception
     */
    public void read(File file) throws Exception{
        //read input params from file
        byte[] buffer = MyData.readParameterFile(file);
        if(buffer[0] != processID){
            throw new Exception("INVALID ADC_ACQ_PARAM FILE PID = "+buffer[0]);
        }
        if(buffer.length != LEN){//newG
            throw new Exception("Old version of ADC_ACQ_PARAMS file");
        }
        uid = MyData.getInt(buffer, 1);
        fsTableIndex = buffer[5];
        pulseHeight = MyData.getFloat(buffer, 6);
        samplingAvg = (int)(buffer[10]&0x00FF);
        pulseWidth = MyData.getInt(buffer, 11);
        inputPower = MyData.getFloat(buffer, 15);
        inputCountRate = MyData.getInt(buffer, 19);
        minValX = MyData.getInt(buffer, 23);//newG
        minVal = MyData.getFloat(buffer, 27);
        maxValX = MyData.getInt(buffer, 31);//newG
        maxVal = MyData.getFloat(buffer, 35);
        avgVal = MyData.getFloat(buffer, 39);
        numOfPoints = MyData.getInt(buffer, 43);
        riseTime = MyData.getInt(buffer, 47);
        fallTime = MyData.getInt(buffer, 51);
        paramFile = file;
        paramsTM.fireTableDataChanged();
    }
    /**
     * calculate min max and avg value
     * save into the file also
     * @param points
     * @throws Exception
     */
    public void calculateMinMaxAvg(DataPoint[] points) throws Exception{
        //calculate MIN MAX and AVG
        minVal = Float.MAX_VALUE;
        maxVal = - Float.MAX_VALUE;
        double sum = 0;
        for (int i = 0; i < points.length; i++) {
            if(minVal > points[i].y){
                minVal = points[i].y;
                minValX = i;//newG
            }
            if(maxVal < points[i].y){
                maxVal = points[i].y;
                maxValX = i;//newG
            }
            sum += points[i].y;
        }
        avgVal = (float)(sum/points.length);
    }
    /**
     *
     * @param experimentFolder
     * @return
     */
    public File getParamFile(String experimentFolder){
        return new File(experimentFolder+"/"+dirName+"/"+paramsFileName);
    }

    /**
     *
     * @return
     */
    public float getSnapshotTime() throws Exception{
        //TODO calculating SnapshotTime
        if(fsTableIndex==0){
            throw new Exception("FS_INDEX_NOT_SET");
        }
        return (float)1048576.0/FS[fsTableIndex];
    }

    /**
     * 
     * @return
     */
    public float getFS() throws Exception{
        //TODO  Getting FS
        if(fsTableIndex==0){
            throw new Exception("FS_INDEX_NOT_SET");
        }
        if(samplingAvg==0){
            //MyData.showInfoMessage("Returning fs = "+FS[fsTableIndex]);
            return (float)FS[fsTableIndex];
        }else{
            //Told by mohit on the phone
            /*MyData.showInfoMessage(
                    "FS[fsTableIndex] = "+FS[fsTableIndex]+
                    ", samplingAvg="+samplingAvg+
                    ", FS[fsTableIndex]/samplingAvg="+FS[fsTableIndex]/samplingAvg);*/
            return (float)FS[fsTableIndex]/samplingAvg;
        }
    }
    //index starts from 1 so index 0 means freq not set
    private long[] FS = 
                {     0,//0
                 100000,//1
                 200000,//2
                 300000,//3
                 400000,//4
                 500000,//5
                 600000,//6
                 700000,//7
                 800000,//8
                 900000,//9
                1000000,//10
                2000000,//11
                3030000,//12
                4000000,//13
                5000000,//14
                6250000,//15
                7140000,//16
                8330000,//17
                9090000,//18
                10000000,//19
                11111000,//20
                12500000,//21
                14280000,//22
                16667000,//23
                20000000};//24
    //TODO fsTable
    //fsTabe to be shown in the dropdown
    public String[] fsTable = {
                "Index - Sampling Frequency - Snapshot Time",
                "01    -           100 K    -       10 sec",
                "02    -           200 K    -        5 sec",
                "03    -           300 K    -    3.333 sec",
                "04    -           400 K    -      2.5 sec",
                "05    -           500 K    -        2 sec",
                "06    -           600 K    -     1.67 sec",
                "07    -           700 K    -    1.429 sec",
                "08    -           800 K    -     1.25 sec",
                "09    -           900 K    -     1.11 sec",
                "10    -             1 M    -      1000 ms",
                "11    -             2 M    -       500 ms",
                "12    -         3.030 M    -       333 ms",
                "13    -             4 M    -       250 ms",
                "14    -             5 M    -       200 ms",
                "15    -          6.25 M    -       160 ms",
                "16    -          7.14 M    -       140 ms",
                "17    -          8.33 M    -       120 ms",
                "18    -          9.09 M    -       111 ms",
                "19    -            10 M    -       100 ms",
                "20    -        11.111 M    -        90 ms",
                "21    -          12.5 M    -        80 ms",
                "22    -         14.28 M    -        70 ms",
                "23    -        16.667 M    -        60 ms",
                "24    -            20 M    -        52 ms"
    };

    public String getDate() {
        try{
            String s = paramFile.getParentFile().getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }

    public String getTime() {
        try{
            String s = paramFile.getParentFile().getParentFile().getName();
            return s.substring(5);
        }catch(Exception e){
            return "null";
        }
    }
}