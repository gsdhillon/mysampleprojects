/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Common;

/**
 *
 * @author pradnya
 */
public class ReportClass {

    public String[] returnVals = new String[20];

    public ReportClass() {
        for (int i = 0; i < 20; i++) {
            returnVals[i] = new String();
        }
    }
}
