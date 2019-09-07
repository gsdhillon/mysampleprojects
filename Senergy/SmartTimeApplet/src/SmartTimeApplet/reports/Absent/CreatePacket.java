/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.Absent;

import lib.session.MyUtils;
import lib.utils.Packetizer;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDatePicker;

/**
 *
 * @author pradnya
 */
public class CreatePacket {

    private AbsentReportGUI AR;
    private String packet = "";

    public CreatePacket(AbsentReportGUI AR, String div, String section, String workL) {
        this.AR = AR;
        packet = Packet(div, section, workL);
    }

    public CreatePacket(AbsentReportGUI AR, String checkHead) {
        this.AR = AR;
        packet = Packet(checkHead);
    }

    public CreatePacket(String[] name) throws Exception {
        packet = Packet(name);
    }

    public String getPacket() {
        return packet;

    }

    private String Packet(String div, String section, String workL) {
        Packetizer a = new Packetizer();
        try {
            a.addString(GetDate(AR.picker1));
            a.addString(div);
            a.addString(section);
            a.addString(workL);
        } catch (Exception ex) {
            MyUtils.showException("CreateSelectedPacket", ex);
            return "PacketFail";
        }
        return a.getPacket();
    }

    private String Packet(String checkHead) {
        Packetizer a = new Packetizer();
        try {
            a.addString(GetDate(AR.picker1));
            a.addString(checkHead);
        } catch (Exception ex) {
            MyUtils.showException("CreateFullPacket", ex);
            return "PacketFail";
        }
        return a.getPacket();
    }

    public static String GetDate(JDatePicker picker) {
        DateModel model = ((JDateComponent) picker).getModel();
        String month = "";
        String day = "";
        if ((model.getMonth() + 1) > 0 && (model.getMonth() + 1) < 10) {
            month = "0" + (model.getMonth() + 1);
        } else {
            month = "" + (model.getMonth() + 1);
        }
        if ((model.getDay() + 1) > 0 && (model.getDay() + 1) < 10) {
            day = "0" + (model.getDay());
        } else {
            day = "" + (model.getDay());
        }

        String date1 = model.getYear() + "-" + month + "-" + day;
        return date1;
    }

    public static String Packet(String[] name) throws Exception {
        Packetizer a = new Packetizer();
        a.setCounter();
        if (name.length > 0) {
            for (int i = 0; i < name.length; i++) {
                if (!"Select".equals(name[i])) {
                    a.addString(name[i]);
                    a.incrCounter();
                }
            }
        }
        return a.getPacket();
    }
}
