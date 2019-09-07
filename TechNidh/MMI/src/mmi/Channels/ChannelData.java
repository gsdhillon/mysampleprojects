package mmi.Channels;

import gui.MyComboBox;
import gui.MyLabel;
import gui.MyPanel;
import gui.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import mmi.data.MyData;

/**
 * Class ChannelData
 * Created on Sep 2, 2011
 * @version 1.0.0
 * @author
 */
public class ChannelData implements ItemListener{
    private int channelNo = -1;
    private final int NUM_RISE_TIME = 1000;
    private int[] riseTimeNS = new int[NUM_RISE_TIME];
    private int[] spec = {
        10000, 
         9000, 8000, 7000, 6000, 5000, 4000, 3000, 2000, 1000,
          900,  800,  700,  600,  500,  400,  300,  200,  100,
           90,   80,   70,   60,   50,   40,   30,   20,   10,
            9,    8,    7,    6,    5,    4,    3,    2,    1
    };
    private int[] freq = new int[spec.length];
    private int[] value = new int[spec.length];
    private int filterConstant = 0;

    private final int NUM_AMP = 220;
    private int[] amp = new int[NUM_AMP];
    private MyComboBox comboRiseTime;
    private MyTextField textFilterConstant;
    private MyComboBox comboFrequency;
    private MyComboBox comboAmplitude;
    private MyLabel labelDataBytes;
    private MyLabel labelDataDesc;
    private byte[] p = new byte[10];
    private MyPanel guiPanel;
    /**
     *
     * @param channelNo
     */
    public ChannelData(int channelNo){
        this.channelNo = channelNo;
        //RiseTime
        comboRiseTime = new MyComboBox();
        NumberFormat f = new DecimalFormat("#0");
        int riseTm = 100;
        for(int i = 0; i<NUM_RISE_TIME; i++){
            riseTimeNS[i] = riseTm;
            comboRiseTime.addItem(f.format(riseTm));
            riseTm += 100;
        }
        comboRiseTime.addItemListener(this);

        //FilterConstant
        textFilterConstant = new MyTextField("0");
        textFilterConstant.setEditable(false);

        //Frequency
        comboFrequency = new MyComboBox();
        
        //Amplitude
        f = new DecimalFormat("#0");
        comboAmplitude = new MyComboBox();
        for(int i = 0; i<NUM_AMP; i++){
            amp[i] = (i+1);
            comboAmplitude.addItem(f.format(amp[i]));
        }
        //data Bytes
        labelDataBytes = new MyLabel(MyLabel.TYPE_DATA, "");
        for(int i=0;i<10;i++){
            p[i] = 0;
        }
        labelDataBytes.setText(MyData.hexify(p));

        //data Bytes
        labelDataDesc = new MyLabel(MyLabel.TYPE_DATA,
                " 03 P9 P8 P7 P6 P5 P4 P3 P2 P1");

        populateFreq(0);
        makeGUI();
    }
    /**
     *
     * @return
     */
    private void makeGUI(){
        guiPanel = new MyPanel(new BorderLayout());

        guiPanel.setPreferredSize(new Dimension(MyData.CHANNEL_GUI_WIDTH,
                                                MyData.CHANNEL_GUI_HEIGHT-30));
        guiPanel.setBorder(BorderFactory.createTitledBorder(
                                                      "Chanenel - "+channelNo));

        MyPanel labelPanel = new MyPanel(new GridLayout(0, 1, 2, 10));
        MyPanel fieldPanel = new MyPanel(new GridLayout(0,1, 2, 10));

        //Rise Time
        labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Rise Time (ns):"));
        fieldPanel.add(comboRiseTime);

        //FilterConstant
        if(MyData.debug){
            labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Filter Constant:"));
            fieldPanel.add(textFilterConstant);
        }

        //Frequency
        labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Frequency (hz):"));
        fieldPanel.add(comboFrequency);

        //Amplitude
        labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Amplitude:"));
        fieldPanel.add(comboAmplitude);

 
        if(MyData.debug){
            //Data to be sent
            labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Data Bytes:"));
            fieldPanel.add(labelDataBytes);

            //Data desc
            labelPanel.add(new MyLabel(MyLabel.TYPE_LABEL, "Data Desc:"));
            fieldPanel.add(labelDataDesc);
        }

        guiPanel.add(labelPanel, BorderLayout.WEST);
        guiPanel.add(fieldPanel, BorderLayout.CENTER);
    }

