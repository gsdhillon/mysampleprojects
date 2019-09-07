package lib.Classes;

import java.util.logging.Level;
import java.util.logging.Logger;
import lib.utils.Depacketizer;
import lib.utils.Packetizer;
/**
 *
 * @author Gaurav
 */
public class RSClass {

    public int BaudRate;
    public int ReaderMode;
    public int CheckShiftHoliday;
    public int CheckPin;
    public int CheckValidityDOB;
    public int MultiRejAlarm;
    public boolean ChkRdrStr;
    public boolean ChkRecS1;
    public boolean ChkRecS2;
    public boolean ChkDate;
    public String ReaderNo;
    public String ReaderTypeLevel;
    public String UseRdrDB;
    public String AutoOffline;
    public String AntiPassBack;
    public String APBLevel;
    public String StoreAllRec;
    public String DoorDelay;
    public String LedDelay;
    public String InvalidAlarm;
    public String TamperAlarm;
    public String DoorOpenAlarm;
    public String OnlineRespTimeout;
    public String KeyTimeOut;
    public String AppID1;
    public String AppID2;
    public String ReaderString;
    public String Location;
    public String Division;
    public String ReaderType;
    public String SelfIP;
    public String SubnetMask;
    public String GateWayIP;
    public String ServerIP1;
    public String ServerIP2;
    public String ListenPort;
    public String ServerPort;
    public String Zone;
    public String ReaderModel;
    public String UnitCode;
    public String UnusedEntry;
    public String PUTmOut;
    public String PUCnt;
    public String Building="0";
    public String TotalRec;
    public String RecOnS1;
    public String RecOnS2;
    public String DateTime;

    public void getDatabaseData(String packet){
        try {
            Depacketizer dp = new Depacketizer(packet);
            BaudRate = dp.getInt();
            int RMode = dp.getInt();
            if (RMode < 12 || RMode > 14) {
                ReaderMode = 1;
            } else {
                ReaderMode = (RMode - 12);
            }
            ReaderTypeLevel = dp.getString();
            UseRdrDB = dp.getString();
            CheckShiftHoliday = dp.getInt();
            CheckPin = dp.getInt();
            CheckValidityDOB = dp.getInt();
            AutoOffline = dp.getString();
            AntiPassBack = dp.getString();
            APBLevel = dp.getString();
            StoreAllRec = dp.getString();
            MultiRejAlarm = dp.getInt();
            DoorDelay = dp.getString();
            LedDelay = dp.getString();
            InvalidAlarm = dp.getString();
            TamperAlarm = dp.getString();
            DoorOpenAlarm = dp.getString();
            OnlineRespTimeout = dp.getString();
            KeyTimeOut = dp.getString();
            AppID1 = dp.getString();
            AppID2 = dp.getString();
            ReaderString = dp.getString();
            Location = dp.getString();
            Division = dp.getString();
            ReaderType = dp.getString();
            SelfIP = dp.getString();
            SubnetMask = dp.getString();
            GateWayIP = dp.getString();
            ServerIP1 = dp.getString();
            ServerIP2 = dp.getString();
            ListenPort = dp.getString();
            ServerPort = dp.getString();
            Zone = dp.getString();
            ReaderModel = dp.getString();
        } catch (Exception ex) {
            Logger.getLogger(RSClass.class.getName()).log(Level.SEVERE, null, ex);
        }              
    }

