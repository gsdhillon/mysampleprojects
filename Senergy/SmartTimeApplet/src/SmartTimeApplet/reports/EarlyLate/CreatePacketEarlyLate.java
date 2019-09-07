/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartTimeApplet.reports.EarlyLate;

import lib.session.MyUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponent;
import net.sourceforge.jdatepicker.JDatePicker;
import lib.utils.Packetizer;

/**
 *
 * @author pradnya
 */
public class CreatePacketEarlyLate {

    private EarlyLateGUI EL;
    private String packet = "";

    public CreatePacketEarlyLate(EarlyLateGUI EL, String div, String section, String workL, boolean Todays) {
        this.EL = EL;
        packet = Packet(div, section, workL, Todays);
    }

    public CreatePacketEarlyLate(EarlyLateGUI EL, String checkHead, boolean Todays) {
        this.EL = EL;
        packet = Packet(checkHead, Todays);
    }

    public CreatePacketEarlyLate(String[] name) throws Exception {
        packet = Packet(name);
    }

    public String getPacket() {
        return packet;

    }

    private String Packet(String div, String section, String workL, boolean Todays) {//packet which contains div,section,workloc
        Packetizer a = new Packetizer();
        try {
            a.addBool(Todays);
            if (Todays == true) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                a.addString(dateFormat.format(date).toString());
            } else {
                a.addString(GetDate(EL.picker1));
                a.addString(GetDate(EL.picker2));
            }
            a.addString(div);
            a.addString(section);
            a.addString(workL);
        } catch (Exception ex) {
            MyUtils.showException("CreateSelectedPacket", ex);
            return "PacketFail";
        }
        return a.getPacket();
    }

    private String Packet(String checkHead, boolean Todays) {//packet which doesnt contains div,section,workloc
        Packetizer a = new Packetizer();
        try {

            a.addBool(Todays);
            if (Todays == true) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                a.addString(dateFormat.format(date).toString());
            } else {
                a.addString(GetDate(EL.picker1));
                a.addString(GetDate(EL.picker2));
            }
            a.addString(checkHead);
        } catch (Exception ex) {
            MyUtils.showException("CreateFullPacket", ex);
            return "PacketFail";
        }
        return a.getPacket();
    }

    private String GetDate(JDatePicker picker) {
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