    /**
     *
     * @return
     */
    public MyPanel getGUI(){
        return guiPanel;
    }
    /**
     *
     * @return
     */
    public byte[] getDataBytes() throws Exception{
        //TODO Nidhi making data bytes of each channel
        //MAKING P1 P2 P3 P4
        int riseTime_ns = riseTimeNS[comboRiseTime.getSelectedIndex()];
        double freq_MHz = Math.pow(10, 3)/(2.0*riseTime_ns);
        int fmclk_MHz = 25;
        int data = (int)(Math.pow(2, 28)*freq_MHz / fmclk_MHz);
       // System.out.println(data);
        int num28Bit = data & 0x0FFFFFFF;//last 14 bit 1s
      //  System.out.println(num28Bit);
        //take lower 14 bits in a
        int a = num28Bit & 0x00003FFF;//last 14 bit 1s
        //take next 14 bits in b
        int b = (num28Bit >> 14 )& 0x00003FFF;
       // System.out.println(data + " = " + num28Bit + " = " + (a|(b<<14)));
        //take p0 and p1 from a
        p[0] = (byte) (a & 0x000000FF);
        p[1] = (byte) ((a >> 8) & 0x000000FF);
        //take p2 and p3 from b
        p[2] = (byte) (b & 0x000000FF);
        p[3] = (byte) ((b >> 8) & 0x000000FF);

        //making P5
        if(filterConstant < 0 || filterConstant > 255){
            throw new Exception("Filter Const. not 0-255");
        }
        p[4] = (byte) filterConstant;

        //making P6 P7 P8
        int freq_val = value[comboFrequency.getSelectedIndex()];
        p[5] =  (byte) (freq_val & 0x000000FF);
        p[6] =  (byte) ((freq_val >> 8) & 0x000000FF);
        p[7] =  (byte) ((freq_val >> 16)& 0x000000FF);

        //Making P9
        p[8] = (byte) amp[comboAmplitude.getSelectedIndex()];

        //P10 fix value 3
        p[9] = 3;
        labelDataBytes.setText(MyData.hexifyReverseOrder(p));
        return p;
    }

    public void itemStateChanged(ItemEvent e) {
        try{
            int index = comboRiseTime.getSelectedIndex();
            populateFreq(index);
            if(channelNo==5){
                filterConstant = calculateFC_Channel5(index);
                textFilterConstant.setText(filterConstant+"");
            }else{
                filterConstant = calculateFC_Channel1_4(index);
                textFilterConstant.setText(filterConstant+"");
            }

            getDataBytes();
        }catch(Exception ex){
            MyData.showErrorMessage(ex);
        }
    }