    public void getReaderMenu1Menu2(String packet) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        ReaderNo = dp.getString();
        BaudRate = dp.getInt() - 7;
        ReaderMode = dp.getInt() - 12;
        APBLevel = dp.getString();
        UseRdrDB = dp.getString();
        CheckShiftHoliday = dp.getInt() - 15;
        CheckPin = dp.getInt() - 21;
        CheckValidityDOB = dp.getInt() - 17;
        AutoOffline = dp.getString();
        AntiPassBack = dp.getString();
        UnusedEntry = dp.getString();
        UnitCode = dp.getString();
        DoorDelay = dp.getString();
        LedDelay = dp.getString();
        DoorOpenAlarm = dp.getString();
        KeyTimeOut = dp.getString();
        PUTmOut = dp.getString();
        PUCnt = dp.getString();
        OnlineRespTimeout = dp.getString();
        MultiRejAlarm = dp.getInt() & 1;
        AppID1 = Integer.toString((dp.getInt() * 256) + dp.getInt());
        AppID2 = Integer.toString((dp.getInt() * 256) + dp.getInt());
    }

    public void getReaderIPSettings(String packet) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        SelfIP = dp.getString() + "." + dp.getString() + "." + dp.getString() + "." + dp.getString();
        SubnetMask = dp.getString() + "." + dp.getString() + "." + dp.getString() + "." + dp.getString();
        GateWayIP = dp.getString() + "." + dp.getString() + "." + dp.getString() + "." + dp.getString();
        ServerIP1 = dp.getString() + "." + dp.getString() + "." + dp.getString() + "." + dp.getString();
        ServerIP2 = dp.getString() + "." + dp.getString() + "." + dp.getString() + "." + dp.getString();
        ListenPort = Integer.toString((dp.getInt() * 256) + dp.getInt());
        ServerPort = Integer.toString((dp.getInt() * 256) + dp.getInt());
        Building = Integer.toString((dp.getInt() * 256) + dp.getInt());

    }

    public void getReaderString(String packet) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        String s = "";
        while (!dp.isEmpty()) {
            s += (char) dp.getInt();
        }
        ReaderString = s;
    }

    public void getReaderRecordPointer(String packet) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        TotalRec = Integer.toString((dp.getInt() * 256) + dp.getInt());
        RecOnS1 = Integer.toString((dp.getInt() * 256) + dp.getInt());
        RecOnS2 = Integer.toString((dp.getInt() * 256) + dp.getInt());
    }

    public void getReaderDateTime(String packet) throws Exception {
        Depacketizer dp = new Depacketizer(packet);
        dp.getString();
        DateTime = dp.getString() + "-" + dp.getString() + "-" + "20" + dp.getString() + " " + dp.getString() + ":" + dp.getString() + ":" + dp.getString();
    }

    public String CreatePacketMenu3() throws Exception {
        Packetizer p = new Packetizer();
        p.addBool(ChkRecS1);
        if (ChkRecS1) {
            p.addInt(Integer.parseInt(RecOnS1) / 256);
            p.addInt(Integer.parseInt(RecOnS1) & 255);
        }
        p.addBool(ChkRecS2);
        if (ChkRecS2) {
            p.addInt(Integer.parseInt(RecOnS2) / 256);
            p.addInt(Integer.parseInt(RecOnS2) & 255);
        }
        p.addBool(ChkRdrStr);
        if (ChkRdrStr) {
            p.addString(ReaderString);
        }
        p.addBool(ChkDate);
        return p.getPacket();
    }

    public String CreatePacketIPSettings() throws Exception {
        String[] temp;
        Packetizer p = new Packetizer();
        temp = SelfIP.split("\\.");
        p.addString(temp[0]);
        p.addString(temp[1]);
        p.addString(temp[2]);
        p.addString(temp[3]);
        temp = SubnetMask.split("\\.");
        p.addString(temp[0]);
        p.addString(temp[1]);
        p.addString(temp[2]);
        p.addString(temp[3]);
        temp = GateWayIP.split("\\.");
        p.addString(temp[0]);
        p.addString(temp[1]);
        p.addString(temp[2]);
        p.addString(temp[3]);
        temp = ServerIP1.split("\\.");
        p.addString(temp[0]);
        p.addString(temp[1]);
        p.addString(temp[2]);
        p.addString(temp[3]);
        temp = ServerIP2.split("\\.");
        p.addString(temp[0]);
        p.addString(temp[1]);
        p.addString(temp[2]);
        p.addString(temp[3]);
        p.addInt(Integer.parseInt(ListenPort) / 256);
        p.addInt(Integer.parseInt(ListenPort) & 255);
        p.addInt(Integer.parseInt(ServerPort) / 256);
        p.addInt(Integer.parseInt(ServerPort) & 255);
        p.addInt(Integer.parseInt(Building) / 256);
        p.addInt(Integer.parseInt(Building) & 255);
        return p.getPacket();
    }

    public String CreatePacketMenu1() throws Exception {
        Packetizer p = new Packetizer();
        p.addString(ReaderNo);
        p.addInt(BaudRate + 7);
        p.addInt(ReaderMode + 12);
        p.addString(APBLevel);
        p.addString(UseRdrDB);
        p.addInt(CheckShiftHoliday + 15);
        p.addInt(CheckPin + 21);
        p.addInt(CheckValidityDOB + 17);
        p.addString(AutoOffline);
        p.addString(AntiPassBack);
        p.addString(UnusedEntry);
        p.addString(UnitCode);
        return p.getPacket();
    }

    public String CreatePacketMenu2() throws Exception {
        Packetizer p = new Packetizer();
        p.addString(DoorDelay);
        p.addString(LedDelay);
        p.addString(DoorOpenAlarm);
        p.addString(KeyTimeOut);
        p.addString(PUTmOut);
        p.addString(PUCnt);
        p.addString(OnlineRespTimeout);
        p.addInt(MultiRejAlarm);
        p.addInt(Integer.parseInt(AppID1) / 256);
        p.addInt(Integer.parseInt(AppID1) & 255);
        p.addInt(Integer.parseInt(AppID2) / 256);
        p.addInt(Integer.parseInt(AppID2) & 255);
        return p.getPacket();
    }

    public void clear() {
        ReaderNo = null;
        BaudRate = 0;
        ReaderMode = 0;
        ReaderTypeLevel = null;
        UseRdrDB = null;
        CheckShiftHoliday = 0;
        CheckPin = 0;
        CheckValidityDOB = 0;
        AutoOffline = null;
        AntiPassBack = null;
        APBLevel = null;
        StoreAllRec = null;
        MultiRejAlarm = 0;
        DoorDelay = null;
        LedDelay = null;
        InvalidAlarm = null;
        TamperAlarm = null;
        DoorOpenAlarm = null;
        OnlineRespTimeout = null;
        KeyTimeOut = null;
        AppID1 = null;
        AppID2 = null;
        ReaderString = null;
        ReaderType = null;
        SelfIP = null;
        SubnetMask = null;
        GateWayIP = null;
        ServerIP1 = null;
        ServerIP2 = null;
        ListenPort = null;
        ServerPort = null;
        Zone = null;
        ReaderModel = null;
        UnitCode = null;
        UnusedEntry = null;
        PUTmOut = null;
        PUCnt = null;
        Building = null;
        DateTime = null;
    }
}