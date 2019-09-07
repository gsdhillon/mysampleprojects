/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Brief;

import java.awt.*;
import lib.gui.MyButton;
import lib.gui.MyLabel;
import lib.gui.MyPanel;
import lib.gui.table.MyTable;
import lib.gui.table.MyTableModel;

/**
 *
 * @author pradnya
 */
public class BriefReportView extends MyPanel {

    private Container contentsPane;
    public String strCompanyNm;
    public String strDate;
    public String strTotalEmp;
    public MyPanel MainPanel;
    public MyTable tlbDetails;
    public MyButton btnPrint, btnCancel;
    public String RptNm;


    public void setTableModel(MyTableModel tableModel) {
        tlbDetails = new MyTable(tableModel);
        MainPanel.add(tlbDetails.getGUI(), BorderLayout.CENTER);
    }

    public BriefReportView() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 2);
        int height = (int) (screenSize.height / 1.3);
        setSize(width, height);

        strCompanyNm = "Senergy Intellution Pvt.Ltd.";
        strTotalEmp = "234";
        strDate="";
        MainPanel = new MyPanel(new BorderLayout());
        MyPanel panNorth = new MyPanel(new BorderLayout());

        MyPanel panHeading = new MyPanel(new GridLayout(2, 1));

        MyPanel panCmpName = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyLabel lblCompNm = new MyLabel(3, strCompanyNm);
        panCmpName.add(lblCompNm);
        panHeading.add(panCmpName);

        MyPanel panRptNm = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        MyLabel lblRptNm = new MyLabel(3, "Brief Details For " + strDate);
        panRptNm.add(lblRptNm);
        panHeading.add(panRptNm);
        panNorth.add(panHeading, BorderLayout.NORTH);

        MyPanel panDetails = new MyPanel(new BorderLayout());

        MyPanel panG1 = new MyPanel(new BorderLayout());
        MyPanel panlblG1 = new MyPanel(new GridLayout(4, 1));

        MyLabel lblTotalPresent = new MyLabel(2, "Total Present: ");
        panlblG1.add(lblTotalPresent);

        MyLabel lblTotalAbsent = new MyLabel(2, "Total Absent: ");
        panlblG1.add(lblTotalAbsent);

        MyLabel lblTLateCmg = new MyLabel(2, "Total Late Coming: ");
        panlblG1.add(lblTLateCmg);

        MyLabel lblTEarlyGng = new MyLabel(2, "Total Early Going: ");
        panlblG1.add(lblTEarlyGng);

        panG1.add(panlblG1, BorderLayout.CENTER);

        MyPanel panGetlblG1 = new MyPanel(new GridLayout(4, 1));

        MyLabel lblGetTPresent = new MyLabel(2, "");
        panGetlblG1.add(lblGetTPresent);

        MyLabel lblGetTAbsent = new MyLabel(2, "");
        panGetlblG1.add(lblGetTAbsent);

        MyLabel lblGetTLateCmg = new MyLabel(2, "");
        panGetlblG1.add(lblGetTLateCmg);

        MyLabel lblGetTEarlyGng = new MyLabel(2, "");
        panGetlblG1.add(lblGetTEarlyGng);

        panG1.add(panGetlblG1, BorderLayout.EAST);
        panDetails.add(panG1, BorderLayout.WEST);

        MyPanel panG2 = new MyPanel(new BorderLayout());

        MyPanel panlblG2 = new MyPanel(new GridLayout(4, 1));

        MyLabel lblTotalOnL = new MyLabel(2, "Total On Leave: ");
        panlblG2.add(lblTotalOnL);

        MyLabel lblTMissIn = new MyLabel(2, "Total Miss In: ");
        panlblG2.add(lblTMissIn);

        MyLabel lblTMissOut = new MyLabel(2, "Total Miss Out: ");
        panlblG2.add(lblTMissOut);

        MyLabel lblTCardExp = new MyLabel(2, "Total Card Expired: ");
        panlblG2.add(lblTCardExp);

        panG2.add(panlblG2, BorderLayout.CENTER);

        MyPanel panGetlblG2 = new MyPanel(new GridLayout(4, 1));

        MyLabel lblGetTOnL = new MyLabel(2, "");
        panGetlblG2.add(lblGetTOnL);

        MyLabel lblGetTMissIn = new MyLabel(2, "");
        panGetlblG2.add(lblGetTMissIn);

        MyLabel lblGetTMissOut = new MyLabel(2, "");
        panGetlblG2.add(lblGetTMissOut);

        MyLabel lblGetTCardExp = new MyLabel(2, "");
        panGetlblG2.add(lblGetTCardExp);

        panG2.add(panGetlblG2, BorderLayout.EAST);
        panDetails.add(panG2, BorderLayout.EAST);

        MyPanel panTotalEmp = new MyPanel(new FlowLayout());
        MyLabel lblTotalEmp = new MyLabel(2, "Total Employee : " + strTotalEmp);
        panTotalEmp.add(lblTotalEmp);
        panDetails.add(panTotalEmp, BorderLayout.CENTER);

        panNorth.add(panDetails, BorderLayout.CENTER);
                             
        MainPanel.add(panNorth, BorderLayout.NORTH);

        MyPanel panButtons = new MyPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrint = new MyButton("Print", 0) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        panButtons.add(btnPrint);

        btnCancel = new MyButton("Cancel", 0) {

            @Override
            public void onClick() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        panButtons.add(btnCancel);
        MainPanel.add(panButtons, BorderLayout.SOUTH);
        //contentsPane = getContentPane();
        contentsPane.setLayout(new BorderLayout());
        contentsPane.add(MainPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
