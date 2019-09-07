package cpnds.gui.ADCAcquisition;

import cpnds.MyGraphs.CustomLineGraph;
import cpnds.MyGraphs.DataPoint;
import cpnds.MyGraphs.LineGraph;
import cpnds.data.MyData;
import java.awt.BorderLayout;
import javax.swing.JDialog;

/**
 * @type     : Java Class
 * @name     : InputWaveDialog
 * @file     : InputWaveDialog.java
 * @created  : Jan 13, 2011 4:31:14 PM
 * @version  : 1.2
 */
public class InputWaveDialog extends JDialog{
    public InputWaveDialog(ADCAcqParams adcParams){
        super(MyData.mainFrame, "InputWave", true);
        setLayout(new BorderLayout());

        DataPoint[] points = new DataPoint[3];

        points[0] = new DataPoint();
        points[0].x = 0;
        points[0].y = 0;

        points[1] = new DataPoint();
        points[1].x = adcParams.riseTime;
        points[1].y = adcParams.pulseHeight;

        points[2] = new DataPoint();
        points[2].x = adcParams.riseTime+adcParams.fallTime;
        points[2].y = 0;
        setSize(400, 600);
        try{
            CustomLineGraph lineGraph = new CustomLineGraph(
                    points,
                    "Time",
                    "InputWave ExpUID="+adcParams.uid);
            add(lineGraph, BorderLayout.CENTER);
            setSize(lineGraph.TOTAL_WIDTH+20, lineGraph.TOTAL_HEIGHT+40);
        }catch(Exception e){
            MyData.showErrorMessage(e);
        }
        setLocationRelativeTo(MyData.mainFrame);
    }
}