    private void populateFreq(int index) {
        int riseTime_ns = riseTimeNS[index];
        double freq_hz = Math.pow(10, 9)/(2.0*riseTime_ns);
        NumberFormat f = new DecimalFormat("#0");
        comboFrequency.removeAllItems();
        //calculate freq and values in temp arrays
        int[] valueTemp = new int[spec.length];
        int[] freqTemp = new int[spec.length];
        for(int i=0;i<spec.length;i++){
            //TODO Nidhi - Please check FReq Cals here
            //for riseTime 100000 and spec[0] = 10000
            //freq_hz = 5000 SO freq_hz/spec[0] becoming 0
            //so im taking freq_hz/spec[0] in double (tempVal) for calc freqTemp[i]
            //and puting (int) tempVal to valueTemp[i] which may be ZERO****
            double tempVal = (freq_hz/spec[i]);
            freqTemp[i] = (int) (freq_hz/tempVal);
            valueTemp[i] = (int) tempVal;
            /*
            if(i==0){
                MyData.showInfoMessage(freq_hz+" "+valueTemp[i]+" "
                        +spec[i]+" "+freqTemp[i]);
            }
            */
        }
        //remove duplicate freqs and values
        //put ith freq and values in distinct arrays
        int distinct = 0;
        for(int i=0;i<spec.length;i++){
            //check if ith freq and values is already added
            boolean match = false;
            for(int j=0;j<distinct;j++){
                if(freq[j] == freqTemp[i]){
                    match = true;
                    break;
                }
            }
            if(!match){
                //put ith freq and values in distinct arrays
                value[distinct] = valueTemp[i];
                freq[distinct] = freqTemp[i];
                distinct++;
            }
        }
        for(int i=0;i<distinct;i++){
            comboFrequency.addItem(f.format(freq[i]));
        }
        comboFrequency.validate();
        comboFrequency.setSelectedIndex(0);
    }

    private int calculateFC_Channel1_4(int index) {
        int riseTime_ns = riseTimeNS[index];
        /*
            1) for 0 to 10,000 -> FC goes from 0 to 2
            2) for 10,000 to 20,000 -> FC goes from 2 to 7
            3) for 20,000 to 40,000 -> FC goes from 7 to 15
            4) for 40,000 to 50,000 -> FC goes from 15 to 20
            5) for 50,000 to 60,000 -> FC is 20
            6) for 60,000 to 70,000 -> FC goes from 20 to 25
            7) for 70,000 to 80,000 -> FC is 25
            8) for 80,000 to 100,000 -> FC goes from  25 to 30
        */
        int[][] range = {
            {0, 10000},
            {10000, 20000},
            {20000, 40000},
            {40000, 50000},
            {50000, 60000},
            {60000, 70000},
            {70000, 80000},
            {80000, 100000}
        };
        int[][] values = {
            {0, 2},
            {2, 7},
            {7, 15},
            {15, 20},
            {20, 20},
            {20, 25},
            {25, 25},
            {25, 30}
        };
        for(int i=0;i<range.length;i++){
            if(riseTime_ns >= range[i][0] && riseTime_ns <= range[i][1]){
                return calculateFC( range[i][0],  range[i][1],
                                    values[i][0],  values[i][1],
                                    riseTime_ns);
            }
        }
        return 0;
    }

    private int calculateFC_Channel5(int index) {
        int riseTime_ns = riseTimeNS[index];
        /*
            1) from 0 to 20,000   -- FC is from 0 to 25
            2) from 20000 to 40,000   -- FC is constant 25
            3) from 40000 to 50,000   -- FC is from 25 to 30
            4) from 50000 to 60,000   -- FC is constant 30
            5) from 60000 to 70,000   -- FC is constant 30
            6) from 70000 to 100,000   -- FC is from 30 to 45
       */
        int[][] range = {
            {0, 20000},
            {20000, 40000},
            {40000, 50000},
            {50000, 60000},
            {60000, 70000},
            {70000, 100000}
        };
        int[][] values = {
           {0, 25},
            {25, 25},
            {25, 30},
            {30, 30},
            {30, 30},
            {30, 45}
        };
        for(int i=0;i<range.length;i++){
            if(riseTime_ns >= range[i][0] && riseTime_ns <= range[i][1]){
                return calculateFC( range[i][0],  range[i][1],
                                    values[i][0],  values[i][1],
                                    riseTime_ns);
            }
        }
        return 0;
    }

    private int calculateFC(float x1, float x2, float y1, float y2, float x){
        if(y2<=y1){
            return (int) y1;
        }
        float y = y1 + (((y2 - y1) / (x2 - x1)) * (x - x1));
        return (int) y;
    }


}